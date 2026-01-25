package org.otomotus.backend.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.otomotus.backend.config.FuelType;
import org.otomotus.backend.config.TransmissionType;
import org.otomotus.backend.dto.AuctionCreateRequestDto;
import org.otomotus.backend.dto.AuctionResponseDto;
import org.otomotus.backend.service.AuctionService;
import org.springframework.data.web.PageableHandlerMethodArgumentResolver;
import org.springframework.http.MediaType;
import org.springframework.security.core.Authentication;

import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.math.BigDecimal;

import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(MockitoExtension.class)
class AuctionControllerTest {

    private MockMvc mockMvc;

    @Mock private AuctionService auctionService;

    @InjectMocks
    private AuctionController auctionController;

    private final ObjectMapper objectMapper = new ObjectMapper();

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(auctionController)
                .setCustomArgumentResolvers(new PageableHandlerMethodArgumentResolver())
                .build();
    }

    @Test
    @DisplayName("createAuction: Should return 200 OK and created auction data")
    void createAuction_ShouldReturnOk() throws Exception {
        // Given
        AuctionCreateRequestDto request = AuctionCreateRequestDto.builder()
                .title("New Car")
                .price(BigDecimal.valueOf(50000))
                .vin("12345678901234567")
                .brand("BMW")
                .model("X5")
                .fuelType(FuelType.DIESEL)
                .transmission(TransmissionType.AUTOMATIC)
                .productionYear(2020)
                .phoneNumber("123-456-789")
                .location("Warsaw")
                .build();

        AuctionResponseDto responseDto = new AuctionResponseDto();
        responseDto.setId(UUID.randomUUID());
        responseDto.setTitle("New Car");

        Authentication authentication = mock(Authentication.class);
        when(authentication.getName()).thenReturn("testUser");

        when(auctionService.createAuction(any(AuctionCreateRequestDto.class), eq("testUser")))
                .thenReturn(responseDto);

        // When & Then
        mockMvc.perform(post("/api/auctions")
                        .principal(authentication)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.title").value("New Car"))
                .andExpect(jsonPath("$.id").exists());
    }

    @Test
    @DisplayName("deleteAuction: Should return 204 No Content")
    void deleteAuction_ShouldReturnNoContent() throws Exception {
        // Given
        UUID auctionId = UUID.randomUUID();
        Authentication authentication = mock(Authentication.class);
        when(authentication.getName()).thenReturn("owner");

        // When & Then
        mockMvc.perform(delete("/api/auctions/{id}", auctionId)
                        .principal(authentication))
                .andExpect(status().isNoContent());

        verify(auctionService).deleteAuction(auctionId, "owner");
    }
}