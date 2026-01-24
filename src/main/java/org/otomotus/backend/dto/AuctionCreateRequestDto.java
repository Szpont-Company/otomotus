package org.otomotus.backend.dto;

import jakarta.validation.constraints.*;
import lombok.*;
import org.otomotus.backend.config.FuelType;
import org.otomotus.backend.config.TransmissionType;

import java.math.BigDecimal;

/**
 * DTO dla żądania utworzenia nowej aukcji.
 * <p>
 * Zawiera dane samochodu oraz warunki sprzedaży. Wszystkie pola są walidowane
 * na podstawie adnotacji @Valid.
 * </p>
 *
 * @author Otomotus Development Team
 * @version 1.0
 */
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class AuctionCreateRequestDto {
    @NotBlank private String vin;
    @NotBlank private String brand;
    @NotBlank private String model;
    private String generation;
    @Min(1900) private int productionYear;
    @Min(0) private int mileage;
    @Min(0) private int engineCapacity;
    @Min(0) private int enginePower;
    @NotNull private FuelType fuelType;
    @NotNull private TransmissionType transmission;
    private String color;

    @NotBlank private String title;
    private String description;
    @NotNull @Min(0) private BigDecimal price;
    @NotBlank private String location;
    private boolean negotiable;
    @NotBlank private String phoneNumber;
}