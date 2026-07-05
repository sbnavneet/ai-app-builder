package com.sbnavneet.projects.ai_app_builder.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.sbnavneet.projects.ai_app_builder.entity.Project;

@Repository
public interface ProjectRepository extends JpaRepository<Project, Long> {

    @Query("""
            SELECT p FROM Project p
            JOIN ProjectMember pm ON pm.project = p
            WHERE p.deletedAt IS NULL
            AND pm.user.id = :userId
            ORDER BY p.updatedAt DESC
            """)
    List<Project> findAllProjectByOwner(@Param("userId") Long ownerId);

    @Query("""
            SELECT p FROM Project p
            JOIN ProjectMember pm ON pm.project = p
            WHERE p.deletedAt IS NULL
            AND p.id = :projectId
            AND pm.user.id = :userId
            AND pm.role = com.sbnavneet.projects.ai_app_builder.enums.ProjectRole.OWNER
            """)
    Optional<Project> findAccessibleProjectByOwnerIdAndProjectId(@Param("userId") Long ownerId, @Param("projectId") Long id);

    Optional<Project> findByIdAndDeletedAtIsNull(Long id);
}
