package com.sbnavneet.projects.ai_app_builder.service;

import java.util.List;


import com.sbnavneet.projects.ai_app_builder.dto.subscription.PlanResponse;

public interface PlanService {

    List<PlanResponse> getAllPlans();

}
