package com.sbnavneet.projects.ai_app_builder.repository;

import java.util.Optional;
import java.util.Set;

import org.springframework.data.jpa.repository.JpaRepository;

import com.sbnavneet.projects.ai_app_builder.entity.Subscription;
import com.sbnavneet.projects.ai_app_builder.enums.SubscriptionStatus;

public interface SubscriptionRepository extends JpaRepository<Subscription, Long>{

    Optional<Subscription> findByUserIdAndStatusIn(Long userId, Set<SubscriptionStatus> status);

    Boolean existsByStripeSubscriptionId(String subscriptionId);

    Optional<Subscription> findByStripeSubscriptionId(String gatewaySubcriptionId);

}
