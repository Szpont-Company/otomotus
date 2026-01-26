package org.otomotus.backend.dto;

import lombok.*;

import java.time.LocalDateTime;
import java.util.UUID;

/**
 * DTO dla odpowiedzi zawierającej szczegóły opinii.
 * <p>
 * Służy do wyświetlania listy opinii na profilu sprzedawcy.
 * </p>
 *
 * @author Otomotus Development Team
 * @version 1.0
 */
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class ReviewResponseDto {
    private UUID id;
    private int rating;
    private String comment;
    private String reviewerFirstName;
    private String auctionTitle;
    private LocalDateTime createdAt;
}