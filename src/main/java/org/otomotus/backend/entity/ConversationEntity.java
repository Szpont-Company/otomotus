package org.otomotus.backend.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@Setter
@Entity
public class ConversationEntity {
    @Id
    @GeneratedValue
    private UUID id;

    private UUID buyerId;
    private UUID sellerId;
    private UUID productId;
    private LocalDateTime createdAt;

}
