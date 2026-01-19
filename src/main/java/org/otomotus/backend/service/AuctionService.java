package org.otomotus.backend.service;

import lombok.RequiredArgsConstructor;
import org.otomotus.backend.config.AuctionStatus;
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
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class AuctionService {

    private final AuctionRepository auctionRepository;
    private final UserRepository userRepository;
    private final AuctionMapper auctionMapper;

    @Transactional
    public AuctionResponseDto createAuction(AuctionCreateRequestDto request, String username) {
        UserEntity seller = userRepository.findByUsername(username)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));

        CarEntity car = auctionMapper.toCarEntity(request);
        AuctionEntity auction = auctionMapper.toAuctionEntity(request);

        auction.setCar(car);
        auction.setSeller(seller);
        auction.setStatus(AuctionStatus.ACTIVE);
        auction.setExpiresAt(LocalDateTime.now().plusDays(30)); // Default 30 days

        return auctionMapper.toDto(auctionRepository.save(auction));
    }

    public Page<AuctionResponseDto> getAllActiveAuctions(Pageable pageable) {
        return auctionRepository.findAllByStatus(AuctionStatus.ACTIVE, pageable)
                .map(auctionMapper::toDto);
    }

    public List<AuctionResponseDto> getUserAuctions(String username) {
        UserEntity user = userRepository.findByUsername(username)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));

        return auctionRepository.findAllBySellerId(user.getId()).stream()
                .map(auctionMapper::toDto)
                .toList();
    }

    public AuctionResponseDto getAuctionDetails(UUID id) {
        AuctionEntity auction = auctionRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Auction not found"));

        auction.setViewCount(auction.getViewCount() + 1);
        auctionRepository.save(auction);

        return auctionMapper.toDto(auction);
    }

    @Transactional
    public void deleteAuction(UUID id, String username) {
        AuctionEntity auction = auctionRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Auction not found"));

        if (!auction.getSeller().getUsername().equals(username)) {
            throw new RuntimeException("You are not authorized to delete this auction");
        }

        auctionRepository.delete(auction);
    }

    @Transactional
    public AuctionResponseDto updateAuction(UUID id, AuctionUpdateRequestDto request, String username) {
        AuctionEntity auction = auctionRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Auction not found"));

        if (!auction.getSeller().getUsername().equals(username)) {
            throw new RuntimeException("You are not authorized to update this auction");
        }

        if (request.getTitle() != null) auction.setTitle(request.getTitle());
        if (request.getDescription() != null) auction.setDescription(request.getDescription());
        if (request.getPrice() != null) auction.setPrice(request.getPrice());
        if (request.getLocation() != null) auction.setLocation(request.getLocation());
        if (request.getNegotiable() != null) auction.setNegotiable(request.getNegotiable());
        if (request.getPhoneNumber() != null) auction.setPhoneNumber(request.getPhoneNumber());

        CarEntity car = auction.getCar();
        if (request.getBrand() != null) car.setBrand(request.getBrand());
        if (request.getModel() != null) car.setModel(request.getModel());
        if (request.getGeneration() != null) car.setGeneration(request.getGeneration());
        if (request.getProductionYear() != null) car.setProductionYear(request.getProductionYear());
        if (request.getMileage() != null) car.setMileage(request.getMileage());
        if (request.getEngineCapacity() != null) car.setEngineCapacity(request.getEngineCapacity());
        if (request.getEnginePower() != null) car.setEnginePower(request.getEnginePower());
        if (request.getFuelType() != null) car.setFuelType(request.getFuelType());
        if (request.getTransmission() != null) car.setTransmission(request.getTransmission());
        if (request.getColor() != null) car.setColor(request.getColor());

        return auctionMapper.toDto(auctionRepository.save(auction));
    }
}