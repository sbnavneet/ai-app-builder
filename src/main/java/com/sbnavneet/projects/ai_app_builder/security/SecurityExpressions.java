package com.sbnavneet.projects.ai_app_builder.security;

import org.springframework.stereotype.Component;

import com.sbnavneet.projects.ai_app_builder.enums.ProjectPermission;
import com.sbnavneet.projects.ai_app_builder.enums.ProjectRole;
import com.sbnavneet.projects.ai_app_builder.repository.ProjectMemberRepository;

import lombok.RequiredArgsConstructor;

@Component("security")
@RequiredArgsConstructor
public class SecurityExpressions {

    private final AuthUtility authUtility;
    private final ProjectMemberRepository projectMemberRepository;

    public boolean hasPermissions(Long projectId, ProjectPermission projectPermission){
        Long userId = authUtility.getCurrentUser();
        return projectMemberRepository.findRoleByProjectIdAndUserId(projectId, userId).map( role -> role.getPermissions().contains(projectPermission)).orElse(false);
    }

    public boolean canViewProjects(Long projectId){
        return hasPermissions(projectId, ProjectPermission.VIEW);
    }
    
    public boolean canEditProjects(Long projectId){
        return hasPermissions(projectId, ProjectPermission.EDIT);
    }

    public boolean canDeleteProjects(Long projectId){
        return hasPermissions(projectId, ProjectPermission.DELETE);
    }

    public boolean canViewMembers(Long projectId){
        return hasPermissions(projectId, ProjectPermission.VIEW_MEMBERS);
    }

    public boolean canManageMembers(Long projectId){
        return hasPermissions(projectId, ProjectPermission.MANAGE_MEMBERS);
    }

    public boolean canEditFiles(Long projectId){
        return hasPermissions(projectId, ProjectPermission.EDIT_FILES);
    }
}
