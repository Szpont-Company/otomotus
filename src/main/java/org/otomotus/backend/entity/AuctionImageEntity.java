package org.otomotus.backend.entity;

import jakarta.persistence.*;
import lombok.*;

import java.util.UUID;

@Entity
@Table(name = "auction_images")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class AuctionImageEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "auction_id", nullable = false)
    private AuctionEntity auction;

    @Column(nullable = false)
    private String imageUrl;
}