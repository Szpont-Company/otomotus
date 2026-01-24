package org.otomotus.backend.dto;

import java.time.LocalDateTime;
import java.util.UUID;

public record ConversationListDto(
        UUID id,
        UUID productId,
        UUID otherUserId,
        String otherUserName,
        String lastMessage,
        LocalDateTime lastMessageAt,
        Long unreadCount
) {}

