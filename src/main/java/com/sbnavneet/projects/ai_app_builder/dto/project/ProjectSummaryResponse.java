package com.sbnavneet.projects.ai_app_builder.dto.project;

import java.time.Instant;

public record ProjectSummaryResponse(
    Long id,
    String name,
    Instant createdAt,
    Instant updatedAt
) {}
