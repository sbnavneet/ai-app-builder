
package com.sbnavneet.projects.ai_app_builder.dto.member;

import com.sbnavneet.projects.ai_app_builder.enums.ProjectRole;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record InviteMemberRequest(
    @Email @NotBlank String email,
    @NotNull ProjectRole role
) {

}
