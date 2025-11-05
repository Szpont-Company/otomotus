package org.otomotus.backend.auth.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.otomotus.backend.auth.config.VerificationStatus;
import org.otomotus.backend.auth.dto.RegisterRequest;
import org.otomotus.backend.service.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor()
public class AuthController {

    private final UserService userService;

    @PostMapping("/register")
    public ResponseEntity<?> registerUser(@Valid @RequestBody RegisterRequest registerRequest) {
        //AuthService register
        return ResponseEntity.status(HttpStatus.CREATED).body("User successfully registered");
    }

    @GetMapping("/verify")
    public ResponseEntity<String> verifyUser(@RequestParam String token) {
        VerificationStatus result = userService.verifyToken(token);

        switch (result) {
            case VerificationStatus.VERIFIED:
                return ResponseEntity.ok("User successfully verified");
            case VerificationStatus.EXPIRED:
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Token has expired");
            case VerificationStatus.INVALID:
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Invalid token");
            default:
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Something went wrong");
        }
    }
}
