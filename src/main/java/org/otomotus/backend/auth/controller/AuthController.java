package org.otomotus.backend.auth.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.otomotus.backend.auth.config.VerificationStatus;
import org.otomotus.backend.auth.dto.LoginRequestDto;
import org.otomotus.backend.auth.dto.LoginResponseDto;
import org.otomotus.backend.auth.dto.RegisterRequest;
import org.otomotus.backend.auth.service.JwtService;
import org.otomotus.backend.repository.UserRepository;
import org.otomotus.backend.service.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor()
public class AuthController {

    private final UserService userService;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;
    private final UserDetailsService userDetailsService;
    private final UserRepository userRepository;

    @PostMapping("/register")
    public ResponseEntity<String> registerUser(@Valid @RequestBody RegisterRequest registerRequest) {
        userService.registerUser(registerRequest);
        return ResponseEntity.status(HttpStatus.CREATED).body("User successfully registered");
    }

    @PostMapping("/login")
    public ResponseEntity<LoginResponseDto> login(@Valid @RequestBody LoginRequestDto loginRequestDto) {
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            loginRequestDto.getUsername(),
                            loginRequestDto.getPassword()
                    )
            );

            final UserDetails userDetails = userDetailsService.loadUserByUsername(loginRequestDto.getUsername());
            final String jwt = jwtService.generateToken(userDetails);

            return ResponseEntity.ok(new LoginResponseDto(jwt));
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
