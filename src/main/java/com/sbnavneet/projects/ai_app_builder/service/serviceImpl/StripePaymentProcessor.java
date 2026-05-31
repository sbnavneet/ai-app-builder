package com.sbnavneet.projects.ai_app_builder.service.serviceImpl;

import java.util.Map;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.sbnavneet.projects.ai_app_builder.dto.subscription.CheckoutRequest;
import com.sbnavneet.projects.ai_app_builder.dto.subscription.CheckoutResponse;
import com.sbnavneet.projects.ai_app_builder.dto.subscription.PortalResponse;
import com.sbnavneet.projects.ai_app_builder.entity.Plan;
import com.sbnavneet.projects.ai_app_builder.entity.User;
import com.sbnavneet.projects.ai_app_builder.error.ResourceNotFoundException;
import com.sbnavneet.projects.ai_app_builder.repository.PlanRepository;
import com.sbnavneet.projects.ai_app_builder.repository.UserRepository;
import com.sbnavneet.projects.ai_app_builder.security.AuthUtility;
import com.sbnavneet.projects.ai_app_builder.service.PaymentProcessor;
import com.stripe.param.checkout.SessionCreateParams;
import com.stripe.exception.StripeException;
import com.stripe.model.StripeObject;
import com.stripe.model.checkout.Session;


import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class StripePaymentProcessor implements PaymentProcessor {

    private final AuthUtility authUtility;
    private final PlanRepository planRepository;
    private final UserRepository userRepository;

    @Value("${client.url}")
    private String frontEndUrl;
    
    @Override
    public CheckoutResponse createCheckoutSessionUrl(CheckoutRequest checkoutRequest) {
        Long userId = authUtility.getCurrentUser();
        User user = userRepository.findById(userId).orElseThrow(() -> new ResourceNotFoundException("User", userId.toString()));

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
                params.setCustomerEmail(stripeCustomerId);
            }
            Session session = Session.create(params.build());
            return new CheckoutResponse(session.getUrl());
        } catch (StripeException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public PortalResponse openCustomerPortal(Long userId) {
        return null;
    }

    @Override
    public void handleWebhook(String type, StripeObject stripeObject, Map<String, String> metadata) {
        
    }

}
