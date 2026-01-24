package org.otomotus.backend.service;

import lombok.RequiredArgsConstructor;
import org.otomotus.backend.dto.ConversationListDto;
import org.otomotus.backend.entity.ConversationEntity;
import org.otomotus.backend.entity.MessageEntity;
import org.otomotus.backend.exception.ResourceNotFoundException;
import org.otomotus.backend.repository.ConversationRepository;
import org.otomotus.backend.repository.MessageRepository;
import org.otomotus.backend.repository.UserRepository;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ChatService {
    private final MessageRepository messageRepository;
    private final ConversationRepository conversationRepository;
    private final MailNotificationService mailNotificationService;
    private final UserRepository userRepository;
    private final SimpMessagingTemplate messagingTemplate;
    UserService userService;

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

        notifyUser(saved.getRecipientId(), saved, "NEW");

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
        MessageEntity edited =  messageRepository.save(msg);

        notifyUser(edited.getRecipientId(), edited, "UPDATE");

        return edited;
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

            notifyUser(msg.getRecipientId(), msg, "READ");
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

            notifyUser(msg.getRecipientId(), msg, "UNREAD");
        }
        return msg;
    }

    public void deleteMessage(UUID msgId, UUID userId) {
        MessageEntity msg = messageRepository.findById(msgId)
                .orElseThrow(() -> new ResourceNotFoundException("Message not found!"));

        if(!msg.getSenderId().equals(userId)) {
            throw new AccessDeniedException("You are not allowed to delete this message!");
        }
            UUID userID = msg.getRecipientId();
            messageRepository.delete(msg);
            Map<String, Object> payload = new HashMap<>();
            payload.put("deletedMessageId", msgId);
            payload.put("content", "Message Deleted");
            notifyUser(userID, payload, "DELETE");
    }

    private void notifyUser(UUID userId, Object payload, String type) {
        userRepository.findById(userId).ifPresent(user -> {

            Object objectToSend = payload;

            if (payload instanceof MessageEntity) {
                MessageEntity msg = (MessageEntity) payload;
                Map<String, Object> map = new HashMap<>();
                map.put("type", type);

                map.put("id", msg.getId());
                map.put("senderId", msg.getSenderId());
                map.put("recipientId", msg.getRecipientId());
                map.put("msgContent", msg.getMsgContent());
                map.put("timestamp", msg.getTimestamp());
                map.put("editedTimestamp", msg.getEditedTimestamp());
                map.put("read", msg.isRead());
                map.put("readTimestamp", msg.getReadTimestamp());
                map.put("conversationId", msg.getConversationId());

                objectToSend = map;
            }
            messagingTemplate.convertAndSendToUser(
                    user.getUsername(),
                    "/queue/messages",
                    objectToSend
            );
        });
    }

    public List<ConversationListDto> getUserConversations(UUID id) {
        return conversationRepository.findConversationsForUser(id);
    }
}

