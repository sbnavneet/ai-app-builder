package com.sbnavneet.projects.ai_app_builder.entity;

import java.time.Instant;

import org.hibernate.annotations.CreationTimestamp;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.FieldDefaults;

// @Entity
@Getter
@Setter
@FieldDefaults(level = AccessLevel.PRIVATE)
public class UsageLog {
   
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;
    

    User user;
   
    Project project;
    
    String action;
    Integer tokenUsed;
    Integer durationMs;
    String metaData; //JSON of {model_used, prompt_used}

    @CreationTimestamp
    Instant createdAt;
    
}
