package com.sbnavneet.projects.ai_app_builder.service.serviceImpl;

import java.time.Instant;
import java.util.List;

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

    @Override
    public List<ProjectSummaryResponse> getUserProjects(Long userId) {
        return projectRepository.findAllProjectByOwner(userId)
            .stream()
            .map(projectMapper::toProjectSummaryResponse)
            .toList();
    }

    @Override
    public ProjectResponse getUserProjectById(Long id, Long userId) {
        Project project = projectRepository.findAccessibleProjectByOwnerIdAndProjectId(userId, id).orElseThrow(() -> new ResourceNotFoundException("Project" , Long.toString(id)));
        return projectMapper.toProjectResponse(project);
    }

    @Override
    public ProjectResponse createProject(ProjectRequest request, Long userId) {
        User owner = userRepository.findById(userId).orElseThrow(() -> new ResourceNotFoundException("User", Long.toString(userId)));
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
    public ProjectResponse updateProject(Long id, ProjectRequest request, Long userId) {
        Project project = projectRepository.findAccessibleProjectByOwnerIdAndProjectId(userId, id).orElseThrow(() -> new ResourceNotFoundException("Project", id.toString()));

        project.setName(request.name());
        return projectMapper.toProjectResponse(projectRepository.save(project));
    }

    @Override
    public void softDelete(Long id, Long userId) {
        Project project = projectRepository.findAccessibleProjectByOwnerIdAndProjectId(userId, id).orElseThrow(() -> new ResourceNotFoundException("Project", id.toString()));

        project.setDeletedAt(Instant.now());
        projectRepository.save(project);
    }

}
