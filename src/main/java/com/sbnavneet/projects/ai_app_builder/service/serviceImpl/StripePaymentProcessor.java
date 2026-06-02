package com.sbnavneet.projects.ai_app_builder.service.serviceImpl;

import java.util.Map;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.sbnavneet.projects.ai_app_builder.dto.subscription.CheckoutRequest;
import com.sbnavneet.projects.ai_app_builder.dto.subscription.CheckoutResponse;
import com.sbnavneet.projects.ai_app_builder.dto.subscription.PortalResponse;
import com.sbnavneet.projects.ai_app_builder.entity.Plan;
import com.sbnavneet.projects.ai_app_builder.entity.User;
import com.sbnavneet.projects.ai_app_builder.enums.SubscriptionStatus;
import com.sbnavneet.projects.ai_app_builder.error.BadRequestException;
import com.sbnavneet.projects.ai_app_builder.error.ResourceNotFoundException;
import com.sbnavneet.projects.ai_app_builder.repository.PlanRepository;
import com.sbnavneet.projects.ai_app_builder.repository.UserRepository;
import com.sbnavneet.projects.ai_app_builder.security.AuthUtility;
import com.sbnavneet.projects.ai_app_builder.service.PaymentProcessor;
import com.sbnavneet.projects.ai_app_builder.service.SubscriptionService;
import com.stripe.param.checkout.SessionCreateParams;
import com.stripe.exception.StripeException;
import com.stripe.model.Invoice;
import com.stripe.model.StripeObject;
import com.stripe.model.Subscription;
import com.stripe.model.SubscriptionItem;
import com.stripe.model.checkout.Session;

import java.time.Instant;


