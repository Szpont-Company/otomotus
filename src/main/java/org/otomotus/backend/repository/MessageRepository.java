package org.otomotus.backend.repository;

import org.otomotus.backend.entity.MessageEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface MessageRepository extends JpaRepository<MessageEntity, UUID> {
    List<MessageEntity> findAllByConversation_Id(UUID conversationId);
    List<MessageEntity> findByRecipientId(UUID recipientId);
}
