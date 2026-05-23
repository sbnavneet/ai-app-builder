package com.sbnavneet.projects.ai_app_builder.entity;

import java.time.Instant;

import com.sbnavneet.projects.ai_app_builder.enums.ProjectRole;

import jakarta.persistence.Entity;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.FieldDefaults;

@Entity
@Getter
@Setter
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ProjectMember {
    
    ProjectMemberId id;
    
    Project project;
    User user;
    ProjectRole role;

    Instant invitedAt;
    Instant acceptedAt;
    
}
