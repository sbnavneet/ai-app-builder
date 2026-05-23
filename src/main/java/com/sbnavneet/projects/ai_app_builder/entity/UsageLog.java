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
public class UsageLog {
    Long id;
    User user;
    Project project;
    
    String action;
    Integer tokenUsed;
    Integer durationMs;
    String metaData; //JSON of {model_used, prompt_used}

    Instant createdAt;
    
}
