package com.sbnavneet.projects.ai_app_builder.dto.subscription;

public record PlanLimitsResponse(
    String planName,
    int maxTokensPerDay,
    int maxProjects,
    boolean unlimitedAi
) {

}
