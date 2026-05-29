package com.sbnavneet.projects.ai_app_builder.service;


import com.sbnavneet.projects.ai_app_builder.dto.auth.AuthResponse;
import com.sbnavneet.projects.ai_app_builder.dto.auth.LoginRequest;
import com.sbnavneet.projects.ai_app_builder.dto.auth.SignupRequest;

public interface AuthService {


    AuthResponse signup(SignupRequest signupRequest);

    AuthResponse login(LoginRequest loginDto);

    AuthResponse refresh(String refreshToken);

}
