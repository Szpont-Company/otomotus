package org.otomotus.backend.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class MessageRequestDto {
    private UUID senderId;
    private UUID recipientId;
    private UUID productId;
    @NotBlank(message = "Content cannot be empty!")
    @Size(max=300, message = "Message cannot be longer than 300 characters!")
    private String content;
}
