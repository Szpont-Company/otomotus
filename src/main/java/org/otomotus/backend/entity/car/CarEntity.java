package org.otomotus.backend.entity.car;

import jakarta.persistence.*;
import lombok.*;
import org.otomotus.backend.config.BodyType;
import org.otomotus.backend.config.DriveType;
import org.otomotus.backend.config.FuelType;
import org.otomotus.backend.config.GearboxType;

import java.util.UUID;

@Entity
@Table(name = "cars")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class CarEntity {
    @Id
    @GeneratedValue(strategy =GenerationType.UUID)
    private UUID car_id;

    @ManyToOne
    private GenerationEntity generation;

    @Column(nullable = false)
    private Integer year;

    @Column(nullable = false)
    private Integer mileage;

    @Column(nullable = false)
    private Integer enginePower;

    @Column(nullable = false)
    private Integer engineCapacity;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private FuelType fuelType;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private GearboxType gearboxType;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private DriveType driveType;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private BodyType bodyType;

    @Column(nullable = false)
    private Integer doors;

    @Column(nullable = false)
    private Integer seats;
}