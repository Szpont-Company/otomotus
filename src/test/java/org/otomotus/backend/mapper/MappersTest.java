package org.otomotus.backend.mapper;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;
import org.otomotus.backend.auth.dto.RegisterRequest;
import org.otomotus.backend.dto.AuctionResponseDto;
import org.otomotus.backend.entity.AuctionEntity;
import org.otomotus.backend.entity.CarEntity;
import org.otomotus.backend.entity.UserEntity;

import java.time.LocalDateTime;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

class MappersTest {

    private final UserMapper userMapper = Mappers.getMapper(UserMapper.class);
    private final AuctionMapper auctionMapper = Mappers.getMapper(AuctionMapper.class);

    @Test
    @DisplayName("UserMapper: Should map RegisterRequest to UserEntity correctly")
    void userMapper_ShouldMapRegisterRequestToEntity() {
        // Given
        RegisterRequest request = new RegisterRequest();
        request.setUsername("johndoe");
        request.setEmail("john@example.com");
        request.setFirstName("John");

        // When
        UserEntity entity = userMapper.toRegisterRequestEntity(request);

        // Then
        assertNotNull(entity);
        assertEquals("johndoe", entity.getUsername());
        assertEquals("john@example.com", entity.getEmail());
        assertEquals("John", entity.getFirstName());
    }

    @Test
    @DisplayName("AuctionMapper: Should map nested CarEntity properties to flat AuctionResponseDto")
    void auctionMapper_ShouldFlattenCarProperties() {
        // Given
        UserEntity seller = new UserEntity();
        seller.setId(UUID.randomUUID());
        seller.setFirstName("Jan");
        seller.setLastName("Nowak");
        seller.setCreatedAt(LocalDateTime.of(2020, 1, 1, 12, 0));

        CarEntity car = new CarEntity();
        car.setBrand("Audi");
        car.setModel("A4");
        car.setProductionYear(2015);

        AuctionEntity auction = new AuctionEntity();
        auction.setCar(car);
        auction.setSeller(seller);
        auction.setTitle("Super Audi");

        // When
        AuctionResponseDto dto = auctionMapper.toDto(auction);

        // Then
        assertNotNull(dto);
        assertEquals("Super Audi", dto.getTitle());

        assertEquals("Audi", dto.getBrand());
        assertEquals("A4", dto.getModel());
        assertEquals(2015, dto.getProductionYear());

        assertEquals("Jan", dto.getSellerFirstName());
        assertEquals("Nowak", dto.getSellerLastName());
        assertEquals(2020, dto.getSellerSinceYear()); // Wyrażenie java(entity.getSeller()...)
    }
}