package org.otomotus.backend.mapper;

import org.mapstruct.Mapper;
import org.otomotus.backend.dto.auction.AuctionResponseDto;
import org.otomotus.backend.entity.AuctionEntity;

@Mapper(componentModel = "spring")
public interface AuctionMapper {
    AuctionResponseDto toAuctionResponseDto(AuctionEntity auctionEntity);
    AuctionEntity toAuctionEntity(AuctionResponseDto auctionResponseDto);
}