import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class StripePaymentProcessor implements PaymentProcessor {

    private final AuthUtility authUtility;
    private final PlanRepository planRepository;
    private final UserRepository userRepository;
    private final SubscriptionService subscriptionService;

    @Value("${client.url}")
    private String frontEndUrl;
    
    @Override
    public CheckoutResponse createCheckoutSessionUrl(CheckoutRequest checkoutRequest) {
        Long userId = authUtility.getCurrentUser();
        User user = getUser(userId);

        Plan plan = planRepository
                .findById(checkoutRequest.planId()).orElseThrow(() -> new ResourceNotFoundException("Plan" , checkoutRequest.planId().toString()));
        var params = SessionCreateParams.builder()
                                                        .addLineItem(
                                                            SessionCreateParams.LineItem.builder().setPrice(plan.getStripePriceId()).setQuantity(1L).build()
                                                        )
                                                        .setMode(SessionCreateParams.Mode.SUBSCRIPTION)
                                                        .setSuccessUrl(frontEndUrl + "/success.html?session_id={CHECKOUT_SESSION_ID}")
                                                        .setCancelUrl(frontEndUrl + "/cancel.html")
                                                        .putMetadata("user_id", userId.toString())
                                                        .putMetadata("plan_id", plan.getId().toString());
                                                        
        try {
            String stripeCustomerId = user.getStripeCustomerId();
            if(stripeCustomerId == null || stripeCustomerId.isEmpty()){
                params.setCustomerEmail(user.getUsername());
            }else{
                params.setCustomer(stripeCustomerId);
            }
            Session session = Session.create(params.build());
            return new CheckoutResponse(session.getUrl());
        } catch (StripeException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public PortalResponse openCustomerPortal() {
        Long userId = authUtility.getCurrentUser();
        User user = getUser(userId);
        String stripeCustomerId = user.getStripeCustomerId();
        if(stripeCustomerId == null || stripeCustomerId.isEmpty()){
            throw new BadRequestException("User does not have a Stripe Customer Id, UserId : "+userId);
        }
        try{
            var portalSession = com.stripe.model.billingportal.Session.create(
            com.stripe.param.billingportal.SessionCreateParams.builder().setCustomer(stripeCustomerId).setReturnUrl(frontEndUrl).build()
            );
             return new PortalResponse(portalSession.getUrl());
        }catch(StripeException ex){
            throw new RuntimeException(ex);
        }    
    }

    @Override
    public void handleWebhook(String type, StripeObject stripeObject, Map<String, String> metadata) {
        switch(type){
            case "checkout.session.completed" -> handleCheckoutSessionCompleted((Session) stripeObject, metadata);
            case "customer.subscription.updated" -> handleCustomerSubscriptionUpdated((Subscription) stripeObject);
            case "customer.subscription.deleted" -> handleCustomerSubscriptionDeleted((Subscription) stripeObject);
            case "invoice.paid" -> handleInvoicePaid((Invoice) stripeObject);
            case "invoice.payment_failed" -> handleInvoicePaymentFailed((Invoice) stripeObject);
        }
    }


    private void handleCheckoutSessionCompleted(Session session, Map<String, String> metadata){
            if(session == null){
                return;
            }
            Long userId = Long.parseLong(metadata.get("user_id"));
            Long planId = Long.parseLong(metadata.get("plan_id"));

            String subscriptionId = session.getSubscription();
            String customerId = session.getCustomer();

            User user = getUser(userId);
            if(user.getStripeCustomerId() == null){
                user.setStripeCustomerId(customerId);
                userRepository.save(user);
            }

            subscriptionService.activateSubscription(userId, planId, subscriptionId, customerId);
    }

    private User getUser(Long userId) {
        return userRepository.findById(userId).orElseThrow(() -> new ResourceNotFoundException("User", userId.toString()));
    }

    private void handleCustomerSubscriptionUpdated(Subscription subscription){
        if(subscription == null){
            return;
        }

        SubscriptionStatus status = mapStripeStatusToEnum(subscription.getStatus());
        if(status == null){
            return;
        }
        SubscriptionItem item = subscription.getItems().getData().get(0);
        String priceId = item.getPrice().getId();
        Instant periodStart = toInstant(item.getCurrentPeriodStart());
        Instant periodEnd = toInstant(item.getCurrentPeriodEnd());
        Long planId = resolvePlanId(priceId);
        subscriptionService.updateSubscription(subscription.getId(), status, periodStart, periodEnd, subscription.getCancelAtPeriodEnd(), planId);
    }

    private void handleCustomerSubscriptionDeleted(Subscription subscription){
        if(subscription == null)return;
        subscriptionService.deleteSubscription(subscription.getId());
    }

    private void handleInvoicePaid(Invoice invoice){
        String subcriptionId = extractSubscriptionId(invoice);
        if(subcriptionId == null){
            return;
        }
        try{
            Subscription subscription = Subscription.retrieve(subcriptionId);
            var item = subscription.getItems().getData().get(0);
            Instant periodStart = toInstant(item.getCurrentPeriodStart());
            Instant periodEnd = toInstant(item.getCurrentPeriodEnd());
            subscriptionService.renewSubscription(subcriptionId, periodStart, periodEnd);
        }catch(StripeException ex){
            throw new RuntimeException(ex);
        }
    }

    private void handleInvoicePaymentFailed(Invoice invoice){
        String subcriptionId = extractSubscriptionId(invoice);
        if(subcriptionId == null){
            return;
        }
        subscriptionService.handlePaymentFailure(subcriptionId);
    }
    
// Utility Methods

    private Instant toInstant(Long epoch) {
        return epoch != null ? Instant.ofEpochSecond(epoch) : null;
    }

    private String extractSubscriptionId(Invoice invoice) {
        var parent = invoice.getParent();
        if (parent == null) return null;
        var subDetails = parent.getSubscriptionDetails();
        if (subDetails == null) return null;
        return subDetails.getSubscription();
    }

    public SubscriptionStatus mapStripeStatusToEnum(String status){

        return switch(status){
            case "active" -> SubscriptionStatus.ACTIVE;
            case "trialing" -> SubscriptionStatus.TRIALING;
            case "past_due" -> SubscriptionStatus.PAST_DUE;
            case "canceled" -> SubscriptionStatus.CANCELED;
            case "incomplete" -> SubscriptionStatus.INCOMPLETE;
            default -> {
                yield null;
            }
        };
    }

    private Long resolvePlanId(String priceId){
        return planRepository.findByStripePriceId(priceId.toString()).map(Plan::getId).orElse(null);
    }


}
