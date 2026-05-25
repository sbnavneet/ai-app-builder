package com.sbnavneet.projects.ai_app_builder.mapper;

import org.mapstruct.Mapper;

import com.sbnavneet.projects.ai_app_builder.dto.project.ProjectRequest;
import com.sbnavneet.projects.ai_app_builder.dto.project.ProjectResponse;
import com.sbnavneet.projects.ai_app_builder.dto.project.ProjectSummaryResponse;
import com.sbnavneet.projects.ai_app_builder.entity.Project;

@Mapper(componentModel = "spring")
public interface ProjectMapper {

    ProjectResponse toProjectResponse(Project projectEntity);

    ProjectSummaryResponse toProjectSummaryResponse(Project projectEntity);

    Project toProjectEntity(ProjectRequest projectRequest);
}
