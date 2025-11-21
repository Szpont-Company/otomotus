package org.otomotus.backend.entity.car;

import jakarta.persistence.*;
import lombok.*;

import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "models")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class ModelEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column
    private String name;

    @ManyToOne
    private BrandEntity brand;

    @OneToMany(mappedBy = "models")
    private List<GenerationEntity> generations;
}
