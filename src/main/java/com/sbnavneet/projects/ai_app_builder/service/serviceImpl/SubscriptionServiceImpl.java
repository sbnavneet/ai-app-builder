package com.sbnavneet.projects.ai_app_builder.service.serviceImpl;

import java.time.Instant;
import java.util.Set;

import org.springframework.stereotype.Service;

import com.sbnavneet.projects.ai_app_builder.dto.subscription.SubscriptionResponse;
import com.sbnavneet.projects.ai_app_builder.entity.Plan;
import com.sbnavneet.projects.ai_app_builder.entity.Subscription;
import com.sbnavneet.projects.ai_app_builder.entity.User;
import com.sbnavneet.projects.ai_app_builder.enums.SubscriptionStatus;
import com.sbnavneet.projects.ai_app_builder.error.ResourceNotFoundException;
import com.sbnavneet.projects.ai_app_builder.mapper.SubscriptionMapper;
import com.sbnavneet.projects.ai_app_builder.repository.PlanRepository;
import com.sbnavneet.projects.ai_app_builder.repository.SubscriptionRepository;
import com.sbnavneet.projects.ai_app_builder.repository.UserRepository;
import com.sbnavneet.projects.ai_app_builder.security.AuthUtility;
import com.sbnavneet.projects.ai_app_builder.service.SubscriptionService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class SubscriptionServiceImpl implements SubscriptionService{

    private final SubscriptionMapper subscriptionMapper;
    private final SubscriptionRepository subscriptionRepository;
    private final AuthUtility authUtility;
    private final UserRepository userRepository;
    private final PlanRepository planRepository;


    @Override
    public SubscriptionResponse getCurrentSubscription() {
        Long userId = authUtility.getCurrentUser();
        Subscription subscription = subscriptionRepository.findByUserIdAndStatusIn(userId, Set.of(SubscriptionStatus.ACTIVE, SubscriptionStatus.PAST_DUE, SubscriptionStatus.TRIALING)).orElse(new Subscription());
        return subscriptionMapper.toSubscriptionResponse(subscription);
    }

    @Override
    public void activateSubscription(Long userId, Long planId, String subscriptionId, String customerId) {
        Boolean exists = subscriptionRepository.existsByStripeSubscriptionId(subscriptionId);
        if(exists) return;
        User user = getUser(userId);
        Plan plan = getPlan(planId);
        Subscription subscription = Subscription.builder()
                                                    .user(user)
                                                    .plan(plan)
                                                    .stripeSubscriptionId(subscriptionId)
                                                    .stripeCustomerId(customerId)
                                                    .status(SubscriptionStatus.INCOMPLETE)
                                                    .build();
        subscriptionRepository.save(subscription);


    }

    @Override
    public void updateSubscription(String id, SubscriptionStatus status, Instant periodStart, Instant periodEnd,
            Boolean cancelAtPeriodEnd, Long planId) {
        Subscription subscription = getSubscription(id);
        subscription.setStatus(status);
        if (periodStart != null) subscription.setCurrentPeriodStart(periodStart);
        if (periodEnd != null) subscription.setCurrentPeriodEnd(periodEnd);
        if (cancelAtPeriodEnd != null) subscription.setCancelAtPeriodEnd(cancelAtPeriodEnd);
        if (planId != null) {
            Plan plan = getPlan(planId);
            subscription.setPlan(plan);
        }
        subscriptionRepository.save(subscription);
    }

    @Override
    public void deleteSubscription(String id) {

    }

    @Override
    public void renewSubscription(String gatewaySubcriptionId, Instant periodStart, Instant periodEnd) {
        Subscription subscription = getSubscription(gatewaySubcriptionId);

        Instant newStart  = periodStart != null ? periodStart : subscription.getCurrentPeriodEnd();
        subscription.setCurrentPeriodStart(newStart);
        subscription.setCurrentPeriodEnd(periodEnd);

        if(subscription.getStatus() == SubscriptionStatus.PAST_DUE || subscription.getStatus() == SubscriptionStatus.INCOMPLETE ){
            subscription.setStatus(SubscriptionStatus.ACTIVE);
        }
        subscriptionRepository.save(subscription);
    }

    @Override
    public void handlePaymentFailure(String subcriptionId) {

    }

    //Utility Methods
    private Subscription getSubscription(String id){
        return subscriptionRepository.findByStripeSubscriptionId(id).orElseThrow(() -> new ResourceNotFoundException("Subscription Id" , id.toString()));
    }

    private User getUser(Long userId){
        return userRepository.findById(userId).orElseThrow(() -> new ResourceNotFoundException("User", userId.toString()));
    }

    private Plan getPlan(Long planId){
        return planRepository.findById(planId).orElseThrow(() -> new ResourceNotFoundException("Plan", planId.toString()));
    }

}
