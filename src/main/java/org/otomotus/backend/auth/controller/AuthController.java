package org.otomotus.backend.auth.controller;

import jakarta.validation.Valid;
import org.otomotus.backend.auth.dto.RegisterRequest;
import org.otomotus.backend.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    @PostMapping("/register")
    public ResponseEntity<?> registerUser(@Valid @RequestBody RegisterRequest registerRequest) {
        //AuthService register
        return ResponseEntity.status(HttpStatus.CREATED).body("User successfully registered");
    }
}
