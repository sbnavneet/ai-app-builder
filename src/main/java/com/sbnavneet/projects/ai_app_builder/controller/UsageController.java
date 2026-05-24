package com.sbnavneet.projects.ai_app_builder.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.sbnavneet.projects.ai_app_builder.dto.subscription.PlanLimitsResponse;
import com.sbnavneet.projects.ai_app_builder.dto.subscription.UsageTodayResponse;
import com.sbnavneet.projects.ai_app_builder.service.UsageService;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/usage")
public class UsageController {
    
    private UsageService usageService;

    @GetMapping("/today")
    public ResponseEntity<UsageTodayResponse> getTodayUsageOfUser(){
        Long userId = 1L;
        return ResponseEntity.ok(usageService.getTodayUsageOfUser(userId));
    }

    @GetMapping("/limits")
    public ResponseEntity<PlanLimitsResponse> getCurrentSubscriptionLimits(){
        Long userId = 1L;
        return ResponseEntity.ok(usageService.getCurrentSubscriptionLimits(userId));
    }
}
