package org.otomotus.backend.service;

import lombok.RequiredArgsConstructor;
import org.otomotus.backend.auth.config.VerificationStatus;
import org.otomotus.backend.auth.dto.RegisterRequest;
import org.otomotus.backend.auth.service.VerificationTokenService;
import org.otomotus.backend.config.UserRole;
import org.otomotus.backend.dto.UserResponseDto;
import org.otomotus.backend.dto.UserUpdateRequestDto;
import org.otomotus.backend.entity.UserEntity;
import org.otomotus.backend.exception.UserAlreadyExistsException;
import org.otomotus.backend.mapper.UserMapper;
import org.otomotus.backend.repository.UserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

/**
 * Serwis dla zarządzania użytkownikami aplikacji.
 * <p>
 * Zawiera logikę biznesową dla rejestracji, weryfikacji, aktualizacji i usuwania użytkowników.
 * </p>
 *
 * @author Otomotus Development Team
 * @version 1.0
 */
@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final UserMapper userMapper;
    private final VerificationTokenService verificationTokenService;
    private final EmailService emailService;
    private final PasswordEncoder passwordEncoder;

    /**
     * Rejestruje nowego użytkownika w systemie.
     * <p>
     * Sprawdza unikalność email i nazwy użytkownika, koduje hasło i wysyła link weryfikacyjny.
     * </p>
     *
     * @param registerRequest dane nowego użytkownika
     * @return DTO zarejestrowanego użytkownika
     * @throws UserAlreadyExistsException jeśli email lub login już istnieją
     */
    public UserResponseDto registerUser(RegisterRequest registerRequest) {
        if (userRepository.existsByEmail(registerRequest.getEmail())) {
            throw new UserAlreadyExistsException("Email already exists");
        }

        if (userRepository.existsByUsername(registerRequest.getUsername())) {
            throw new UserAlreadyExistsException("Username already taken");
        }

        UserEntity userEntity = userMapper.toRegisterRequestEntity(registerRequest);
        userEntity.setRole(UserRole.USER);
        userEntity.setCreatedAt(LocalDateTime.now());
        userEntity.setLastLoginDate(LocalDateTime.now());
        userEntity.setPassword(passwordEncoder.encode(registerRequest.getPassword()));

        String token = verificationTokenService.generateToken();
        userEntity.setVerificationToken(token);
        userEntity.setTokenExpirationDate(LocalDateTime.now().plusHours(24));

        userRepository.save(userEntity);

        String verificationUrl = "http://localhost:5173/activate?token=" + token;
        emailService.sendVerificationEmail(userEntity.getEmail(), verificationUrl);

        return userMapper.toDto(userEntity);
    }

    /**
     * Pobiera listę wszystkich użytkowników w systemie.
     *
     * @return lista DTO wszystkich użytkowników
     */
    public List<UserResponseDto> getAll() {
        return userRepository.findAll().stream()
                .map(userMapper::toDto)
                .toList();
    }

    /**
     * Pobiera użytkownika po ID.
     *
     * @param userId identyfikator użytkownika
     * @return DTO użytkownika
     * @throws RuntimeException jeśli użytkownik nie zostanie znaleziony
     */
    public UserResponseDto getById(UUID userId) {
        return userMapper.toDto(userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found")));
    }

    /**
     * Aktualizuje dane użytkownika (operacja PATCH).
     * <p>
     * Selektywnie aktualizuje tylko pola, które zostały podane w żądaniu.
     * </p>
     *
     * @param userId identyfikator użytkownika
     * @param userUpdateRequest dane do zaktualizowania
     * @return zaktualizowane DTO użytkownika
     * @throws RuntimeException jeśli użytkownik nie zostanie znaleziony
     */
    public UserResponseDto patch(UUID userId, UserUpdateRequestDto userUpdateRequest) {
        UserEntity userEntity = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        if (userUpdateRequest.getUsername() != null) {
            userEntity.setUsername(userUpdateRequest.getUsername());
        }
        if (userUpdateRequest.getEmail() != null) {
            userEntity.setEmail(userUpdateRequest.getEmail());
        }
        if (userUpdateRequest.getPhoneNumber() != null) {
            userEntity.setPhoneNumber(userUpdateRequest.getPhoneNumber());
        }
        if (userUpdateRequest.getFirstName() != null) {
            userEntity.setFirstName(userUpdateRequest.getFirstName());
        }
        if (userUpdateRequest.getLastName() != null) {
            userEntity.setLastName(userUpdateRequest.getLastName());
        }
        if (userUpdateRequest.getRole() != null) {
            userEntity.setRole(userUpdateRequest.getRole());
        }

        return userMapper.toDto(userRepository.save(userEntity));
    }

    /**
     * Usuwa użytkownika ze systemu.
     *
     * @param userId identyfikator użytkownika do usunięcia
     */
    public void delete(UUID userId) {
        userRepository.deleteById(userId);
    }

    /**
     * Weryfikuje token przesłany w emailu podczas rejestracji.
     * <p>
     * Sprawdza ważność i wygaśnięcie tokenu, a następnie aktywuje konto użytkownika.
     * </p>
     *
     * @param token token weryfikacyjny
     * @return status weryfikacji (VERIFIED, INVALID, EXPIRED)
     */
    public VerificationStatus verifyToken(String token) {
        Optional<UserEntity> userOptional = userRepository.findByVerificationToken(token);

        if (userOptional.isEmpty()) {
            return VerificationStatus.INVALID;
        }

        UserEntity user = userOptional.get();

        if (verificationTokenService.isTokenExpired(user.getTokenExpirationDate())) {
            return VerificationStatus.EXPIRED;
        }

        user.setActivated(true);
        user.setVerificationToken(null);
        userRepository.save(user);

        emailService.sendWelcomeEmail(user.getEmail(), user.getUsername());

        return VerificationStatus.VERIFIED;
    }
}