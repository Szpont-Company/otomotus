package org.otomotus.backend.auth.dto;

import lombok.*;

/**
 * DTO dla odpowiedzi logowania użytkownika.
 * <p>
 * Zawiera token JWT zwrócony po pomyślnym uwierzytelnieniu.
 * </p>
 *
 * @author Otomotus Development Team
 * @version 1.0
 */
@Getter @Setter @NoArgsConstructor @AllArgsConstructor
public class LoginResponseDto {
    private String token;
}
