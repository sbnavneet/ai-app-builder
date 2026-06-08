package com.sbnavneet.projects.ai_app_builder.mapper;

import java.util.List;

import org.mapstruct.Mapper;

import com.sbnavneet.projects.ai_app_builder.dto.project.FileNode;
import com.sbnavneet.projects.ai_app_builder.entity.ProjectFile;

@Mapper(componentModel = "spring")
public interface ProjectFileMapper {
    List<FileNode> toListOfFileNode(List<ProjectFile> projectFileList);
}
