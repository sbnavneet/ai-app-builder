package com.sbnavneet.projects.ai_app_builder.entity;

import java.time.Instant;

import jakarta.persistence.Entity;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.FieldDefaults;

@Entity
@Getter
@Setter
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ChatSession {
    Project project;
    User user;
    String title;
    Instant createdAt;
    Instant updatedAt;
    Instant deleteAt;
    
}
