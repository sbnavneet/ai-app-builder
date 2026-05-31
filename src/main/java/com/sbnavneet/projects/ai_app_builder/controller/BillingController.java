package com.sbnavneet.projects.ai_app_builder.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RestController;

import com.sbnavneet.projects.ai_app_builder.dto.subscription.CheckoutRequest;
import com.sbnavneet.projects.ai_app_builder.dto.subscription.CheckoutResponse;
import com.sbnavneet.projects.ai_app_builder.dto.subscription.PlanResponse;
import com.sbnavneet.projects.ai_app_builder.dto.subscription.PortalResponse;
import com.sbnavneet.projects.ai_app_builder.dto.subscription.SubscriptionResponse;
import com.sbnavneet.projects.ai_app_builder.service.PaymentProcessor;
import com.sbnavneet.projects.ai_app_builder.service.PlanService;
import com.sbnavneet.projects.ai_app_builder.service.SubscriptionService;
import com.stripe.exception.SignatureVerificationException;
import com.stripe.model.EventDataObjectDeserializer;
import com.stripe.model.StripeObject;
import com.stripe.model.checkout.Session;
import com.stripe.model.Event;
import com.stripe.net.Webhook;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
public class BillingController {

    private final PlanService planService;
    private final PaymentProcessor paymentProcessor;
    private final SubscriptionService subscriptionService;

    @Value("${stripe.webhook-secret}")
    private String webhookSecret;

    @GetMapping("/api/plans")
    public ResponseEntity<List<PlanResponse>> getAllPlans() {
        return ResponseEntity.ok(planService.getAllPlans());
    }

    @GetMapping("/api/me/subscription")
    public ResponseEntity<SubscriptionResponse> getCurrentSubscription() {
        Long userId = 1L;
        return ResponseEntity.ok(subscriptionService.getCurrentSubscription(userId));
    }

    @PostMapping("/api/payments/checkout")
    public ResponseEntity<CheckoutResponse> createCheckoutResponse(@RequestBody CheckoutRequest checkoutRequest) {
        return ResponseEntity.ok(paymentProcessor.createCheckoutSessionUrl(checkoutRequest));
    }

    @PostMapping("/api/payments/portal")
    public ResponseEntity<PortalResponse> openCustomerPortal() {
        Long userId = 1L;
        return ResponseEntity.ok(paymentProcessor.openCustomerPortal(userId));
    }

    @PostMapping("/api/webhooks/payments")
    public ResponseEntity<String> handlePaymentWebhook(@RequestBody String payload,
            @RequestHeader("Stripe-Signature") String signatureHeader) {
        try {
            Event event = Webhook.constructEvent(payload, signatureHeader, webhookSecret);
            EventDataObjectDeserializer deserializer = event.getDataObjectDeserializer();
            StripeObject stripeObject;
            if (deserializer.getObject().isPresent()) {
                stripeObject = deserializer.getObject().get();
            } else {
                try{
                    stripeObject = deserializer.deserializeUnsafe();
                    if(stripeObject == null){
                        return ResponseEntity.ok().build();
                    }
                }catch (Exception e){
                    return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Deserialization failed");
                }
            }

            Map<String, String> metadata = new HashMap<>();
            if (stripeObject instanceof Session session) {
                metadata = session.getMetadata();
            }

            paymentProcessor.handleWebhook(event.getType(), stripeObject, metadata);
            return ResponseEntity.ok("success");

        } catch (SignatureVerificationException ex) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Invalid signature");
        }

    }

}
