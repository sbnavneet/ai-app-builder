package com.sbnavneet.projects.ai_app_builder.service;

import com.sbnavneet.projects.ai_app_builder.dto.chat.StreamResponse;

import reactor.core.publisher.Flux;

public interface AiGenerationService {
     Flux<StreamResponse> streamResponse(String message, Long projectId);
}
