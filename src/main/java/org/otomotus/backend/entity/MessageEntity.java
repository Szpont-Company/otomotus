package org.otomotus.backend.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;
import java.util.UUID;

/**
 * Encja reprezentująca wiadomość w rozmowie.
 * <p>
 * Zawiera treść wiadomości, informacje o nadawcy i odbiorcy,
 * znaczniki czasowe oraz status przeczytania.
 * </p>
 *
 * @author Otomotus Development Team
 * @version 1.0
 */
@Getter @Setter
@Entity
@Table(name="messages", indexes = {@Index(name="idx_message_conversation", columnList = "conversationId")})
@NoArgsConstructor
public class MessageEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name="conversation_id", nullable = false)
    @JsonIgnore
    private ConversationEntity conversation;

    @Column(nullable = false)
    private UUID senderId;
    @Column(nullable = false)
    private UUID recipientId;

    @Column(name="message_content", nullable = false, columnDefinition = "TEXT")
    private String msgContent;

    @CreationTimestamp
    @Column(name="message_timestamp", nullable = false, updatable = false)
    private LocalDateTime timestamp;

    @UpdateTimestamp
    @Column(name="message_edited_timestamp")
    private LocalDateTime editedTimestamp;

    @Column(name="is_message_read", nullable = false)
    private boolean isRead = false;

    @Column(name="message_read_timestamp")
    private LocalDateTime readTimestamp;

    @JsonProperty("conversationId")
    public UUID getConversationId() {
        return conversation != null ? conversation.getId() : null;
    }
}
