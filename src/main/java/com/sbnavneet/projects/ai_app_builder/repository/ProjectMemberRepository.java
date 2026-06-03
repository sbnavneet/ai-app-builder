package com.sbnavneet.projects.ai_app_builder.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.sbnavneet.projects.ai_app_builder.entity.ProjectMember;
import com.sbnavneet.projects.ai_app_builder.entity.ProjectMemberId;
import com.sbnavneet.projects.ai_app_builder.enums.ProjectRole;


@Repository
public interface ProjectMemberRepository extends JpaRepository<ProjectMember, ProjectMemberId> {

    List<ProjectMember> findByIdProjectIdAndAcceptedAtIsNotNull(Long projectId);

    @Query("SELECT pm.role FROM ProjectMember pm WHERE pm.id.projectId = :projectId AND pm.id.userId = :userId")
    Optional<ProjectRole> findRoleByProjectIdAndUserId(Long projectId, Long userId);

    @Query("SELECT COUNT(pm) FROM ProjectMember pm WHERE pm.id.userId = :userId AND pm.role = 'OWNER'")
    Integer countProjectOwnedByUser(@Param("userId") Long userId);
}
