package com.sbnavneet.projects.ai_app_builder.entity;

import java.time.Instant;

import com.sbnavneet.projects.ai_app_builder.enums.MessageRole;

import jakarta.persistence.Entity;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.FieldDefaults;

@Entity
@Getter
@Setter
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ChatMessage {
    Long id;
    ChatSession chatSession;
    String content;
    String toolCalls;
    Integer tokenUsed;
    Instant createdAt;
    MessageRole role;
}
