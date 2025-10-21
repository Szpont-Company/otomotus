package org.otomotus.backend.mapper;

import org.mapstruct.Mapper;
import org.otomotus.backend.dto.UserDto;
import org.otomotus.backend.entity.UserEntity;

@Mapper(componentModel = "spring")
public interface UserMapper {
    UserDto toDto(UserEntity userEntity);
    UserEntity toEntity(UserDto userDto);
}
