package com.sbnavneet.projects.ai_app_builder.dto.auth;

public record SignupRequest(
    String email,
    String name,
    String password
){}
