package org.otomotus.backend.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.otomotus.backend.dto.ReviewResponseDto;
import org.otomotus.backend.entity.ReviewEntity;

/**
 * Mapper dla konwersji między encjami opinii a DTOs.
 * <p>
 * Wykorzystuje MapStruct do automatycznego mapowania pól.
 * </p>
 *
 * @author Otomotus Development Team
 * @version 1.0
 */
@Mapper(componentModel = "spring")
public interface ReviewMapper {

    @Mapping(target = "reviewerFirstName", source = "reviewer.firstName")
    @Mapping(target = "auctionTitle", source = "auction.title")
    ReviewResponseDto toDto(ReviewEntity entity);
}