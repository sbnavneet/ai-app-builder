package com.sbnavneet.projects.ai_app_builder.entity;

import java.time.Instant;

import com.sbnavneet.projects.ai_app_builder.enums.ProjectRole;

import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToOne;
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

    @EmbeddedId
    ProjectMemberId id;
    
    @ManyToOne
    @JoinColumn(name = "project_id")
    Project project;

    @OneToOne
    @JoinColumn(name = "user_id")
    User user;

    @Enumerated(value = EnumType.STRING)
    ProjectRole role;

    Instant invitedAt;

    Instant acceptedAt;
    
}
