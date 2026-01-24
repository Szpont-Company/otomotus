package org.otomotus.backend.repository;

import org.otomotus.backend.entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

/**
 * Repozytorium dla encji UserEntity.
 * <p>
 * Dostarcza metody dostępu do danych użytkowników w bazie danych,
 * z dodatkowymi metodami do wyszukiwania po nazwie, emailu i tokenach weryfikacyjnych.
 * </p>
 *
 * @author Otomotus Development Team
 * @version 1.0
 */
public interface UserRepository extends JpaRepository<UserEntity, UUID> {
    Optional<UserEntity> findByUsername(String username);
    Optional<UserEntity> findByEmail(String email);
    Optional<UserEntity> findByVerificationToken(String verificationToken);

    boolean existsByEmail(String email);
    boolean existsByUsername(String username);
}
