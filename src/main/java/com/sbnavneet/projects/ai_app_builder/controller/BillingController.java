package com.sbnavneet.projects.ai_app_builder.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.sbnavneet.projects.ai_app_builder.dto.subscription.CheckoutRequest;
import com.sbnavneet.projects.ai_app_builder.dto.subscription.CheckoutResponse;
import com.sbnavneet.projects.ai_app_builder.dto.subscription.PlanResponse;
import com.sbnavneet.projects.ai_app_builder.dto.subscription.PortalResponse;
import com.sbnavneet.projects.ai_app_builder.dto.subscription.SubscriptionResponse;
import com.sbnavneet.projects.ai_app_builder.service.PlanService;
import com.sbnavneet.projects.ai_app_builder.service.SubscriptionService;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
public class BillingController {

    private final PlanService planService;
    private final SubscriptionService subscriptionService;
    
    @GetMapping("/api/plans")
    public ResponseEntity<List<PlanResponse>> getAllPlans(){
        return ResponseEntity.ok(planService.getAllPlans());
    }

    @GetMapping("/api/me/subscription")
    public ResponseEntity<SubscriptionResponse> getCurrentSubscription(){
        Long userId = 1L;
        return ResponseEntity.ok(subscriptionService.getCurrentSubscription(userId));
    }

    @PostMapping("/api/stripe/checkout")
    public ResponseEntity<CheckoutResponse> createCheckoutResponse(@RequestBody CheckoutRequest checkoutRequest){
        Long userId  = 1L;
        return ResponseEntity.ok(subscriptionService.createCheckoutSessionUrl(userId, checkoutRequest));
    }

    @PostMapping("/api/stripe/portal")
    public ResponseEntity<PortalResponse> openCustomerPortal(){
        Long userId = 1L;
        return ResponseEntity.ok(subscriptionService.openCustomerPortal(userId));
    }


}
