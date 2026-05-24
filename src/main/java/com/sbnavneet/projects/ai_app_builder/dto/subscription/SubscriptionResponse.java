package com.sbnavneet.projects.ai_app_builder.dto.subscription;

import java.time.Instant;

public record SubscriptionResponse(
    PlanResponse plan,
    String status,
    Instant preiodEnd,
    Long tokenUsedThisCycle
) {

}
