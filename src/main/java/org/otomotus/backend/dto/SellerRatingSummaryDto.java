package org.otomotus.backend.dto;

import lombok.*;

/**
 * DTO dla podsumowania rankingu sprzedawcy.
 * <p>
 * Zawiera średnią ocen oraz liczbę wystawionych opinii.
 * </p>
 *
 * @author Otomotus Development Team
 * @version 1.0
 */
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class SellerRatingSummaryDto {
    private double averageRating;
    private long totalReviews;
    private String sellerUsername;
}