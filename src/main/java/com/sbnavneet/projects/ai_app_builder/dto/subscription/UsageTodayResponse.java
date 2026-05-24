package com.sbnavneet.projects.ai_app_builder.dto.subscription;

public record UsageTodayResponse(
    int tokensUsed,
    int tokensLimit,
    int previewRunning,
    int previewLimit
) {

}
