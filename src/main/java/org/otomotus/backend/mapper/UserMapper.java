package org.otomotus.backend.mapper;

import org.mapstruct.*;
import org.otomotus.backend.auth.dto.RegisterRequest;
import org.otomotus.backend.dto.UserCreateRequestDto;
import org.otomotus.backend.dto.UserResponseDto;
import org.otomotus.backend.dto.UserUpdateRequestDto;
import org.otomotus.backend.entity.UserEntity;

/**
 * Mapper dla konwersji między encjami użytkowników a DTOs.
 * <p>
 * Wykorzystuje MapStruct do automatycznego mapowania pól między obiektami.
 * </p>
 *
 * @author Otomotus Development Team
 * @version 1.0
 */
@Mapper(componentModel = "spring")
public interface UserMapper {
    UserResponseDto toDto(UserEntity userEntity);
    UserEntity toEntity(UserResponseDto userResponseDto);

    UserCreateRequestDto toCreateRequestDto(UserEntity userEntity);
    UserEntity toRegisterRequestEntity(RegisterRequest registerRequest);

    UserUpdateRequestDto toUpdateRequestDto(UserEntity userEntity);
    UserEntity toUpdateRequestEntity(UserUpdateRequestDto userUpdateRequestDto);
}
