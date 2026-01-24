package org.otomotus.backend.repository;

import org.otomotus.backend.config.AuctionStatus;
import org.otomotus.backend.entity.AuctionEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
import java.util.UUID;

/**
 * Repozytorium dla encji AuctionEntity.
 * <p>
 * Dostarcza metody dostępu do danych aukcji w bazie danych,
 * oprócz standardowych operacji CRUD z {@link JpaRepository}.
 * </p>
 *
 * @author Otomotus Development Team
 * @version 1.0
 */
public interface AuctionRepository extends JpaRepository<AuctionEntity, UUID> {
    Page<AuctionEntity> findAllByStatus(AuctionStatus status, Pageable pageable);

    int countBySellerId(UUID sellerId);
    List<AuctionEntity> findAllBySellerId(UUID sellerId);
}