package org.otomotus.backend.dto.auction;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Builder
public class SellerDto {
    private String username;
    private LocalDateTime createdAt;
}
