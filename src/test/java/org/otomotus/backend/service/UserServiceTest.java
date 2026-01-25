package org.otomotus.backend.service;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.otomotus.backend.auth.dto.RegisterRequest;
import org.otomotus.backend.auth.service.VerificationTokenService;
import org.otomotus.backend.config.UserRole;
import org.otomotus.backend.dto.UserResponseDto;
import org.otomotus.backend.entity.UserEntity;
import org.otomotus.backend.exception.UserAlreadyExistsException;
import org.otomotus.backend.mapper.UserMapper;
import org.otomotus.backend.repository.UserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {

    @Mock
    private UserRepository userRepository;
    @Mock
    private UserMapper userMapper;
    @Mock
    private VerificationTokenService verificationTokenService;
    @Mock
    private EmailService emailService;
    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private UserService userService;

    @Test
    @DisplayName("registerUser: It should throw an exception when the email is already taken")
    void registerUser_ShouldThrowException_WhenEmailExists() {
        // Given
        RegisterRequest request = RegisterRequest.builder()
                .email("zajety@example.com")
                .username("newuser")
                .build();

        when(userRepository.existsByEmail(request.getEmail())).thenReturn(true);

        // When & Then
        assertThrows(UserAlreadyExistsException.class, () -> userService.registerUser(request));
        verify(userRepository, never()).save(any());
    }

    @Test
    @DisplayName("registerUser: It should throw an exception when the username is already taken")
    void registerUser_ShouldThrowException_WhenUsernameExists() {
        // Given
        RegisterRequest request = RegisterRequest.builder()
                .email("wolny@example.com")
                .username("zajetyUser")
                .build();

        when(userRepository.existsByEmail(request.getEmail())).thenReturn(false);
        when(userRepository.existsByUsername(request.getUsername())).thenReturn(true);

        // When & Then
        assertThrows(UserAlreadyExistsException.class, () -> userService.registerUser(request));
    }

    @Test
    @DisplayName("registerUser: It should register the user and send an email when the details are correct")
    void registerUser_ShouldSaveUserAndSendEmail_WhenDataIsValid() {
        // Given
        RegisterRequest request = RegisterRequest.builder()
                .email("test@example.com")
                .username("testUser")
                .password("password123")
                .build();

        UserEntity userEntity = new UserEntity();
        userEntity.setEmail("test@example.com");

        when(userRepository.existsByEmail(request.getEmail())).thenReturn(false);
        when(userRepository.existsByUsername(request.getUsername())).thenReturn(false);
        when(userMapper.toRegisterRequestEntity(request)).thenReturn(userEntity);
        when(passwordEncoder.encode(request.getPassword())).thenReturn("encodedPass");
        when(verificationTokenService.generateToken()).thenReturn("token123");
        when(userRepository.save(any(UserEntity.class))).thenReturn(userEntity);
        when(userMapper.toDto(any(UserEntity.class))).thenReturn(new UserResponseDto());

        // When
        userService.registerUser(request);

        // Then
        verify(userRepository).save(userEntity);
        verify(emailService).sendVerificationEmail(eq("test@example.com"), contains("token123"));
        assertEquals(UserRole.USER, userEntity.getRole());
        assertEquals("encodedPass", userEntity.getPassword());
    }
}