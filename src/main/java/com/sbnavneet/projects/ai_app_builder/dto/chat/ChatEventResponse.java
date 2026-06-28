package com.sbnavneet.projects.ai_app_builder.dto.chat;

import com.sbnavneet.projects.ai_app_builder.enums.ChatEventType;

public record ChatEventResponse(
        Long id,
        ChatEventType type,
        Integer sequenceOrder,
        String content,
        String filePath,
        String metadata
) {
}