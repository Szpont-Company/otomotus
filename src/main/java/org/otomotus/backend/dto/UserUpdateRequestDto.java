package org.otomotus.backend.dto;

import lombok.*;
import org.otomotus.backend.config.UserRole;
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class UserUpdateRequestDto {
    private String username;
    private String email;
    private String phoneNumber;
    private String lastName;
    private String firstName;
    private UserRole role;
}
