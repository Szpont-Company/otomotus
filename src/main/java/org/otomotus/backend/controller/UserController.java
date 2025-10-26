package org.otomotus.backend.controller;

import org.otomotus.backend.dto.UserCreateRequestDto;
import org.otomotus.backend.dto.UserResponseDto;
import org.otomotus.backend.dto.UserUpdateRequestDto;
import org.otomotus.backend.service.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/users")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    //    Tworzenie usera przez admina
    @PostMapping
    public ResponseEntity<UserResponseDto> create(@RequestBody UserCreateRequestDto userCreateRequestDto) {
        return ResponseEntity.ok(userService.create(userCreateRequestDto));
    }

    @GetMapping
    public ResponseEntity<List<UserResponseDto>> getAll() {
        return ResponseEntity.ok(userService.getAll());
    }

    @GetMapping("/{userId}")
    public ResponseEntity<UserResponseDto> getById(@PathVariable UUID userId) {
        return ResponseEntity.ok(userService.getById(userId));
    }

    @PatchMapping("/{userId}")
    public ResponseEntity<UserResponseDto> patch(@PathVariable UUID userId, @RequestBody UserUpdateRequestDto userUpdateRequest) {
        return ResponseEntity.ok(userService.patch(userId, userUpdateRequest));
    }

    @DeleteMapping("/{userId}")
    public ResponseEntity<UserResponseDto> delete(@PathVariable UUID userId) {
        userService.delete(userId);
        return ResponseEntity.noContent().build();
    }
}
