package com.sbnavneet.projects.ai_app_builder.dto.auth;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record LoginRequest(
    @NotBlank @Email String username,
    @Size(min = 8, max = 15) String password
){}
