package com.sbnavneet.projects.ai_app_builder.service;


import com.sbnavneet.projects.ai_app_builder.dto.auth.UserProfileResponse;

public interface UserService {

    UserProfileResponse getProfile(Long userId);

}
