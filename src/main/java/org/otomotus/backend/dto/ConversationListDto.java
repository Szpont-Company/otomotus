package org.otomotus.backend.dto;

import java.time.LocalDateTime;
import java.util.UUID;

/**
 * Record DTO dla listy rozmów użytkownika.
 * <p>
 * Zawiera podsumowanie rozmowy wraz z ostatnią wiadomością i liczbą nieprzeczytanych.
 * </p>
 *
 * @param id identyfikator rozmowy
 * @param productId identyfikator produktu
 * @param otherUserId identyfikator drugiego uczestnika rozmowy
 * @param otherUserName nazwa drugiego uczestnika
 * @param lastMessage treść ostatniej wiadomości
 * @param lastMessageAt czas ostatniej wiadomości
 * @param unreadCount liczba nieprzeczytanych wiadomości
 *
 * @author Otomotus Development Team
 * @version 1.0
 */
public record ConversationListDto(
        UUID id,
        UUID productId,
        UUID otherUserId,
        String otherUserName,
        String lastMessage,
        LocalDateTime lastMessageAt,
        Long unreadCount
) {}

