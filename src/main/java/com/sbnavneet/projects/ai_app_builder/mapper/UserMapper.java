package com.sbnavneet.projects.ai_app_builder.mapper;

import org.mapstruct.Mapper;

import com.sbnavneet.projects.ai_app_builder.dto.auth.UserProfileResponse;
import com.sbnavneet.projects.ai_app_builder.entity.User;

@Mapper(componentModel = "spring")
public interface UserMapper {
    UserProfileResponse toUserProfileResponse(User user);

}
