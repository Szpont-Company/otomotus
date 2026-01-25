package org.otomotus.backend.controller.auth;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.otomotus.backend.auth.config.VerificationStatus;
import org.otomotus.backend.auth.controller.AuthController;
import org.otomotus.backend.auth.dto.LoginRequestDto;
import org.otomotus.backend.auth.service.JwtService;
import org.otomotus.backend.service.UserService;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
class AuthControllerTest {

    private MockMvc mockMvc;

    @Mock private UserService userService;
    @Mock private JwtService jwtService;
    @Mock private AuthenticationManager authenticationManager;
    @Mock private UserDetailsService userDetailsService;

    @InjectMocks
    private AuthController authController;

    private final ObjectMapper objectMapper = new ObjectMapper();

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(authController).build();
    }

    @Test
    @DisplayName("login: Should return token when credentials are valid")
    void login_ShouldReturnToken() throws Exception {
        // Given
        LoginRequestDto loginRequest = new LoginRequestDto("user", "password");
        String fakeToken = "jwt-token-xyz";

        when(authenticationManager.authenticate(any())).thenReturn(null);
        when(userDetailsService.loadUserByUsername("user")).thenReturn(null); // Mock UserDetails
        when(jwtService.generateToken(any())).thenReturn(fakeToken);

        // When & Then
        mockMvc.perform(post("/api/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(loginRequest)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.token").value(fakeToken));
    }

    @Test
    @DisplayName("verifyUser: Should return 200 OK when token is verified")
    void verifyUser_ShouldReturnOk_WhenVerified() throws Exception {
        // Given
        String token = "valid-token";
        when(userService.verifyToken(token)).thenReturn(VerificationStatus.VERIFIED);

        // When & Then
        mockMvc.perform(get("/api/auth/verify")
                        .param("token", token))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").value("User successfully verified"));
    }

    @Test
    @DisplayName("verifyUser: Should return 400 Bad Request when token is invalid")
    void verifyUser_ShouldReturnBadRequest_WhenInvalid() throws Exception {
        // Given
        String token = "bad-token";
        when(userService.verifyToken(token)).thenReturn(VerificationStatus.INVALID);

        // When & Then
        mockMvc.perform(get("/api/auth/verify")
                        .param("token", token))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$").value("Invalid token"));
    }
}