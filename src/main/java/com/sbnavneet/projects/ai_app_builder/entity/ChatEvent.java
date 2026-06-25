package com.sbnavneet.projects.ai_app_builder.entity;

import org.hibernate.annotations.ManyToAny;

import com.sbnavneet.projects.ai_app_builder.enums.ChatEventType;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "chat_events")
@Data
@Builder
public class ChatEvent {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id; 

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(nullable = false)
    ChatMessage chatMessage;

    @Column(nullable = false)
    Integer seqOrder;

    @Column(columnDefinition = "text")
    String content;

    String filePath;

    @Enumerated(EnumType.STRING)
    ChatEventType chatEventType;

    @Column(columnDefinition = "text")
    String metaData;
}
