package org.otomotus.backend.dto;

import lombok.*;
import org.otomotus.backend.config.UserRole;

import java.time.LocalDateTime;
import java.util.UUID;

/**
 * DTO dla odpowiedzi zawierającej dane użytkownika.
 * <p>
 * Zawiera publiczne informacje o użytkowniku bez wrażliwych danych takich jak hasło.
 * </p>
 *
 * @author Otomotus Development Team
 * @version 1.0
 */
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class UserResponseDto {
    private UUID id;
    private String username;
    private String email;
    private String phoneNumber;
    private String firstName;
    private String lastName;
    private UserRole role;
    private LocalDateTime createdAt;
    private LocalDateTime lastLoginDate;
}
