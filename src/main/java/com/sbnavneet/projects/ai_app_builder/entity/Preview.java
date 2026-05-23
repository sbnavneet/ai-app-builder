package com.sbnavneet.projects.ai_app_builder.entity;

import java.time.Instant;

import com.sbnavneet.projects.ai_app_builder.enums.PreviewStatus;

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
public class Preview {

    Long id;
    Project project;
    String nameSpace;
    String podName;
    String priviewUrl;
    PreviewStatus status;
    Instant startedAt;
    Instant terminatedAt;
    Instant createdAt;

}
