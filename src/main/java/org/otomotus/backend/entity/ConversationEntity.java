package org.otomotus.backend.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;
import java.util.UUID;

/**
 * Encja reprezentująca rozmowę między kupującym a sprzedawcą.
 * <p>
 * Rozmowa jest unikalna dla kombinacji kupującego, sprzedawcy i produktu.
 * </p>
 *
 * @author Otomotus Development Team
 * @version 1.0
 */
@Getter
@Setter
@Entity
@NoArgsConstructor
@Table(name = "conversations", uniqueConstraints = {
        @UniqueConstraint(columnNames = {"buyerId", "sellerId", "productId"})
})
public class ConversationEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(nullable = false)
    private UUID buyerId;
    @Column(nullable = false)
    private UUID sellerId;
    @Column(nullable = false)
    private UUID productId;

    @CreationTimestamp
    @Column(nullable = false, updatable = false)
    private LocalDateTime createdAt;
}
