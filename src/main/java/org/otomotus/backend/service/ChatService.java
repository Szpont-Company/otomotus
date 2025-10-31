package org.otomotus.backend.service;

import lombok.RequiredArgsConstructor;
import org.otomotus.backend.entity.ConversationEntity;
import org.otomotus.backend.entity.MessageEntity;
import org.otomotus.backend.exception.ResourceNotFoundException;
import org.otomotus.backend.repository.ConversationRepository;
import org.otomotus.backend.repository.MessageRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ChatService {
    private final MessageRepository messageRepository;
    private final ConversationRepository conversationRepository;

    public MessageEntity sendMessage(UUID senderId, UUID recipientId, String content) {
        ConversationEntity conversation = conversationRepository.findConversationBetweenUsers(senderId, recipientId)
                .orElseGet(() -> createNewConversation(senderId, recipientId));

        MessageEntity msg = new MessageEntity();
        msg.setConversationId(conversation.getId());
        msg.setSenderId(senderId);
        msg.setRecipientId(recipientId);
        msg.setMsgContent(content);
        msg.setTimestamp(LocalDateTime.now());
        msg.setRead(false);

        return messageRepository.save(msg);
    }

    private ConversationEntity createNewConversation(UUID senderId, UUID recipientId) {
        ConversationEntity conversation = new ConversationEntity();
        conversation.setBuyerId(senderId);
        conversation.setSellerId(recipientId);
        conversation.setCreatedAt(LocalDateTime.now());
        return conversationRepository.save(conversation);
    }

    public List<MessageEntity> getConversationMessages(UUID conversationId) {
        return messageRepository.findByConversationId(conversationId);
    }

    public MessageEntity editMessage(UUID msgId, String content) {
        MessageEntity msg = messageRepository.findById(msgId)
                .orElseThrow(() -> new RuntimeException("Message not found!"));
        msg.setEditedTimestamp(LocalDateTime.now());
        msg.setMsgContent(content);
        messageRepository.save(msg);

        return msg;
    }

    public MessageEntity markRead(UUID msgId) {
        MessageEntity msg = messageRepository.findById(msgId)
                .orElseThrow(() -> new RuntimeException("Message not found!"));
        if(!msg.isRead()) {
            msg.setRead(true);
            msg.setReadTimestamp(LocalDateTime.now());
            messageRepository.save(msg);
        }
        return msg;
    }

    public MessageEntity markUnread(UUID msgId) {
        MessageEntity msg = messageRepository.findById(msgId)
                .orElseThrow(() -> new RuntimeException("Message not found!"));
        if(msg.isRead()) {
            msg.setRead(false);
            msg.setReadTimestamp(null);
            messageRepository.save(msg);
        }
        return msg;
    }

    public void deleteMessage(UUID msgId) {
        MessageEntity msg = messageRepository.findById(msgId)
                .orElseThrow(() -> new ResourceNotFoundException("Message not found!"));
            messageRepository.delete(msg);
    }
}
