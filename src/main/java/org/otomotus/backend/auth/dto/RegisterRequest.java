package org.otomotus.backend.auth.dto;

import jakarta.validation.constraints.*;
import lombok.*;

import org.otomotus.backend.auth.config.validation.*;

/**
 * DTO dla żądania rejestracji nowego użytkownika.
 * <p>
 * Zawiera dane wymagane do utworzenia nowego konta wraz z walidacją.
 * Email i login muszą być unikalne, hasło musi spełniać wymagania bezpieczeństwa.
 * </p>
 *
 * @author Otomotus Development Team
 * @version 1.0
 */
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class RegisterRequest {

    @NotBlank(message = "Username cannot be empty")
    @UniqueUsername()
    private String username;

    @NotBlank(message = "Password cannot be empty")
    @ValidPassword(message = "The password does not meet security requirements")
    private String password;

    @NotBlank(message = "Email cannot be empty")
    @Email(message = "Invalid email format")
    @UniqueEmail()
    private String email;

    @NotBlank(message = "Name cannot be empty")
    @Size(min = 3, max = 50, message = "Name must be between 2 and 50 characters long")
    @Pattern(regexp = "^[\\p{L}\\p{M}]+([ '\\-][\\p{L}\\p{M}]+)*$", message = "Invalid format")
    private String firstName;

    @NotBlank(message = "Last name cannot be empty")
    @Size(min = 3, max = 50, message = "Last name must be between 2 and 50 characters long")
    @Pattern(regexp = "^[\\p{L}\\p{M}]+([ '\\-][\\p{L}\\p{M}]+)*$", message = "Invalid format")
    private String lastName;

    @NotBlank(message = "Phone number cannot be empty")
    @Pattern(regexp = "^\\d{3}-\\d{3}-\\d{3}$", message = "Invalid phone number format")
    private String phoneNumber;
}
