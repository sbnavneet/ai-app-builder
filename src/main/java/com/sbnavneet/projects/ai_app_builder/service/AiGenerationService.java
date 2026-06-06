package com.sbnavneet.projects.ai_app_builder.service;

import reactor.core.publisher.Flux;

public interface AiGenerationService {
     Flux<String> streamResponse(String message, Long projectId);
}
