package org.otomotus.backend.repository;

import org.otomotus.backend.entity.AuctionImageEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.UUID;

/**
 * Repozytorium dla encji AuctionImageEntity.
 * <p>
 * Dostarcza metody dostępu do zdjęć aukcji w bazie danych.
 * </p>
 *
 * @author Otomotus Development Team
 * @version 1.0
 */
public interface AuctionImageRepository extends JpaRepository<AuctionImageEntity, UUID> {
}