package com.sbnavneet.projects.ai_app_builder.mapper;

import java.util.List;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import com.sbnavneet.projects.ai_app_builder.dto.chat.ChatEventResponse;
import com.sbnavneet.projects.ai_app_builder.dto.chat.ChatResponse;
import com.sbnavneet.projects.ai_app_builder.entity.ChatEvent;
import com.sbnavneet.projects.ai_app_builder.entity.ChatMessage;

@Mapper(componentModel = "spring")
public interface ChatMapper {

    @Mapping(source = "chatEvents", target = "events")
    @Mapping(source = "tokenUsed", target = "tokensUsed")
    ChatResponse toChatResponse(ChatMessage chatMessage);

    List<ChatResponse> toChatResponse(List<ChatMessage> chatMessageList);

    @Mapping(source = "chatEventType", target = "type")
    @Mapping(source = "seqOrder", target = "sequenceOrder")
    @Mapping(source = "metaData", target = "metadata")
    ChatEventResponse toChatEventResponse(ChatEvent chatEvent);
}
