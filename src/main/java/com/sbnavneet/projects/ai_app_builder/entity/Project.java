package com.sbnavneet.projects.ai_app_builder.entity;

import java.time.Instant;

import jakarta.persistence.Entity;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.FieldDefaults;

@Getter
@Setter
@AllArgsConstructor
@Entity
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Project {

    Long id;

    String name;
    User owner;
    Boolean isPublic = false;
    Instant createdAt;
    Instant updatedAt;
    Instant deletedAt;
}
