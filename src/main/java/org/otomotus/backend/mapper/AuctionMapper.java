package org.otomotus.backend.mapper;

import org.mapstruct.*;
import org.otomotus.backend.dto.AuctionCreateRequestDto;
import org.otomotus.backend.dto.AuctionResponseDto;
import org.otomotus.backend.entity.AuctionEntity;
import org.otomotus.backend.entity.AuctionImageEntity;
import org.otomotus.backend.entity.CarEntity;

/**
 * Mapper dla konwersji między encjami aukcji a DTOs.
 * <p>
 * Wykorzystuje MapStruct do automatycznego mapowania pól między obiektami.
 * </p>
 *
 * @author Otomotus Development Team
 * @version 1.0
 */
@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface AuctionMapper {

    @Mapping(target = "brand", source = "car.brand")
    @Mapping(target = "model", source = "car.model")
    @Mapping(target = "productionYear", source = "car.productionYear")
    @Mapping(target = "mileage", source = "car.mileage")
    @Mapping(target = "fuelType", source = "car.fuelType")
    @Mapping(target = "enginePower", source = "car.enginePower")
    @Mapping(target = "engineCapacity", source = "car.engineCapacity")
    @Mapping(target = "transmissionType", source = "car.transmission")
    @Mapping(target = "sellerId", source = "seller.id")
    @Mapping(target = "imageUrls", source = "images")
    @Mapping(target = "description", source = "description")
    @Mapping(target = "sellerFirstName", source = "seller.firstName")
    @Mapping(target = "sellerLastName", source = "seller.lastName")
    @Mapping(target = "sellerSinceYear", expression = "java(entity.getSeller().getCreatedAt().getYear())")
    AuctionResponseDto toDto(AuctionEntity entity);

    CarEntity toCarEntity(AuctionCreateRequestDto dto);

    default String map(AuctionImageEntity image) {
        return image != null ? image.getImageUrl() : null;
    }

    @Mapping(target = "car", ignore = true)
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "images", ignore = true)
    AuctionEntity toAuctionEntity(AuctionCreateRequestDto dto);
}