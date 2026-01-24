package org.otomotus.backend.dto;

import lombok.*;

/**
 * DTO dla żądania utworzenia nowego użytkownika.
 * <p>
 * Zawiera dane potrzebne do bezpośredniego utworzenia użytkownika przez administratora.
 * </p>
 *
 * @author Otomotus Development Team
 * @version 1.0
 */
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class UserCreateRequestDto {
    private String username;
    private String password;
    private String email;
    private String phoneNumber;
    private String firstName;
    private String lastName;
}
