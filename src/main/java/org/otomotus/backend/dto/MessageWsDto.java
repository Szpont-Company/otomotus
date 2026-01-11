package org.otomotus.backend.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class MessageWsDto {
    private UUID id;
    private UUID conversationId;
    private UUID senderId;
    private UUID recipientId;
    private String content;
    private boolean read;
    private LocalDateTime createdAt;
    private String type; // ex. NEW, EDIT, DELETE, READ
}
