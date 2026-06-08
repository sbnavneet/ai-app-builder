package com.sbnavneet.projects.ai_app_builder.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.sbnavneet.projects.ai_app_builder.entity.ProjectFile;

public interface ProjectFileRepository extends JpaRepository<ProjectFile, Long>{

    List<ProjectFile> findByProjectId(Long projectId);

    Optional<ProjectFile> findByProjectIdAndPath(Long projectId, String path);

}
