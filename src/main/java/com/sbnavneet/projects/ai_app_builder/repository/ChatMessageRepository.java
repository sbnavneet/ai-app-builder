package com.sbnavneet.projects.ai_app_builder.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.sbnavneet.projects.ai_app_builder.entity.ChatMessage;
import com.sbnavneet.projects.ai_app_builder.entity.ChatSession;

import java.util.List;


public interface ChatMessageRepository extends JpaRepository<ChatMessage, Long> {

    @Query("""
        SELECT DISTINCT cm FROM ChatMessage cm
        LEFT JOIN FETCH cm.chatEvents e
        WHERE cm.chatSession = :chatSession
        ORDER BY cm.createdAt ASC, e.seqOrder ASC
        """)
    List<ChatMessage> findByChatSession(ChatSession chatSession);
}
