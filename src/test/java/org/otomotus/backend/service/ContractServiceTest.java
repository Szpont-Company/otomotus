package org.otomotus.backend.service;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.otomotus.backend.entity.AuctionEntity;
import org.otomotus.backend.exception.ResourceNotFoundException;
import org.otomotus.backend.repository.AuctionRepository;
import org.otomotus.backend.repository.UserRepository;

import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ContractServiceTest {

    @Mock private AuctionRepository auctionRepository;
    @Mock private UserRepository userRepository;

    @InjectMocks
    private ContractService contractService;

    @Test
    @DisplayName("generateCarSaleContract: Should throw ResourceNotFoundException when auction not found")
    void generateContract_ShouldThrowException_WhenAuctionNotFound() {
        // Given
        UUID auctionId = UUID.randomUUID();
        String buyerName = "buyer";

        when(auctionRepository.findById(auctionId)).thenReturn(Optional.empty());

        // When & Then
        assertThrows(ResourceNotFoundException.class, () ->
                contractService.generateCarSaleContract(auctionId, buyerName)
        );
    }

    @Test
    @DisplayName("generateCarSaleContract: Should throw ResourceNotFoundException when buyer not found")
    void generateContract_ShouldThrowException_WhenBuyerNotFound() {
        // Given
        UUID auctionId = UUID.randomUUID();
        String buyerName = "unknownBuyer";

        when(auctionRepository.findById(auctionId)).thenReturn(Optional.of(new AuctionEntity()));
        when(userRepository.findByUsername(buyerName)).thenReturn(Optional.empty());

        // When & Then
        assertThrows(ResourceNotFoundException.class, () ->
                contractService.generateCarSaleContract(auctionId, buyerName)
        );
    }
}