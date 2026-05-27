package com.sbnavneet.projects.ai_app_builder.service.serviceImpl;

import java.time.Instant;
import java.util.List;

import org.springframework.stereotype.Service;

import com.sbnavneet.projects.ai_app_builder.dto.project.ProjectRequest;
import com.sbnavneet.projects.ai_app_builder.dto.project.ProjectResponse;
import com.sbnavneet.projects.ai_app_builder.dto.project.ProjectSummaryResponse;
import com.sbnavneet.projects.ai_app_builder.entity.Project;
import com.sbnavneet.projects.ai_app_builder.entity.User;
import com.sbnavneet.projects.ai_app_builder.error.ResourceNotFoundException;
import com.sbnavneet.projects.ai_app_builder.mapper.ProjectMapper;
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
        User owner = userRepository.findById(userId).orElseThrow();
        Project project = Project.builder()
                                 .name(request.name())
                                 .owner(owner)
                                 .isPublic(false)
                                 .build();
        project = projectRepository.save(project);
        return projectMapper.toProjectResponse(project);
    }

    @Override
    public ProjectResponse updateProject(Long id, ProjectRequest request, Long userId) {
        Project project = projectRepository.findAccessibleProjectByOwnerIdAndProjectId(userId, id).orElseThrow();
        if(!project.getOwner().getId().equals(userId)){
            throw new RuntimeException("You are not allowed to update this project.");
        }
        project.setName(request.name());
        return projectMapper.toProjectResponse(projectRepository.save(project));
    }

    @Override
    public void softDelete(Long id, Long userId) {
        Project project = projectRepository.findAccessibleProjectByOwnerIdAndProjectId(userId, id).orElseThrow();
        if(!project.getOwner().getId().equals(userId)){
            throw new RuntimeException("You are not allowed to delete this project.");
        }
        project.setDeletedAt(Instant.now());
        projectRepository.save(project);
    }

}
