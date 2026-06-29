package com.sbnavneet.projects.ai_app_builder.service.serviceImpl;
import java.util.List;
import java.util.Map;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;

import com.sbnavneet.projects.ai_app_builder.dto.chat.StreamResponse;
import com.sbnavneet.projects.ai_app_builder.entity.ChatEvent;
import com.sbnavneet.projects.ai_app_builder.entity.ChatMessage;
import com.sbnavneet.projects.ai_app_builder.entity.ChatSession;
import com.sbnavneet.projects.ai_app_builder.entity.ChatSessionId;
import com.sbnavneet.projects.ai_app_builder.entity.Project;
import com.sbnavneet.projects.ai_app_builder.entity.User;
import com.sbnavneet.projects.ai_app_builder.enums.ChatEventType;
import com.sbnavneet.projects.ai_app_builder.enums.MessageRole;
import com.sbnavneet.projects.ai_app_builder.error.ResourceNotFoundException;
import com.sbnavneet.projects.ai_app_builder.llm.AiGenerationTools;
import com.sbnavneet.projects.ai_app_builder.llm.LlmResponseParser;
import com.sbnavneet.projects.ai_app_builder.llm.PromptUtils;
import com.sbnavneet.projects.ai_app_builder.llm.advisor.FileTreeContextAdvisor;
import com.sbnavneet.projects.ai_app_builder.repository.ChatMessageRepository;
import com.sbnavneet.projects.ai_app_builder.repository.ChatSessionRepository;
import com.sbnavneet.projects.ai_app_builder.repository.ProjectRepository;
import com.sbnavneet.projects.ai_app_builder.repository.UserRepository;
import com.sbnavneet.projects.ai_app_builder.security.AuthUtility;
import com.sbnavneet.projects.ai_app_builder.service.AiGenerationService;
import com.sbnavneet.projects.ai_app_builder.service.FileService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Flux;
import reactor.core.scheduler.Schedulers;

@Service
@RequiredArgsConstructor
@Slf4j
public class AiGenerationServiceImpl implements AiGenerationService {

    private final ChatClient chatClient;
    private final FileService fileService;
    private final AuthUtility authUtility;
    private final ProjectRepository projectRepository;
    private final UserRepository userRepository;
    private final ChatSessionRepository chatSessionRepository;
    private final FileTreeContextAdvisor fileTreeContextAdvisor;
    private final LlmResponseParser llmResponseParser;
    private final ChatMessageRepository chatMessageRepository;

    @Override
    @PreAuthorize("@security.canEditProjects(#projectId)")
    public Flux<StreamResponse> streamResponse(String userMessage, Long projectId) {
        Long userId = authUtility.getCurrentUser();
        ChatSession chatSession = createChatSessionIfNotExists(projectId, userId);

        Map<String, Object> advisorParams = Map.of(
                "userId", userId,
                "projectId", projectId
        );
        AiGenerationTools codeGenerationTools = new AiGenerationTools(fileService, projectId);
        StringBuilder fullResponseBuffer = new StringBuilder();
        return chatClient.prompt()
                .system(PromptUtils.CODE_GENERATION_SYSTEM_PROMPT)
                .user(userMessage)
                .advisors(advisorSpec -> {
                            advisorSpec.params(advisorParams);
                            advisorSpec.advisors(fileTreeContextAdvisor);
                        }
                )
                .tools(codeGenerationTools)
                .stream()
                .chatResponse()
                .log("CHAT_STREAM")
                .doOnNext(response -> {
                    String content = response.getResult().getOutput().getText();
                    log.info("Chunk received: {}", content);
                    fullResponseBuffer.append(content);
                })
                .doOnComplete(() -> {
                    Schedulers.boundedElastic().schedule(() -> {
                        finalizeChats(userMessage, chatSession, fullResponseBuffer.toString(), projectId);
                    });
                })
                .doOnError(error -> log.error("Error during streaming for projectId: {}", projectId))
                .map(response -> {
                    String text = response.getResult().getOutput().getText();
                    log.info("Text body: {} ", text);
                    return new StreamResponse(text != null ? text : "");
                });
    }

    private void finalizeChats(String userMessage, ChatSession chatSession, String fullText, Long projectId){
        // Save the user message
        chatMessageRepository.save(
            ChatMessage.builder()
                .chatSession(chatSession)
                .content(userMessage)
                .role(MessageRole.USER)
                .build()
        );

        // Build assistant message
        ChatMessage assistantChatMessage = ChatMessage.builder()
                .role(MessageRole.ASSISTANT)
                .chatSession(chatSession)
                .content(fullText)
                .build();

        // Parse events and attach to the message
        List<ChatEvent> chatEventList = llmResponseParser.parseChatEvents(fullText, assistantChatMessage);
        assistantChatMessage.setChatEvents(chatEventList);

        // Save files from FILE_EDIT events
        chatEventList.stream()
                .filter(e -> e.getChatEventType() == ChatEventType.FILE_EDIT)
                .forEach(e -> fileService.saveFile(projectId, e.getFilePath(), e.getContent()));

        // Single save — cascade persists the events automatically
        chatMessageRepository.save(assistantChatMessage);
    }

    private ChatSession createChatSessionIfNotExists(Long projectId, Long userId) {
        ChatSessionId chatSessionId = new ChatSessionId(projectId, userId);
        ChatSession chatSession = chatSessionRepository.findById(chatSessionId).orElse(null);

        if(chatSession == null) {
            Project project = projectRepository.findById(projectId)
                    .orElseThrow(() -> new ResourceNotFoundException("Project", projectId.toString()));
            User user = userRepository.findById(userId)
                    .orElseThrow(() -> new ResourceNotFoundException("User", userId.toString()));

            chatSession = ChatSession.builder()
                    .id(chatSessionId)
                    .project(project)
                    .user(user)
                    .build();

            chatSession = chatSessionRepository.save(chatSession);
        }
        return chatSession;
    }
}