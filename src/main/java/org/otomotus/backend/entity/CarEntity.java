package org.otomotus.backend.entity;

import jakarta.persistence.*;
import lombok.*;
import org.otomotus.backend.config.FuelType;
import org.otomotus.backend.config.TransmissionType;

import java.util.UUID;

/**
 * Encja reprezentująca samochód.
 * <p>
 * Zawiera szczegóły techniczne samochodu takie jak marka, model,
 * rok produkcji, parametry silnika itp.
 * </p>
 *
 * @author Otomotus Development Team
 * @version 1.0
 */
@Entity
@Table(name = "cars")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class CarEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(nullable = false)
    private String vin;

    @Column(nullable = false)
    private String brand;

    @Column(nullable = false)
    private String model;

    private String generation;

    @Column(nullable = false)
    private int productionYear;

    @Column(nullable = false)
    private int mileage;

    @Column(nullable = false)
    private int engineCapacity;

    @Column(nullable = false)
    private int enginePower;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private FuelType fuelType;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private TransmissionType transmission;

    private String color;
}