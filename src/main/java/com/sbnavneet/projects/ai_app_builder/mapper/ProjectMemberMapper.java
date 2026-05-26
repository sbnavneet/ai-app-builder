package com.sbnavneet.projects.ai_app_builder.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import com.sbnavneet.projects.ai_app_builder.dto.member.MemberResponse;
import com.sbnavneet.projects.ai_app_builder.entity.ProjectMember;
import com.sbnavneet.projects.ai_app_builder.entity.User;

@Mapper(componentModel = "spring")
public interface ProjectMemberMapper {

    @Mapping(target = "userId", source = "id")
    @Mapping(target = "projectRole", constant = "OWNER")
    MemberResponse toMemberResponse(User user);

    @Mapping(target = "userId", source = "user.id")
    @Mapping(target = "email", source = "user.email")
    @Mapping(target = "name", source = "user.name")
    @Mapping(target = "avatarUrl", source = "user.avatarUrl")
    @Mapping(target = "projectRole", source = "role")
    MemberResponse toMemberResponse(ProjectMember projectMember);
}
