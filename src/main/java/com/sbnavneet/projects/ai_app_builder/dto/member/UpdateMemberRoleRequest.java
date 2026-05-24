package com.sbnavneet.projects.ai_app_builder.dto.member;

import com.sbnavneet.projects.ai_app_builder.enums.ProjectRole;

public record UpdateMemberRoleRequest(
    ProjectRole newRole
) {}