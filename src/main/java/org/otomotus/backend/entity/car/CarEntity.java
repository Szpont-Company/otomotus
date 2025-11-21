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
    private UUID id;

    @ManyToOne
    private GenerationEntity generation;

    @Column
    private Integer year;

    @Column
    private Integer mileage;

    @Column
    private Integer enginePower;

    @Column
    private Integer engineCapacity;

    @Column
    @Enumerated(EnumType.STRING)
    private FuelType fuelType;

    @Column
    @Enumerated(EnumType.STRING)
    private GearboxType gearboxType;

    @Column
    @Enumerated(EnumType.STRING)
    private DriveType driveType;

    @Column
    @Enumerated(EnumType.STRING)
    private BodyType bodyType;

    @Column
    private Integer doors;

    @Column
    private Integer seats;
}