package org.otomotus.backend.repository;

import org.otomotus.backend.entity.MessageEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

/**
 * Repozytorium dla encji MessageEntity.
 * <p>
 * Dostarcza metody dostępu do wiadomości w bazie danych.
 * </p>
 *
 * @author Otomotus Development Team
 * @version 1.0
 */
public interface MessageRepository extends JpaRepository<MessageEntity, UUID> {
    List<MessageEntity> findAllByConversation_Id(UUID conversationId);
    List<MessageEntity> findByRecipientId(UUID recipientId);
}
