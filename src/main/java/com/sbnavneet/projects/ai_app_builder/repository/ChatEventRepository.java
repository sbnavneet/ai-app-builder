package com.sbnavneet.projects.ai_app_builder.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.sbnavneet.projects.ai_app_builder.entity.ChatEvent;

public interface ChatEventRepository extends JpaRepository<ChatEvent, Long> {

}
