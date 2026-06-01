package com.sbnavneet.projects.ai_app_builder.service;


import java.time.Instant;

import com.sbnavneet.projects.ai_app_builder.dto.subscription.CheckoutRequest;
import com.sbnavneet.projects.ai_app_builder.dto.subscription.CheckoutResponse;
import com.sbnavneet.projects.ai_app_builder.dto.subscription.PortalResponse;
import com.sbnavneet.projects.ai_app_builder.dto.subscription.SubscriptionResponse;
import com.sbnavneet.projects.ai_app_builder.enums.SubscriptionStatus;

public interface SubscriptionService {
    
    SubscriptionResponse getCurrentSubscription();
    
    void activateSubscription(Long userId, Long planId, String subscriptionId, String customerId);

    void updateSubscription(String id, SubscriptionStatus status, Instant periodStart, Instant periodEnd,
            Boolean cancelAtPeriodEnd, Long planId);

    void deleteSubscription(String id);

    void renewSubscription(String subcriptionId, Instant periodStart, Instant periodEnd);

    void handlePaymentFailure(String subcriptionId);

}
