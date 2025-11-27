package org.otomotus.backend.dto.auction;

import lombok.Builder;
import lombok.Data;
import org.otomotus.backend.config.BodyType;
import org.otomotus.backend.config.DriveType;
import org.otomotus.backend.config.FuelType;
import org.otomotus.backend.config.GearboxType;

import java.util.UUID;

@Data
@Builder
public class CarResponseDto {
    private UUID id;

    private String brand;
    private String model;
    private String generation;

    private Integer year;
    private Integer mileage;
    private Integer enginePower;
    private Integer engineCapacity;
    private FuelType fuelType;
    private GearboxType gearboxType;
    private DriveType driveType;
    private BodyType bodyType;
    private Integer doors;
    private Integer seats;
}
