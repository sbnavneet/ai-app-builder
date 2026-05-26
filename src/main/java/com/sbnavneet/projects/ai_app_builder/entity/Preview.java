package com.sbnavneet.projects.ai_app_builder.entity;

import java.time.Instant;

import org.hibernate.annotations.CreationTimestamp;

import com.sbnavneet.projects.ai_app_builder.enums.PreviewStatus;

import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.FieldDefaults;

// @Entity
@Getter
@Setter
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Preview {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;

    @ManyToOne
    @JoinColumn(name = "project_id")
    Project project;
    String nameSpace;
    String podName;
    String priviewUrl;

    @Enumerated(value = EnumType.STRING)
    PreviewStatus status;

    Instant startedAt;

    Instant terminatedAt;
    
    @CreationTimestamp
    Instant createdAt;

}
