package org.otomotus.backend.service;

import lombok.RequiredArgsConstructor;
import org.otomotus.backend.dto.UserDto;
import org.otomotus.backend.entity.UserEntity;
import org.otomotus.backend.mapper.UserMapper;
import org.otomotus.backend.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final UserMapper userMapper;

    public UserDto create(UserDto userDto) {
        UserEntity userEntity = userMapper.toEntity(userDto);
        userEntity.setCreatedAt(LocalDateTime.now());
        userEntity.setLastLoginDate(LocalDateTime.now());

        return userMapper.toDto(userRepository.save(userEntity));
    }


}