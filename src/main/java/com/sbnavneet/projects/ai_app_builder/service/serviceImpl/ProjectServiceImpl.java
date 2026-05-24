package com.sbnavneet.projects.ai_app_builder.service.serviceImpl;

import java.util.List;

import com.sbnavneet.projects.ai_app_builder.dto.project.ProjectRequest;
import com.sbnavneet.projects.ai_app_builder.dto.project.ProjectResponse;
import com.sbnavneet.projects.ai_app_builder.dto.project.ProjectSummaryResponse;
import com.sbnavneet.projects.ai_app_builder.service.ProjectService;

public class ProjectServiceImpl implements ProjectService{

    @Override
    public List<ProjectSummaryResponse> getUserProjects(Long userId) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'getUserProjects'");
    }

    @Override
    public ProjectResponse getUserProjectById(Long id, Long userId) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'getUserProjectById'");
    }

    @Override
    public ProjectResponse createProject(ProjectRequest request, Long userId) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'createProject'");
    }

    @Override
    public ProjectResponse updateProject(Long id, ProjectRequest request, Long userId) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'updateProject'");
    }

    @Override
    public void softDelete(Long id, Long userId) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'softDelete'");
    }

}
