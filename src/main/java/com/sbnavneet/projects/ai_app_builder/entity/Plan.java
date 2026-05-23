package com.sbnavneet.projects.ai_app_builder.entity;

import jakarta.persistence.Entity;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@Entity
public class Plan {
    private Long id;
    private String name;
    private String stripePriceId;
    private Integer maxProjects;
    private Integer maxTokensPerDay;
    private Boolean unlimitedAi;
    private Boolean active;
}
