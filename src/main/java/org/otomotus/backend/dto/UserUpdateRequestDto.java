package org.otomotus.backend.dto;

import lombok.*;
import org.otomotus.backend.config.UserRole;

/**
 * DTO dla żądania aktualizacji danych użytkownika.
 * <p>
 * Zawiera pola, które mogą być zmienione w profilu użytkownika.
 * Wszystkie pola są opcjonalne (mogą być null).
 * </p>
 *
 * @author Otomotus Development Team
 * @version 1.0
 */
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class UserUpdateRequestDto {
    private String username;
    private String email;
    private String phoneNumber;
    private String lastName;
    private String firstName;
    private UserRole role;
}
