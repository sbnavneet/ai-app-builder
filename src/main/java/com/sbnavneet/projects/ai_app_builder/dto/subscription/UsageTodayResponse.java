package com.sbnavneet.projects.ai_app_builder.dto.subscription;

public record UsageTodayResponse(
    Integer tokensUsed,
    Integer tokensLimit,
    Integer previewRunning,
    Integer previewLimit
) {

}
