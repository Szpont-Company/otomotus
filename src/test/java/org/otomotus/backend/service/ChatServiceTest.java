package org.otomotus.backend.service;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.otomotus.backend.entity.ConversationEntity;
import org.otomotus.backend.entity.MessageEntity;
import org.otomotus.backend.repository.ConversationRepository;
import org.otomotus.backend.repository.MessageRepository;
import org.springframework.security.access.AccessDeniedException;

import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ChatServiceTest {

    @Mock private MessageRepository messageRepository;
    @Mock private ConversationRepository conversationRepository;
    @Mock private MailNotificationService mailNotificationService;

    @InjectMocks
    private ChatService chatService;

    @Test
    @DisplayName("Should create new conversation if one does not exist when sending message")
    void sendMessage_ShouldCreateConversation_WhenNoneExists() {
        // Given
        UUID senderId = UUID.randomUUID();
        UUID recipientId = UUID.randomUUID();
        UUID productId = UUID.randomUUID();
        String content = "Is this available?";

        when(conversationRepository.findConversationBetweenUsers(senderId, recipientId, productId))
                .thenReturn(Optional.empty());

        when(conversationRepository.save(any(ConversationEntity.class)))
                .thenAnswer(i -> i.getArguments()[0]); // Return what was saved

        when(messageRepository.save(any(MessageEntity.class)))
                .thenAnswer(i -> {
                    MessageEntity msg = i.getArgument(0);
                    msg.setRecipientId(recipientId); // Mock logic needed for notifyUser
                    return msg;
                });

        // When
        chatService.sendMessage(senderId, recipientId, productId, content);

        // Then
        verify(conversationRepository).save(any(ConversationEntity.class));
        verify(messageRepository).save(any(MessageEntity.class));
        verify(mailNotificationService).notifyMessage(any(MessageEntity.class));
    }

    @Test
    @DisplayName("Should edit message successfully when user is the sender")
    void editMessage_ShouldUpdateContent_WhenUserIsSender() {
        // Given
        UUID msgId = UUID.randomUUID();
        UUID userId = UUID.randomUUID();
        String newContent = "Updated text";

        MessageEntity message = new MessageEntity();
        message.setId(msgId);
        message.setSenderId(userId);
        message.setMsgContent("Old text");

        when(messageRepository.findById(msgId)).thenReturn(Optional.of(message));
        when(messageRepository.save(any(MessageEntity.class))).thenReturn(message);

        // When
        MessageEntity result = chatService.editMessage(msgId, newContent, userId);

        // Then
        assertEquals(newContent, result.getMsgContent());
        verify(messageRepository).save(message);
    }

    @Test
    @DisplayName("Should throw AccessDeniedException when user tries to edit someone else's message")
    void editMessage_ShouldThrowException_WhenUserIsNotSender() {
        // Given
        UUID msgId = UUID.randomUUID();
        UUID ownerId = UUID.randomUUID();
        UUID hackerId = UUID.randomUUID();

        MessageEntity message = new MessageEntity();
        message.setSenderId(ownerId);

        when(messageRepository.findById(msgId)).thenReturn(Optional.of(message));

        // When & Then
        assertThrows(AccessDeniedException.class,
                () -> chatService.editMessage(msgId, "Hacked", hackerId)
        );
    }

    @Test
    @DisplayName("Should throw AccessDeniedException when user tries to delete someone else's message")
    void deleteMessage_ShouldThrowException_WhenUserIsNotSender() {
        // Given
        UUID msgId = UUID.randomUUID();
        UUID ownerId = UUID.randomUUID();
        UUID otherUserId = UUID.randomUUID();

        MessageEntity message = new MessageEntity();
        message.setSenderId(ownerId);

        when(messageRepository.findById(msgId)).thenReturn(Optional.of(message));

        // When & Then
        assertThrows(AccessDeniedException.class,
                () -> chatService.deleteMessage(msgId, otherUserId)
        );
        verify(messageRepository, never()).delete(any());
    }
}