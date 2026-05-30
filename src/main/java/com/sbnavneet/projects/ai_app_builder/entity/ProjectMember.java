package com.sbnavneet.projects.ai_app_builder.entity;

import java.time.Instant;

import com.sbnavneet.projects.ai_app_builder.enums.ProjectRole;

import jakarta.persistence.Column;
import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.MapsId;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.FieldDefaults;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@Table(name = "project_members")
@Builder
public class ProjectMember {

    @EmbeddedId
    ProjectMemberId id;
    
    @ManyToOne
    @MapsId("projectId")
    Project project;

    @ManyToOne
    @MapsId("userId")
    User user;

    @Enumerated(value = EnumType.STRING)
    @Column(nullable = false)
    ProjectRole role;

    Instant invitedAt;

    Instant acceptedAt;
    
}
