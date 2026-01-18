package org.otomotus.backend.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.otomotus.backend.auth.model.AuthUserDetails;
import org.otomotus.backend.dto.MessageRequestDto;
import org.otomotus.backend.entity.MessageEntity;
import org.otomotus.backend.service.ChatService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/messages")
@RequiredArgsConstructor
public class ChatController {
    private final ChatService chatSrv;

    @PostMapping("/send")
    public ResponseEntity<MessageEntity> sendMessage(@AuthenticationPrincipal AuthUserDetails user,
                                                     @Valid @RequestBody MessageRequestDto request) {
        MessageEntity msg = chatSrv.sendMessage(
                user.getId(),
                request.getRecipientId(),
                request.getProductId(),
                request.getContent()
        );
        return ResponseEntity.ok(msg);
    }

    @GetMapping("/conversation/{conversationId}/messages")
    public ResponseEntity<List<MessageEntity>> getMessages(@PathVariable("conversationId") UUID conversationId,
                                                           @AuthenticationPrincipal AuthUserDetails user) {
        return ResponseEntity.ok(chatSrv.getConversationMessages(conversationId, user.getId()));
    }

    @PatchMapping("/{id}")
    public ResponseEntity<MessageEntity> updateMessage(@PathVariable("id") UUID msgId,
                                                       @Valid @RequestBody MessageRequestDto request,
                                                       @AuthenticationPrincipal AuthUserDetails user) {
        MessageEntity newMsg = chatSrv.editMessage(
                msgId,
                request.getContent(),
                user.getId()
        );
        return ResponseEntity.ok(newMsg);
    }

    @PatchMapping("/{id}/read")
    public ResponseEntity<MessageEntity> markRead(@PathVariable("id") UUID msgId,
                                                  @AuthenticationPrincipal AuthUserDetails user) {
        MessageEntity updatedMsg = chatSrv.markRead(msgId, user.getId());
        return ResponseEntity.ok(updatedMsg);
    }

    @PatchMapping("/{id}/unread")
    public ResponseEntity<MessageEntity> markUnread(@PathVariable("id") UUID msgId,
                                                    @AuthenticationPrincipal AuthUserDetails user) {
        MessageEntity updatedMsg = chatSrv.markUnread(msgId, user.getId());
        return ResponseEntity.ok(updatedMsg);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<MessageEntity> deleteMessage(@PathVariable("id") UUID msgId,
                                                       @AuthenticationPrincipal AuthUserDetails user) {
        chatSrv.deleteMessage(msgId, user.getId());
        return ResponseEntity.noContent().build();
    }
}
