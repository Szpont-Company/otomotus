package org.otomotus.backend.repository;

import org.otomotus.backend.entity.AuctionImageEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.UUID;

public interface AuctionImageRepository extends JpaRepository<AuctionImageEntity, UUID> {
}