package com.sbnavneet.projects.ai_app_builder.controller;

import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RestController;

import com.sbnavneet.projects.ai_app_builder.service.AiGenerationService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.http.codec.ServerSentEvent;
import reactor.core.publisher.Flux;
import com.sbnavneet.projects.ai_app_builder.dto.chat.ChatRequest;
import com.sbnavneet.projects.ai_app_builder.dto.chat.StreamResponse;

import lombok.RequiredArgsConstructor;


@RestController
@RequiredArgsConstructor
public class ChatController {

    private final AiGenerationService aiGenerationService;


    @PostMapping(value = "/api/chat/stream", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public Flux<ServerSentEvent<StreamResponse>> streamChat(@RequestBody ChatRequest request) {
        return aiGenerationService.streamResponse(request.message(), request.projectId())
                .map(data -> ServerSentEvent.<StreamResponse>builder()
                        .data(data)
                        .build());
    }
    
}
