package org.otomotus.backend.mapper;

import org.mapstruct.*;
import org.otomotus.backend.dto.UserCreateRequestDto;
import org.otomotus.backend.dto.UserResponseDto;
import org.otomotus.backend.dto.UserUpdateRequestDto;
import org.otomotus.backend.entity.UserEntity;

@Mapper(componentModel = "spring")
public interface UserMapper {
    UserResponseDto toDto(UserEntity userEntity);
    UserEntity toEntity(UserResponseDto userResponseDto);

    UserCreateRequestDto toCreateRequestDto(UserEntity userEntity);
    UserEntity toCreateRequestEntity(UserCreateRequestDto userCreateRequestDto);

    UserUpdateRequestDto toUpdateRequestDto(UserEntity userEntity);
    UserEntity toUpdateRequestEntity(UserUpdateRequestDto userUpdateRequestDto);
}
