package com.sbnavneet.projects.ai_app_builder.entity;

import java.time.Instant;

import jakarta.persistence.Entity;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class User {
    private Long id;
    private String email;
    private String passwordHash;
    private String name;
    private String avatarUrl;
    private Instant createdAt;
    private Instant updatedAt;
    private Instant deletedAt; //soft-delete

}
