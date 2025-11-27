package org.otomotus.backend.dto.auction;

import lombok.Builder;
import lombok.Data;
import org.otomotus.backend.config.AuctionStatus;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

@Data
@Builder
public class AuctionResponseDto {
    private UUID id;
    private String title;
    private String description;
    private BigDecimal price;
    private boolean negotiable;
    private String phoneNumber;
    private LocalDateTime createdAt;
    private LocalDateTime expiresAt;
    private AuctionStatus status;
    private Integer viewCount;
    private boolean promoted;

    private CarResponseDto car;
    private SellerDto seller;
}
