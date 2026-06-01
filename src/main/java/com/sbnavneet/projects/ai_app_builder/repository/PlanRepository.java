package com.sbnavneet.projects.ai_app_builder.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.sbnavneet.projects.ai_app_builder.entity.Plan;

public interface PlanRepository extends JpaRepository<Plan, Long> {

    Optional<Plan> findByStripePriceId(String string);

}
