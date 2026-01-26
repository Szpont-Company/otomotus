package org.otomotus.backend.service;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.otomotus.backend.dto.AuctionCreateRequestDto;
import org.otomotus.backend.dto.AuctionResponseDto;
import org.otomotus.backend.dto.AuctionUpdateRequestDto;
import org.otomotus.backend.entity.AuctionEntity;
import org.otomotus.backend.entity.CarEntity;
import org.otomotus.backend.entity.UserEntity;
import org.otomotus.backend.exception.ResourceNotFoundException;
import org.otomotus.backend.mapper.AuctionMapper;
import org.otomotus.backend.repository.AuctionRepository;
import org.otomotus.backend.repository.UserRepository;

import java.math.BigDecimal;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AuctionServiceTest {

    @Mock
    private AuctionRepository auctionRepository;

    @Mock
    private UserRepository userRepository;

    @Mock
    private AuctionMapper auctionMapper;

    @InjectMocks
    private AuctionService auctionService;

    @Test
    @DisplayName("createAuction: It should create an auction when the user exists")
    void createAuction_ShouldReturnDto_WhenUserExists() {
        // Given
        String username = "testUser";
        AuctionCreateRequestDto request = new AuctionCreateRequestDto();
        request.setTitle("Sprzedam Opla");

        UserEntity user = new UserEntity();
        user.setUsername(username);

        CarEntity car = new CarEntity();
        AuctionEntity auctionEntity = new AuctionEntity();
        auctionEntity.setSeller(user);

        AuctionResponseDto expectedDto = new AuctionResponseDto();
        expectedDto.setTitle("Sprzedam Opla");

        // Mockowanie zachowań
        when(userRepository.findByUsername(username)).thenReturn(Optional.of(user));
        when(auctionMapper.toCarEntity(request)).thenReturn(car);
        when(auctionMapper.toAuctionEntity(request)).thenReturn(auctionEntity);
        when(auctionRepository.save(any(AuctionEntity.class))).thenReturn(auctionEntity);
        when(auctionMapper.toDto(auctionEntity)).thenReturn(expectedDto);

        // When
        AuctionResponseDto result = auctionService.createAuction(request, username);

        // Then
        assertNotNull(result);
        assertEquals("Sprzedam Opla", result.getTitle());
        verify(auctionRepository, times(1)).save(any(AuctionEntity.class));
    }

    @Test
    @DisplayName("createAuction: It should throw a ResourceNotFoundException when the user does not exist")
    void createAuction_ShouldThrowException_WhenUserNotFound() {
        // Given
        String username = "unknownUser";
        when(userRepository.findByUsername(username)).thenReturn(Optional.empty());

        // When & Then
        assertThrows(ResourceNotFoundException.class, () ->
                auctionService.createAuction(new AuctionCreateRequestDto(), username)
        );
        verify(auctionRepository, never()).save(any());
    }

    @Test
    @DisplayName("updateAuction: It should update the price when the user is the owner")
    void updateAuction_ShouldUpdatePrice_WhenUserIsOwner() {
        // Given
        UUID auctionId = UUID.randomUUID();
        String username = "owner";

        UserEntity seller = new UserEntity();
        seller.setUsername(username);

        AuctionEntity auction = new AuctionEntity();
        auction.setId(auctionId);
        auction.setSeller(seller);
        auction.setPrice(BigDecimal.valueOf(1000));
        auction.setCar(new CarEntity()); // Potrzebne, bo serwis pobiera CarEntity

        AuctionUpdateRequestDto updateRequest = new AuctionUpdateRequestDto();
        updateRequest.setPrice(BigDecimal.valueOf(2000));

        when(auctionRepository.findById(auctionId)).thenReturn(Optional.of(auction));
        when(auctionRepository.save(any(AuctionEntity.class))).thenAnswer(i -> i.getArguments()[0]);
        when(auctionMapper.toDto(any())).thenReturn(new AuctionResponseDto());

        // When
        auctionService.updateAuction(auctionId, updateRequest, username);

        // Then
        assertEquals(BigDecimal.valueOf(2000), auction.getPrice());
        verify(auctionRepository).save(auction);
    }

    @Test
    @DisplayName("updateAuction: It should throw a RuntimeException when the user is not the owner")
    void updateAuction_ShouldThrowException_WhenUserIsNotOwner() {
        // Given
        UUID auctionId = UUID.randomUUID();
        String ownerUsername = "owner";
        String hackerUsername = "hacker";

        UserEntity seller = new UserEntity();
        seller.setUsername(ownerUsername);

        AuctionEntity auction = new AuctionEntity();
        auction.setSeller(seller);

        when(auctionRepository.findById(auctionId)).thenReturn(Optional.of(auction));

        // When & Then
        RuntimeException exception = assertThrows(RuntimeException.class, () ->
                auctionService.updateAuction(auctionId, new AuctionUpdateRequestDto(), hackerUsername)
        );

        assertEquals("You are not authorized to update this auction", exception.getMessage());
    }

    @Test
    @DisplayName("getAuctionDetails: It should increase the view counter")
    void getAuctionDetails_ShouldIncrementViewCount() {
        // Given
        UUID auctionId = UUID.randomUUID();
        AuctionEntity auction = new AuctionEntity();
        auction.setViewCount(0);
        auction.setSeller(new UserEntity()); // Potrzebne do countBySellerId

        when(auctionRepository.findById(auctionId)).thenReturn(Optional.of(auction));
        when(auctionMapper.toDto(auction)).thenReturn(new AuctionResponseDto());

        // When
        auctionService.getAuctionDetails(auctionId);

        // Then
        assertEquals(1, auction.getViewCount());
        verify(auctionRepository).save(auction);
    }
}