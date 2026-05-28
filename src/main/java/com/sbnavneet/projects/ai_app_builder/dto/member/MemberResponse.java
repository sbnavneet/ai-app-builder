package com.sbnavneet.projects.ai_app_builder.dto.member;

import java.time.Instant;

import com.sbnavneet.projects.ai_app_builder.enums.ProjectRole;

public record MemberResponse(
    Long userId,
    String username,
    String name,
    ProjectRole projectRole,
    Instant invitedAt
) {

}
