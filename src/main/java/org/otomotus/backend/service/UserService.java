package org.otomotus.backend.service;

import lombok.RequiredArgsConstructor;
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
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final UserMapper userMapper;

    public UserResponseDto create(UserCreateRequestDto userCreateRequestDto) {
        UserEntity userEntity = userMapper.toCreateRequestEntity(userCreateRequestDto);
        userEntity.setRole(UserRole.USER);
        userEntity.setCreatedAt(LocalDateTime.now());
        userEntity.setLastLoginDate(LocalDateTime.now());

        return userMapper.toDto(userRepository.save(userEntity));
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
}