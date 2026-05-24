package com.sbnavneet.projects.ai_app_builder.dto.subscription;

public record PlanResponse(
    Long id,
    String name,
    Integer maxProjects,
    Integer maxTokenPerDay,
    Boolean unlimitedAi,
    String price
) {

}
