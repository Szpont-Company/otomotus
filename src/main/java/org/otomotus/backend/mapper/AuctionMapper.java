package org.otomotus.backend.mapper;

import org.mapstruct.*;
import org.otomotus.backend.dto.AuctionCreateRequestDto;
import org.otomotus.backend.dto.AuctionResponseDto;
import org.otomotus.backend.entity.AuctionEntity;
import org.otomotus.backend.entity.AuctionImageEntity;
import org.otomotus.backend.entity.CarEntity;

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