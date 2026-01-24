package org.otomotus.backend.dto;

import jakarta.validation.constraints.Min;
import lombok.*;
import org.otomotus.backend.config.FuelType;
import org.otomotus.backend.config.TransmissionType;

import java.math.BigDecimal;

/**
 * DTO dla żądania aktualizacji aukcji.
 * <p>
 * Zawiera pola, które mogą być zaktualizowane w istniejącej aukcji.
 * Wszystkie pola są opcjonalne (mogą być null).
 * </p>
 *
 * @author Otomotus Development Team
 * @version 1.0
 */
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class AuctionUpdateRequestDto {
    private String brand;
    private String model;
    private String generation;
    @Min(1900)
    private Integer productionYear;
    @Min(0)
    private Integer mileage;
    @Min(0)
    private Integer engineCapacity;
    @Min(0)
    private Integer enginePower;
    private FuelType fuelType;
    private TransmissionType transmission;
    private String color;

    private String title;
    private String description;
    @Min(0)
    private BigDecimal price;
    private String location;
    private Boolean negotiable;
    private String phoneNumber;
}