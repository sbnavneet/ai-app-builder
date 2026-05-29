package com.sbnavneet.projects.ai_app_builder.dto.auth;

public record AuthResponse(String token, UserProfileResponse user, String refreshToken){

}
