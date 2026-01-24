package org.otomotus.backend.auth.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.*;

/**
 * DTO dla żądania logowania użytkownika.
 * <p>
 * Zawiera dane niezbędne do uwierzytelnienia użytkownika (login i hasło).
 * </p>
 *
 * @author Otomotus Development Team
 * @version 1.0
 */
@Getter @Setter @NoArgsConstructor @AllArgsConstructor
public class LoginRequestDto {
    @NotBlank(message = "Username cannot be empty")
    private String username;

    @NotBlank(message = "Password cannot be empty")
    private String password;
}
