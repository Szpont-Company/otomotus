package org.otomotus.backend.service;

import lombok.RequiredArgsConstructor;
import org.otomotus.backend.entity.ConversationEntity;
import org.otomotus.backend.entity.MessageEntity;
import org.otomotus.backend.exception.ResourceNotFoundException;
import org.otomotus.backend.repository.ConversationRepository;
import org.otomotus.backend.repository.MessageRepository;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ChatService {
    private final MessageRepository messageRepository;
    private final ConversationRepository conversationRepository;
    private final MailNotificationService mailNotificationService;
    private final ChatWsPublisherService wsPublisher;

    public MessageEntity sendMessage(UUID senderId, UUID recipientId, UUID productId, String content) {
        ConversationEntity conversation = conversationRepository.findConversationBetweenUsers(senderId, recipientId, productId)
                .orElseGet(() -> createNewConversation(senderId, recipientId, productId));

        MessageEntity msg = new MessageEntity();
        msg.setConversation(conversation);
        msg.setSenderId(senderId);
        msg.setRecipientId(recipientId);
        msg.setMsgContent(content);

        MessageEntity saved = messageRepository.save(msg);

        mailNotificationService.notifyMessage(saved);
        wsPublisher.newMessage(saved);

        return saved;
    }

    private ConversationEntity createNewConversation(UUID senderId, UUID recipientId, UUID productId) {
        ConversationEntity conversation = new ConversationEntity();
        conversation.setBuyerId(senderId);
        conversation.setSellerId(recipientId);
        conversation.setProductId(productId);

        return conversationRepository.save(conversation);
    }

    public List<MessageEntity> getConversationMessages(UUID conversationId, UUID userId) {
        ConversationEntity conversation = conversationRepository.findById(conversationId)
                .orElseThrow(() -> new ResourceNotFoundException("Conversation not found!"));

        if(!conversation.getBuyerId().equals(userId) && !conversation.getSellerId().equals(userId)) {
            throw new AccessDeniedException("You are not part of this conversation");
        }

        return messageRepository.findAllByConversation_Id(conversationId);
    }

    public MessageEntity editMessage(UUID msgId, String content, UUID userId) {
        MessageEntity msg = messageRepository.findById(msgId)
                .orElseThrow(() -> new ResourceNotFoundException("Message not found!"));

        if(!msg.getSenderId().equals(userId)) {
            throw new AccessDeniedException("You are not allowed to edit this message!");
        }

        msg.setMsgContent(content);
        messageRepository.save(msg);

        wsPublisher.editMessage(msg);

        return msg;
    }

    public MessageEntity markRead(UUID msgId, UUID userId) {
        MessageEntity msg = messageRepository.findById(msgId)
                .orElseThrow(() -> new ResourceNotFoundException("Message not found!"));

        if(!msg.getRecipientId().equals(userId)) {
            throw new AccessDeniedException("You are not allowed to mark this message as read!");
        }

        if(!msg.isRead()) {
            msg.setRead(true);
            msg.setReadTimestamp(LocalDateTime.now());
            messageRepository.save(msg);

            wsPublisher.readMessage(msg);
        }
        return msg;
    }

    public MessageEntity markUnread(UUID msgId, UUID userId) {
        MessageEntity msg = messageRepository.findById(msgId)
                .orElseThrow(() -> new ResourceNotFoundException("Message not found!"));

        if(!msg.getRecipientId().equals(userId)) {
            throw new AccessDeniedException("You are not allowed to mark this message as not read!");
        }

        if(msg.isRead()) {
            msg.setRead(false);
            msg.setReadTimestamp(null);
            messageRepository.save(msg);

            wsPublisher.readMessage(msg);
        }
        return msg;
    }

    public void deleteMessage(UUID msgId, UUID userId) {
        MessageEntity msg = messageRepository.findById(msgId)
                .orElseThrow(() -> new ResourceNotFoundException("Message not found!"));

        if(!msg.getSenderId().equals(userId)) {
            throw new AccessDeniedException("You are not allowed to delete this message!");
        }
            messageRepository.delete(msg);
            wsPublisher.deleteMessage(msg);
    }
}
