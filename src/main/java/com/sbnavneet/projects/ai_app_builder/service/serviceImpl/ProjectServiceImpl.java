package com.sbnavneet.projects.ai_app_builder.service.serviceImpl;

import java.time.Instant;
import java.util.List;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;

import com.sbnavneet.projects.ai_app_builder.dto.project.ProjectRequest;
import com.sbnavneet.projects.ai_app_builder.dto.project.ProjectResponse;
import com.sbnavneet.projects.ai_app_builder.dto.project.ProjectSummaryResponse;
import com.sbnavneet.projects.ai_app_builder.entity.Project;
import com.sbnavneet.projects.ai_app_builder.entity.ProjectMember;
import com.sbnavneet.projects.ai_app_builder.entity.ProjectMemberId;
import com.sbnavneet.projects.ai_app_builder.entity.User;
import com.sbnavneet.projects.ai_app_builder.enums.ProjectRole;
import com.sbnavneet.projects.ai_app_builder.error.ResourceNotFoundException;
import com.sbnavneet.projects.ai_app_builder.mapper.ProjectMapper;
import com.sbnavneet.projects.ai_app_builder.repository.ProjectMemberRepository;
import com.sbnavneet.projects.ai_app_builder.repository.ProjectRepository;
import com.sbnavneet.projects.ai_app_builder.repository.UserRepository;
import com.sbnavneet.projects.ai_app_builder.security.AuthUtility;
import com.sbnavneet.projects.ai_app_builder.service.ProjectService;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional
public class ProjectServiceImpl implements ProjectService{

    private final ProjectRepository projectRepository;
    private final UserRepository userRepository;
    private final ProjectMapper projectMapper;
    private final ProjectMemberRepository projectMemberRepository;
    private final AuthUtility authUtility;

    @Override
    public List<ProjectSummaryResponse> getUserProjects() {
        return projectRepository.findAllProjectByOwner(authUtility.getCurrentUser())
            .stream()
            .map(projectMapper::toProjectSummaryResponse)
            .toList();
    }

    @Override
    @PreAuthorize("@security.canViewProjects(#id)")
    public ProjectResponse getUserProjectById(Long id) {
       Project project = projectRepository.findByIdAndDeletedAtIsNull(id).orElseThrow(() -> new ResourceNotFoundException("Project", id.toString()));
        return projectMapper.toProjectResponse(project);
    }

    @Override
    public ProjectResponse createProject(ProjectRequest request) {
        User owner = userRepository.getReferenceById(authUtility.getCurrentUser());
        Project project = Project.builder()
                                 .name(request.name())
                                 .isPublic(false)
                                 .build();
        project = projectRepository.save(project);

        ProjectMemberId projectMemberId = new ProjectMemberId(project.getId(), owner.getId());
        ProjectMember projectMember = ProjectMember.builder()
                                                            .id(projectMemberId)
                                                            .user(owner)
                                                            .project(project)
                                                            .role(ProjectRole.OWNER)
                                                            .acceptedAt(Instant.now())
                                                            .invitedAt(Instant.now())
                                                            .build();
        projectMemberRepository.save(projectMember);
        return projectMapper.toProjectResponse(project);
    }

    @Override
    @PreAuthorize("@security.canEditProjects(#id)")
    public ProjectResponse updateProject(Long id, ProjectRequest request) {
        Project project = projectRepository.findAccessibleProjectByOwnerIdAndProjectId(authUtility.getCurrentUser(), id).orElseThrow(() -> new ResourceNotFoundException("Project", id.toString()));

        project.setName(request.name());
        return projectMapper.toProjectResponse(projectRepository.save(project));
    }

    @Override
    @PreAuthorize("@security.canDeleteProjects(#id)")
    public void softDelete(Long id) {
        Project project = projectRepository.findAccessibleProjectByOwnerIdAndProjectId(authUtility.getCurrentUser(), id).orElseThrow(() -> new ResourceNotFoundException("Project", id.toString()));
        project.setDeletedAt(Instant.now());
        projectRepository.save(project);
    }

}
