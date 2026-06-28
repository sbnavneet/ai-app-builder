package com.sbnavneet.projects.ai_app_builder.service.serviceImpl;

import java.util.List;

import org.springframework.stereotype.Service;

import com.sbnavneet.projects.ai_app_builder.dto.chat.ChatResponse;
import com.sbnavneet.projects.ai_app_builder.entity.ChatMessage;
import com.sbnavneet.projects.ai_app_builder.entity.ChatSession;
import com.sbnavneet.projects.ai_app_builder.entity.ChatSessionId;
import com.sbnavneet.projects.ai_app_builder.mapper.ChatMapper;
import com.sbnavneet.projects.ai_app_builder.repository.ChatMessageRepository;
import com.sbnavneet.projects.ai_app_builder.repository.ChatSessionRepository;
import com.sbnavneet.projects.ai_app_builder.security.AuthUtility;
import com.sbnavneet.projects.ai_app_builder.service.ChatMessageService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ChatMessageServiceImpl implements ChatMessageService{

    private final ChatMessageRepository chatMessageRepository;
    private final ChatSessionRepository chatSessionRepository;
    private final AuthUtility authUtil;
    private final ChatMapper chatMapper;
    
    @Override
    public List<ChatResponse> getProjectChatHistory(Long projectId) {
        Long userId = authUtil.getCurrentUser();
        ChatSession chatSession = chatSessionRepository.getReferenceById(
            new ChatSessionId(projectId, userId)
        );
        List<ChatMessage> chatMessagesList = chatMessageRepository.findByChatSession(chatSession);
        
        return chatMapper.toChatResponse(chatMessagesList);
    }


}
