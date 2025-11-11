package org.otomotus.backend.auth.dto;

import lombok.*;

@Getter @Setter @NoArgsConstructor @AllArgsConstructor
public class LoginResponseDto {
    private String token;
}
