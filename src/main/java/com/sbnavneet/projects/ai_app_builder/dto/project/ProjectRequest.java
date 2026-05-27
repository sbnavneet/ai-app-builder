package com.sbnavneet.projects.ai_app_builder.dto.project;

import jakarta.validation.constraints.NotBlank;

public record ProjectRequest(
    @NotBlank String name
) {}
