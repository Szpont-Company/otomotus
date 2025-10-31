package org.otomotus.backend.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.UUID;

@Getter @Setter
@Entity
public class MessageEntity {
    @Id
    @GeneratedValue
    private UUID id;

    private UUID conversationId;
    private UUID senderId;
    private UUID recipientId;

    private String msgContent;

    private LocalDateTime timestamp;
    private LocalDateTime editedTimestamp;
    private boolean isRead;
    private LocalDateTime readTimestamp;

}
