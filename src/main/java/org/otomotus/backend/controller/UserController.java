package org.otomotus.backend.controller;

import org.otomotus.backend.dto.UserResponseDto;
import org.otomotus.backend.dto.UserUpdateRequestDto;
import org.otomotus.backend.service.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

/**
 * Kontroler REST API dla zarządzania użytkownikami.
 * <p>
 * Udostępnia operacje CRUD dla użytkowników. Dostęp do wszystkich operacji
 * ograniczony jest do ról administratora (@PreAuthorize("hasAuthority('ADMIN')")).
 * </p>
 *
 * @author Otomotus Development Team
 * @version 1.0
 */
@RestController
@RequestMapping("/api/users")
public class UserController {

    private final UserService userService;

    /**
     * Konstruktor kontrolera z wstrzykniętym serwisem użytkowników.
     *
     * @param userService serwis dla operacji na użytkownikach
     */
    public UserController(UserService userService) {
        this.userService = userService;
    }

    /**
     * Pobiera listę wszystkich użytkowników.
     * Dostępne tylko dla administratorów.
     *
     * @return lista wszystkich użytkowników
     */
    @GetMapping
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<List<UserResponseDto>> getAll() {
        return ResponseEntity.ok(userService.getAll());
    }

    /**
     * Pobiera użytkownika po ID.
     * Dostępne tylko dla administratorów.
     *
     * @param userId identyfikator użytkownika
     * @return dane użytkownika
     */
    @GetMapping("/{userId}")
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<UserResponseDto> getById(@PathVariable UUID userId) {
        return ResponseEntity.ok(userService.getById(userId));
    }

    /**
     * Aktualizuje dane użytkownika (PATCH).
     * Dostępne tylko dla administratorów.
     *
     * @param userId identyfikator użytkownika
     * @param userUpdateRequest dane do aktualizacji
     * @return zaktualizowane dane użytkownika
     */
    @PatchMapping("/{userId}")
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<UserResponseDto> patch(@PathVariable UUID userId, @RequestBody UserUpdateRequestDto userUpdateRequest) {
        return ResponseEntity.ok(userService.patch(userId, userUpdateRequest));
    }

    /**
     * Usuwa użytkownika.
     * Dostępne tylko dla administratorów.
     *
     * @param userId identyfikator użytkownika do usunięcia
     * @return odpowiedź 204 No Content
     */
    @DeleteMapping("/{userId}")
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<UserResponseDto> delete(@PathVariable UUID userId) {
        userService.delete(userId);
        return ResponseEntity.noContent().build();
    }
}
