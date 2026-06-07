package com.sbnavneet.projects.ai_app_builder.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.sbnavneet.projects.ai_app_builder.entity.ChatSession;
import com.sbnavneet.projects.ai_app_builder.entity.ChatSessionId;

public interface ChatSessionRepository extends JpaRepository<ChatSession, ChatSessionId> {

}
