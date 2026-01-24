package org.otomotus.backend.entity;

import jakarta.persistence.*;
import lombok.*;
import org.otomotus.backend.config.UserRole;

import java.time.LocalDateTime;
import java.util.UUID;

/**
 * Encja reprezentująca użytkownika aplikacji.
 * <p>
 * Zawiera dane autentykacji, informacje profilu oraz status weryfikacji konta.
 * </p>
 *
 * @author Otomotus Development Team
 * @version 1.0
 */
@Entity
@Table(name = "users")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class UserEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(nullable = false, unique = true)
    private String username;

    @Column(nullable = false)
    private String password;

    @Column(nullable = false, unique = true)
    private String email;

    @Column(nullable = false)
    private String phoneNumber;

    @Column(nullable = false)
    private String firstName;

    @Column(nullable = false)
    private String lastName;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private UserRole role;

    @Column(nullable = false)
    private LocalDateTime createdAt;

    @Column(nullable = false)
    private LocalDateTime lastLoginDate;

    @Column(nullable = false)
    private boolean activated = false;

    @Column()
    private String verificationToken;

    @Column(nullable = false)
    private LocalDateTime tokenExpirationDate;
}
