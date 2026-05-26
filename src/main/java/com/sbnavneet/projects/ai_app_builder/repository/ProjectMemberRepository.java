package com.sbnavneet.projects.ai_app_builder.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.sbnavneet.projects.ai_app_builder.entity.ProjectMember;
import com.sbnavneet.projects.ai_app_builder.entity.ProjectMemberId;


@Repository
public interface ProjectMemberRepository extends JpaRepository<ProjectMember, ProjectMemberId> {

    List<ProjectMember> findByIdProjectId(Long projectId);
}
