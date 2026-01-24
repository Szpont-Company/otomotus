package org.otomotus.backend.repository;

import org.otomotus.backend.dto.ConversationListDto;
import org.otomotus.backend.entity.ConversationEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

/**
 * Repozytorium dla encji ConversationEntity.
 * <p>
 * Dostarcza metody dostępu do rozmów w bazie danych wraz z zaawansowanymi
 * zapytaniami JPQL dla pobierania informacji o rozmowach.
 * </p>
 *
 * @author Otomotus Development Team
 * @version 1.0
 */
public interface ConversationRepository extends JpaRepository<ConversationEntity, UUID> {
    /**
     * Wyszukuje rozmowę między dwoma użytkownikami dotyczącą konkretnego produktu.
     * <p>
     * Rozmowa jest dwukierunkowa - zwraca wynik niezależnie od kolejności użytkowników.
     * </p>
     *
     * @param user1 pierwszy użytkownik
     * @param user2 drugi użytkownik
     * @param productId identyfikator produktu
     * @return Optional zawierający rozmowę, jeśli istnieje
     */
    @Query("""
    SELECT c FROM ConversationEntity c
    WHERE (
        (c.buyerId = :user1 AND c.sellerId = :user2)
        OR
        (c.buyerId = :user2 AND c.sellerId = :user1)
    )
    AND c.productId = :product
    """)
    Optional<ConversationEntity> findConversationBetweenUsers(
            @Param("user1") UUID user1,
            @Param("user2") UUID user2,
            @Param("product") UUID productId
    );

    /**
     * Sprawdza, czy użytkownik ma dostęp do danej rozmowy.
     *
     * @param conversationId identyfikator rozmowy
     * @param userId identyfikator użytkownika
     * @return true jeśli użytkownik jest uczestnikiem rozmowy
     */
    @Query("""
        SELECT CASE WHEN COUNT(c) > 0 THEN true ELSE false END
        FROM ConversationEntity c
        WHERE c.id = :conversationId AND (c.buyerId = :userId OR c.sellerId = :userId)
    """)
    boolean existsByIdAndUser(@Param("conversationId") UUID conversationId, @Param("userId") UUID userId);

    /**
     * Pobiera listę wszystkich rozmów użytkownika z ostatnią wiadomością i liczbą nieprzeczytanych.
     * <p>
     * Zwraca rozmowy posortowane chronologicznie z informacją o ostatniej wiadomości.
     * </p>
     *
     * @param userId identyfikator użytkownika
     * @return lista rozmów użytkownika
     */
    @Query("""
    SELECT new org.otomotus.backend.dto.ConversationListDto(
        c.id,
        c.productId,
        CASE WHEN c.buyerId = :userId THEN c.sellerId ELSE c.buyerId END,
        CONCAT(u.firstName, ' ', u.lastName),
        lm.msgContent,
        lm.timestamp,
        COUNT(CASE WHEN m.isRead = false AND m.senderId <> :userId THEN 1 END)
    )
    FROM ConversationEntity c
    JOIN UserEntity u ON u.id = CASE WHEN c.buyerId = :userId THEN c.sellerId ELSE c.buyerId END
    LEFT JOIN MessageEntity lm ON lm.id = (
        SELECT m2.id 
        FROM MessageEntity m2 
        WHERE m2.conversation.id = c.id 
        ORDER BY m2.timestamp DESC
        LIMIT 1
    )
    LEFT JOIN MessageEntity m ON m.conversation.id = c.id
    WHERE c.buyerId = :userId OR c.sellerId = :userId
    GROUP BY c.id, c.productId, u.id, u.firstName, u.lastName, lm.msgContent, lm.timestamp
    """)
    List<ConversationListDto> findConversationsForUser(@Param("userId") UUID userId);

}