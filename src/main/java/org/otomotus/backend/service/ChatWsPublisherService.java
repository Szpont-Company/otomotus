package org.otomotus.backend.service;

import lombok.RequiredArgsConstructor;
import org.otomotus.backend.dto.MessageWsDto;
import org.otomotus.backend.entity.MessageEntity;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ChatWsPublisherService {

    private final SimpMessagingTemplate messagingTemplate;

    public void newMessage(MessageEntity msg) {

        UUID conversationId = msg.getConversation().getId();

        messagingTemplate.convertAndSend(
                "/topic/conversations/" + conversationId,
                toDto(msg, "NEW")
        );

        messagingTemplate.convertAndSend(
                "/topic/notifications/" + msg.getRecipientId(),
                "New message!"
        );
    }

    public void editMessage(MessageEntity msg) {
        messagingTemplate.convertAndSend(
                "/topic/conversations/" + msg.getConversation().getId(),
                toDto(msg, "EDIT")
        );
    }

    public void deleteMessage(MessageEntity msg) {
        messagingTemplate.convertAndSend(
                "/topic/conversations/" + msg.getConversation().getId(),
                toDto(msg, "DELETE")
        );
    }

    public void readMessage(MessageEntity msg) {
        messagingTemplate.convertAndSend(
                "/topic/conversations/" + msg.getConversation().getId(),
                toDto(msg, "READ")
        );
    }

    private MessageWsDto toDto(MessageEntity msg, String type) {
        return new MessageWsDto(
                msg.getId(),
                msg.getConversation().getId(),
                msg.getSenderId(),
                msg.getRecipientId(),
                msg.getMsgContent(),
                msg.isRead(),
                msg.getTimestamp(),
                type
        );
    }
}
