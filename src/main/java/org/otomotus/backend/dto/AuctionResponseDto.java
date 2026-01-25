package org.otomotus.backend.dto;

import lombok.*;
import org.otomotus.backend.config.AuctionStatus;
import org.otomotus.backend.config.FuelType;
import org.otomotus.backend.config.TransmissionType;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

/**
 * DTO dla odpowiedzi zawierającej dane aukcji.
 * <p>
 * Zawiera informacje o aukcji, samochodzie, cenie, sprzedawcy i zdjęciach.
 * </p>
 *
 * @author Otomotus Development Team
 * @version 1.0
 */
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class AuctionResponseDto {
    private UUID id;
    private String title;
    private BigDecimal price;
    private String location;
    private List<String> imageUrls;
    private AuctionStatus status;
    private LocalDateTime createdAt;
    private String description;

    private String brand;
    private String model;
    private int productionYear;
    private int mileage;
    private String vin;
    private FuelType fuelType;
    private TransmissionType transmissionType;

    private UUID sellerId;

    private Integer enginePower;
    private Integer engineCapacity;

    private String sellerFirstName;
    private String sellerLastName;

    private int sellerSinceYear;
    private int sellerListingCount;

    private boolean isFavorite;
}