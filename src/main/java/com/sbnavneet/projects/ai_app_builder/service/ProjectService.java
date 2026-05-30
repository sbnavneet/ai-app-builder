package com.sbnavneet.projects.ai_app_builder.service;

import java.util.List;


import com.sbnavneet.projects.ai_app_builder.dto.project.ProjectRequest;
import com.sbnavneet.projects.ai_app_builder.dto.project.ProjectResponse;
import com.sbnavneet.projects.ai_app_builder.dto.project.ProjectSummaryResponse;

public interface ProjectService {

    
    List<ProjectSummaryResponse> getUserProjects();

    ProjectResponse getUserProjectById(Long id);

    ProjectResponse createProject(ProjectRequest request);

    ProjectResponse updateProject(Long id, ProjectRequest request);

    void softDelete(Long id);

}
