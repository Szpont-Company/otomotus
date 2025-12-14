package org.otomotus.backend.repository;

import org.otomotus.backend.entity.ConversationEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;
import java.util.UUID;

public interface ConversationRepository extends JpaRepository<ConversationEntity, UUID> {
    @Query("SELECT c FROM ConversationEntity c WHERE " +
            "(c.buyerId = :user1 AND c.sellerId = :user2) OR " +
            "(c.buyerId = :user2 AND c.sellerId = :user1)")
    Optional<ConversationEntity> findConversationBetweenUsers(
            @Param("user1") UUID user1,
            @Param("user2") UUID user2,
            @Param("product") UUID productId
    );
}