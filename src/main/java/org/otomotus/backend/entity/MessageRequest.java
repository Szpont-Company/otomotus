package org.otomotus.backend.entity;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
public class MessageRequest {
    private UUID senderId;
    private UUID recipientId;
    @NotBlank(message = "Content cannot be empty!")
    @Size(max=300, message = "Message cannot be longer than 300 characters!")
    private String content;

    public MessageRequest() {}

    public MessageRequest(UUID senderId, UUID recipientId, String content) {
        this.senderId = senderId;
        this.recipientId = recipientId;
        this.content = content;
    }

}
