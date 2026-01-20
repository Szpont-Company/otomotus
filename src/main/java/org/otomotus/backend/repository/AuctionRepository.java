package org.otomotus.backend.repository;

import org.otomotus.backend.config.AuctionStatus;
import org.otomotus.backend.entity.AuctionEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.UUID;

public interface AuctionRepository extends JpaRepository<AuctionEntity, UUID> {
    Page<AuctionEntity> findAllByStatus(AuctionStatus status, Pageable pageable);

    List<AuctionEntity> findAllBySellerId(UUID sellerId);

    @Query("SELECT a FROM AuctionEntity a " +
            "WHERE LOWER(a.title) LIKE LOWER(CONCAT('%', :keyword, '%')) " +
            "OR LOWER(a.description) LIKE LOWER(CONCAT('%', :keyword, '%'))")
    Page<AuctionEntity> searchByKeyword(@Param("keyword") String keyword, Pageable pageable);
}