package com.sbnavneet.projects.ai_app_builder.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.sbnavneet.projects.ai_app_builder.entity.Project;

public interface ProjectRepository extends JpaRepository<Project, Long> {

    @Query("""
            SELECT p FROM Project p 
            WHERE p.deletedAt IS NULL
            AND p.owner.id = :userId
            ORDER BY p.updatedAt DESC
            """)
    List<Project> findAllProjectByOwner(@Param("userId")Long ownerId);

    @Query("""
            SELECT p FROM Project p
            WHERE p.deletedAt IS NULL 
            AND p.owner.id = :userId
            AND p.id = :projectId
            """)
    Optional<Project> findAccessibleProjectByOwnerIdAndProjectId(@Param("userId") Long ownerId, @Param("projectId") Long id);
}
