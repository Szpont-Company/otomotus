package org.otomotus.backend.service;

import lombok.RequiredArgsConstructor;
import org.otomotus.backend.auth.config.VerificationStatus;
import org.otomotus.backend.config.UserRole;
import org.otomotus.backend.dto.UserCreateRequestDto;
import org.otomotus.backend.dto.UserResponseDto;
import org.otomotus.backend.dto.UserUpdateRequestDto;
import org.otomotus.backend.entity.UserEntity;
import org.otomotus.backend.mapper.UserMapper;
import org.otomotus.backend.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final UserMapper userMapper;
    private final VerificationTokenService verificationTokenService;
    private final EmailService emailService;

    public UserResponseDto create(UserCreateRequestDto userCreateRequestDto) {
        UserEntity userEntity = userMapper.toCreateRequestEntity(userCreateRequestDto);
        userEntity.setRole(UserRole.USER);
        userEntity.setCreatedAt(LocalDateTime.now());
        userEntity.setLastLoginDate(LocalDateTime.now());

        String token = verificationTokenService.generateToken();
        userEntity.setVerificationToken(token);
        userEntity.setTokenExpirationDate(LocalDateTime.now().plusHours(24));

        userRepository.save(userEntity);

        String verificationUrl = "http://localhost:8080/api/auth/verify?token=" + token;
        emailService.sendVerificationEmail(userEntity.getEmail(), verificationUrl);

        return userMapper.toDto(userEntity);
    }

    public List<UserResponseDto> getAll() {
        return userRepository.findAll().stream()
                .map(userMapper::toDto)
                .toList();
    }

    public UserResponseDto getById(UUID userId) {
        return userMapper.toDto(userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found")));
    }

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

    public void delete(UUID userId) {
        userRepository.deleteById(userId);
    }

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

        return VerificationStatus.VERIFIED;
    }
}