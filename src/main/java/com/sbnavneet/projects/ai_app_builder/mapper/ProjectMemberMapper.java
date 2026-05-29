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
    @Mapping(target = "invitedAt", ignore = true)
    MemberResponse toMemberResponse(User user);
    @Mapping(target = "userId", source = "user.id")
    @Mapping(target = "username", source = "user.username")
    @Mapping(target = "name", source = "user.name")
    @Mapping(target = "projectRole", source = "role")
    MemberResponse toMemberResponse(ProjectMember projectMember);
}
