package org.otomotus.backend.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.otomotus.backend.auth.model.AuthUserDetails;
import org.otomotus.backend.dto.ConversationListDto;
import org.otomotus.backend.dto.MessageRequestDto;
import org.otomotus.backend.entity.MessageEntity;
import org.otomotus.backend.service.ChatService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

/**
 * Kontroler REST API dla zarządzania wiadomościami i rozmowami między użytkownikami.
 * <p>
 * Udostępnia operacje dla wysyłania, edytowania, usuwania wiadomości oraz zarządzania statusem odczytania.
 * Wszystkie operacje są zabezpieczone uwierzytelnianiem.
 * </p>
 *
 * @author Otomotus Development Team
 * @version 1.0
 */
@RestController
@RequestMapping("/api/messages")
@RequiredArgsConstructor
public class ChatController {
    private final ChatService chatSrv;

    /**
     * Wysyła nową wiadomość od zalogowanego użytkownika do innego użytkownika.
     *
     * @param user zalogowany użytkownik (wysyłający)
     * @param request dane wiadomości (odbiorca, produkt, treść)
     * @return wysłana wiadomość
     */
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

    /**
     * Pobiera listę wszystkich rozmów zalogowanego użytkownika.
     *
     * @param user zalogowany użytkownik
     * @return lista rozmów użytkownika
     */
    @GetMapping("/conversations/my")
    public ResponseEntity<List<ConversationListDto>> getConversations(@AuthenticationPrincipal AuthUserDetails user) {
        return ResponseEntity.ok(chatSrv.getUserConversations(user.getId()));
    }

    /**
     * Pobiera wszystkie wiadomości z konkretnej rozmowy.
     * Tylko uczestnicy rozmowy mają dostęp do jej wiadomości.
     *
     * @param conversationId identyfikator rozmowy
     * @param user zalogowany użytkownik
     * @return lista wiadomości z rozmowy
     */
    @GetMapping("/conversation/{conversationId}/messages")
    public ResponseEntity<List<MessageEntity>> getMessages(@PathVariable("conversationId") UUID conversationId,
                                                           @AuthenticationPrincipal AuthUserDetails user) {
        return ResponseEntity.ok(chatSrv.getConversationMessages(conversationId, user.getId()));
    }

    /**
     * Aktualizuje treść wiadomości.
     * Tylko autor wiadomości ma prawo ją edytować.
     *
     * @param msgId identyfikator wiadomości
     * @param request nowa treść wiadomości
     * @param user zalogowany użytkownik
     * @return zaktualizowana wiadomość
     */
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

    /**
     * Oznacza wiadomość jako przeczytaną.
     *
     * @param msgId identyfikator wiadomości
     * @param user zalogowany użytkownik
     * @return zaktualizowana wiadomość
     */
    @PatchMapping("/{id}/read")
    public ResponseEntity<MessageEntity> markRead(@PathVariable("id") UUID msgId,
                                                  @AuthenticationPrincipal AuthUserDetails user) {
        MessageEntity updatedMsg = chatSrv.markRead(msgId, user.getId());
        return ResponseEntity.ok(updatedMsg);
    }

    /**
     * Oznacza wiadomość jako nieprzeczytaną.
     *
     * @param msgId identyfikator wiadomości
     * @param user zalogowany użytkownik
     * @return zaktualizowana wiadomość
     */
    @PatchMapping("/{id}/unread")
    public ResponseEntity<MessageEntity> markUnread(@PathVariable("id") UUID msgId,
                                                    @AuthenticationPrincipal AuthUserDetails user) {
        MessageEntity updatedMsg = chatSrv.markUnread(msgId, user.getId());
        return ResponseEntity.ok(updatedMsg);
    }

    /**
     * Usuwa wiadomość.
     * Tylko autor wiadomości ma prawo ją usunąć.
     *
     * @param msgId identyfikator wiadomości
     * @param user zalogowany użytkownik
     * @return odpowiedź 204 No Content
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<MessageEntity> deleteMessage(@PathVariable("id") UUID msgId,
                                                       @AuthenticationPrincipal AuthUserDetails user) {
        chatSrv.deleteMessage(msgId, user.getId());
        return ResponseEntity.noContent().build();
    }
}
