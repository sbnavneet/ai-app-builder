package com.sbnavneet.projects.ai_app_builder.service;


import com.sbnavneet.projects.ai_app_builder.dto.subscription.CheckoutRequest;
import com.sbnavneet.projects.ai_app_builder.dto.subscription.CheckoutResponse;
import com.sbnavneet.projects.ai_app_builder.dto.subscription.PortalResponse;
import com.sbnavneet.projects.ai_app_builder.dto.subscription.SubscriptionResponse;

public interface SubscriptionService {
    
    SubscriptionResponse getCurrentSubscription(Long userId);

    CheckoutResponse createCheckoutSessionUrl(Long userId, CheckoutRequest checkoutRequest);

    PortalResponse openCustomerPortal(Long userId);

}
