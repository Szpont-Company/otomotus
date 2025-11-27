package org.otomotus.backend.entity.car;

import jakarta.persistence.*;
import lombok.*;

import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "brands")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class BrandEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID brand_id;

    @Column(nullable = false)
    private String name;

    @OneToMany(mappedBy = "models")
    private List<ModelEntity> models;
}
