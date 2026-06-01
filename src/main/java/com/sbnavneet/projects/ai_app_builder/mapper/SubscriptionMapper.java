package com.sbnavneet.projects.ai_app_builder.mapper;

import org.mapstruct.Mapper;

import com.sbnavneet.projects.ai_app_builder.dto.subscription.PlanResponse;
import com.sbnavneet.projects.ai_app_builder.dto.subscription.SubscriptionResponse;
import com.sbnavneet.projects.ai_app_builder.entity.Plan;
import com.sbnavneet.projects.ai_app_builder.entity.Subscription;

@Mapper(componentModel = "spring")
public interface SubscriptionMapper {

    SubscriptionResponse toSubscriptionResponse(Subscription subscription);

    PlanResponse toPlanResponse(Plan plan);
}
