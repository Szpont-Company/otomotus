package org.otomotus.backend.repository.car;

import org.otomotus.backend.entity.car.GenerationEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface GenerationRepository extends JpaRepository<GenerationEntity, UUID> {

}
