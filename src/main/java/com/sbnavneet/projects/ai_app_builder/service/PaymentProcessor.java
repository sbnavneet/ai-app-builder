package com.sbnavneet.projects.ai_app_builder.service;

import java.util.Map;

import com.sbnavneet.projects.ai_app_builder.dto.subscription.CheckoutRequest;
import com.sbnavneet.projects.ai_app_builder.dto.subscription.CheckoutResponse;
import com.sbnavneet.projects.ai_app_builder.dto.subscription.PortalResponse;
import com.stripe.model.StripeObject;

public interface PaymentProcessor {

    CheckoutResponse createCheckoutSessionUrl(CheckoutRequest checkoutRequest);

    PortalResponse openCustomerPortal();

    void handleWebhook(String type, StripeObject stripeObject, Map<String, String> metadata);


}
