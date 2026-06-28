package com.sbnavneet.projects.ai_app_builder.mapper;

import java.util.List;

import org.mapstruct.Mapper;

import com.sbnavneet.projects.ai_app_builder.dto.chat.ChatResponse;
import com.sbnavneet.projects.ai_app_builder.entity.ChatMessage;

@Mapper(componentModel = "spring")
public interface ChatMapper {

    List<ChatResponse> toChatResponse(List<ChatMessage> chatMessageList);
}
