package com.sbnavneet.projects.ai_app_builder.dto.chat;

import java.time.Instant;
import java.util.List;

import com.sbnavneet.projects.ai_app_builder.enums.MessageRole;

public record ChatResponse(
        Long id,
        MessageRole role,
        List<ChatEventResponse> events,
        String content,
        Integer tokensUsed,
        Instant createdAt

) {
}