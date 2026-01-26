package org.otomotus.backend.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.UUID;

/**
 * DTO dla wiadomości przesyłanej przez WebSocket.
 * <p>
 * Zawiera wszystkie dane wiadomości wraz z typem zdarzenia dla powiadomień
 * w czasie rzeczywistym.
 * </p>
 *
 * @author Otomotus Development Team
 * @version 1.0
 */
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
