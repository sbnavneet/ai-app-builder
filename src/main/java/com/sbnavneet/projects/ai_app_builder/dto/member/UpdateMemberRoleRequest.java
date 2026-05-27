package com.sbnavneet.projects.ai_app_builder.dto.member;

import com.sbnavneet.projects.ai_app_builder.enums.ProjectRole;

import jakarta.validation.constraints.NotNull;

public record UpdateMemberRoleRequest(
   @NotNull ProjectRole newRole
) {}