package com.sbnavneet.projects.ai_app_builder.dto.project;

import java.time.Instant;

import com.sbnavneet.projects.ai_app_builder.dto.auth.UserProfileResponse;

public record ProjectResponse(
    Long id,
    String name,
    Instant createAt,
    Instant updatedAt,
    UserProfileResponse owner
) {

}
