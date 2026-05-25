package com.sbnavneet.projects.ai_app_builder.service.serviceImpl;

import org.springframework.stereotype.Service;

import com.sbnavneet.projects.ai_app_builder.dto.subscription.PlanLimitsResponse;
import com.sbnavneet.projects.ai_app_builder.dto.subscription.UsageTodayResponse;
import com.sbnavneet.projects.ai_app_builder.service.UsageService;
@Service
public class UsageServiceImpl implements UsageService{

    @Override
    public UsageTodayResponse getTodayUsageOfUser(Long userId) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'getTodayUsageOfUser'");
    }

    @Override
    public PlanLimitsResponse getCurrentSubscriptionLimits(Long userId) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'getCurrentSubscriptionLimits'");
    }

}
