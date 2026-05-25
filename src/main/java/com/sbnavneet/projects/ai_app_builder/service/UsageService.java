package com.sbnavneet.projects.ai_app_builder.service;


import com.sbnavneet.projects.ai_app_builder.dto.subscription.PlanLimitsResponse;
import com.sbnavneet.projects.ai_app_builder.dto.subscription.UsageTodayResponse;

public interface UsageService {

    UsageTodayResponse getTodayUsageOfUser(Long userId);

    PlanLimitsResponse getCurrentSubscriptionLimits(Long userId);

}
