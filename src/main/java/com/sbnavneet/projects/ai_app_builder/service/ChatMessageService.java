package com.sbnavneet.projects.ai_app_builder.service;

import java.util.List;

import com.sbnavneet.projects.ai_app_builder.dto.chat.ChatResponse;

public interface ChatMessageService {

    List<ChatResponse> getProjectChatHistory(Long projectId);

}
