package org.otomotus.backend.entity.car;

import jakarta.persistence.*;
import lombok.*;

import java.util.UUID;

@Entity
@Table(name = "generations")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class GenerationEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID generation_id;

    @Column(nullable = false)
    private String name;

    @ManyToOne
    private ModelEntity model;
}
