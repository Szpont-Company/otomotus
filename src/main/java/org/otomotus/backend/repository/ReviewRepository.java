package org.otomotus.backend.repository;

import org.otomotus.backend.entity.ReviewEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.UUID;

/**
 * Repozytorium dla encji ReviewEntity.
 * <p>
 * Dostarcza metody dostępu do opinii, w tym obliczanie średniej ocen sprzedawcy.
 * </p>
 *
 * @author Otomotus Development Team
 * @version 1.0
 */
public interface ReviewRepository extends JpaRepository<ReviewEntity, UUID> {

    Page<ReviewEntity> findAllBySellerId(UUID sellerId, Pageable pageable);

    boolean existsByAuctionIdAndReviewerId(UUID auctionId, UUID reviewerId);

    @Query("SELECT AVG(r.rating) FROM ReviewEntity r WHERE r.seller.id = :sellerId")
    Double getAverageRatingForSeller(@Param("sellerId") UUID sellerId);

    long countBySellerId(UUID sellerId);
}