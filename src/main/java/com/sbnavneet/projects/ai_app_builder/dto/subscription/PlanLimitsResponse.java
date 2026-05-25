package com.sbnavneet.projects.ai_app_builder.dto.subscription;

public record PlanLimitsResponse(
    String planName,
    Integer maxTokensPerDay,
    Integer maxProjects,
    Boolean unlimitedAi
) {

}
