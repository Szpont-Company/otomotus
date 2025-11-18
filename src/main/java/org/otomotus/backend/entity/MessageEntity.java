package org.otomotus.backend.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;
import java.util.UUID;

@Getter @Setter
@Entity
@Table(name="messages", indexes = {@Index(name="idx_message_conversation", columnList = "conversationId")})
@NoArgsConstructor
public class MessageEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    //@Column(nullable = false)
    //private UUID conversationId;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name="conversation_id", nullable = false)
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

}
