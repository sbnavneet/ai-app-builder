package com.sbnavneet.projects.ai_app_builder.entity;

import java.time.Instant;

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
public class ProjectFile {

    Long id;
    Project project;
    String path;
    String minioObjectKey;
    User createdBy;
    User updatedBy;
    Instant createdAt;
    Instant updatedAt;

}
