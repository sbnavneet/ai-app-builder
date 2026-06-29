package com.sbnavneet.projects.ai_app_builder.service;

import java.util.List;

import com.sbnavneet.projects.ai_app_builder.dto.chat.ChatResponse;
import com.sbnavneet.projects.ai_app_builder.enums.MessageRole;

public interface ChatMessageService {

    List<ChatResponse> getProjectChatHistory(Long projectId);

    void saveMessage(Long projectId, Long userId, String content, MessageRole role);
}
