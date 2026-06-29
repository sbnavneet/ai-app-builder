package com.sbnavneet.projects.ai_app_builder.service.serviceImpl;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.sbnavneet.projects.ai_app_builder.dto.chat.ChatResponse;
import com.sbnavneet.projects.ai_app_builder.entity.ChatMessage;
import com.sbnavneet.projects.ai_app_builder.entity.ChatSession;
import com.sbnavneet.projects.ai_app_builder.entity.ChatSessionId;
import com.sbnavneet.projects.ai_app_builder.enums.MessageRole;
import com.sbnavneet.projects.ai_app_builder.mapper.ChatMapper;
import com.sbnavneet.projects.ai_app_builder.repository.ChatMessageRepository;
import com.sbnavneet.projects.ai_app_builder.repository.ChatSessionRepository;
import com.sbnavneet.projects.ai_app_builder.security.AuthUtility;
import com.sbnavneet.projects.ai_app_builder.service.ChatMessageService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
public class ChatMessageServiceImpl implements ChatMessageService {

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

    @Override
    @Transactional
    public void saveMessage(Long projectId, Long userId, String content, MessageRole role) {
        ChatSessionId sessionId = new ChatSessionId(projectId, userId);
        ChatSession chatSession = chatSessionRepository.getReferenceById(sessionId);

        ChatMessage message = ChatMessage.builder()
                .chatSession(chatSession)
                .content(content)
                .role(role)
                .build();

        chatMessageRepository.save(message);
        log.info("Saved {} message for project {} user {}", role, projectId, userId);
    }
}
