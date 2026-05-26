package com.sbnavneet.projects.ai_app_builder.entity;

import java.time.Instant;

import org.hibernate.annotations.CreationTimestamp;

import com.sbnavneet.projects.ai_app_builder.enums.MessageRole;

import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.FieldDefaults;

// @Entity
@Getter
@Setter
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ChatMessage {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;

    @ManyToOne
    @JoinColumn(name = "chat_session_id")
    ChatSession chatSession;

    String content;
    String toolCalls;
    Integer tokenUsed;
    
    @CreationTimestamp
    Instant createdAt;

    @Enumerated(value = EnumType.STRING)
    MessageRole role;
}
