package org.otomotus.backend.dto;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.util.UUID;

/**
 * DTO dla żądania dodania nowej opinii.
 * <p>
 * Zawiera identyfikator aukcji, ocenę punktową oraz treść komentarza.
 * </p>
 *
 * @author Otomotus Development Team
 * @version 1.0
 */
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class ReviewCreateRequestDto {

    @NotNull(message = "Auction ID is required")
    private UUID auctionId;

    @Min(value = 1, message = "Rating must be at least 1")
    @Max(value = 5, message = "Rating must be at most 5")
    private int rating;

    @NotBlank(message = "Comment cannot be empty")
    private String comment;
}