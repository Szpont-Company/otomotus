package org.otomotus.backend.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.otomotus.backend.dto.MessageRequestDto;
import org.otomotus.backend.entity.MessageEntity;
import org.otomotus.backend.service.ChatService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/messages")
@RequiredArgsConstructor
public class ChatController {
    private final ChatService chatSrv;

    @PostMapping("/send")
    public ResponseEntity<MessageEntity> sendMessage(@RequestBody MessageRequestDto request) {
        MessageEntity msg = chatSrv.sendMessage(
                request.getSenderId(),
                request.getRecipientId(),
                request.getProductId(),
                request.getContent()
        );
        return ResponseEntity.ok(msg);
    }

    @GetMapping("/conversation/{conversationId}/messages")
    public ResponseEntity<List<MessageEntity>> getMessages(@PathVariable("conversationId") UUID conversationId) {
        return ResponseEntity.ok(chatSrv.getConversationMessages(conversationId));
    }

    @PatchMapping("/{id}")
    public ResponseEntity<MessageEntity> updateMessage(@PathVariable("id") UUID msgId,
                                                       @Valid @RequestBody MessageRequestDto request) {
        MessageEntity newMsg = chatSrv.editMessage(
                msgId,
                request.getContent()
        );
        return ResponseEntity.ok(newMsg);
    }

    @PatchMapping("/{id}/read")
    public ResponseEntity<MessageEntity> markRead(@PathVariable("id") UUID msgId) {
        MessageEntity updatedMsg = chatSrv.markRead(msgId);
        return ResponseEntity.ok(updatedMsg);
    }

    @PatchMapping("/{id}/unread")
    public ResponseEntity<MessageEntity> markUnread(@PathVariable("id") UUID msgId) {
        MessageEntity updatedMsg = chatSrv.markUnread(msgId);
        return ResponseEntity.ok(updatedMsg);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<MessageEntity> deleteMessage(@PathVariable("id") UUID msgId) {
        chatSrv.deleteMessage(msgId);
        return ResponseEntity.noContent().build();
    }
}
