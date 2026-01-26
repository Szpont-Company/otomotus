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

/**
 * Serwis dla zarządzania aukcjami samochodów.
 * <p>
 * Zawiera logikę biznesową dla operacji CRUD na aukcjach, włącznie z filtrowaniem,
 * obliczaniem statystyk i zarządzaniem statusami aukcji.
 * </p>
 *
 * @author Otomotus Development Team
 * @version 1.0
 */
@Service
@RequiredArgsConstructor
public class AuctionService {

    private final AuctionRepository auctionRepository;
    private final UserRepository userRepository;
    private final AuctionMapper auctionMapper;

    /**
     * Tworzy nową aukcję dla podanego użytkownika.
     * <p>
     * Aukcja jest domyślnie ustawiana na status ACTIVE z czasem ważności 30 dni.
     * </p>
     *
     * @param request dane nowej aukcji (samochód i warunki sprzedaży)
     * @param username nazwa użytkownika (sprzedawcy)
     * @return DTO utworzonej aukcji
     * @throws ResourceNotFoundException jeśli użytkownik nie zostanie znaleziony
     */
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

    /**
     * Pobiera stronę aktywnych aukcji z paginacją.
     *
     * @param pageable parametry paginacji
     * @return strona zawierająca aukcje z statusem ACTIVE
     */
    @Transactional(readOnly = true)
    public Page<AuctionResponseDto> getAllActiveAuctions(Pageable pageable) {
        return auctionRepository.findAllByStatus(AuctionStatus.ACTIVE, pageable)
                .map(auctionMapper::toDto);
    }

    /**
     * Pobiera wszystkie aukcje utworzone przez danego użytkownika.
     *
     * @param username nazwa użytkownika (sprzedawcy)
     * @return lista aukcji użytkownika
     * @throws ResourceNotFoundException jeśli użytkownik nie zostanie znaleziony
     */
    @Transactional(readOnly = true)
    public List<AuctionResponseDto> getUserAuctions(String username) {
        UserEntity user = userRepository.findByUsername(username)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));

        return auctionRepository.findAllBySellerId(user.getId()).stream()
                .map(auctionMapper::toDto)
                .toList();
    }

    /**
     * Pobiera szczegóły aukcji i inkrementuje licznik wyświetleń.
     * <p>
     * Metoda również pobiera liczbę ogółem aukcji sprzedawcy.
     * </p>
     *
     * @param id identyfikator aukcji
     * @return szczegóły aukcji z liczbą wystawień sprzedawcy
     * @throws ResourceNotFoundException jeśli aukcja nie zostanie znaleziona
     */
    @Transactional
    public AuctionResponseDto getAuctionDetails(UUID id) {
        AuctionEntity auction = auctionRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Auction not found"));

        auction.setViewCount(auction.getViewCount() + 1);
        auctionRepository.save(auction);

        AuctionResponseDto dto = auctionMapper.toDto(auction);

        UserEntity seller = auction.getSeller();
        if (seller != null) {
            int listingsCount = auctionRepository.countBySellerId(seller.getId());
            dto.setSellerListingCount(listingsCount);
        }
        return dto;
    }

    /**
     * Usuwa aukcję. Tylko właściciel aukcji ma prawo ją usunąć.
     *
     * @param id identyfikator aukcji
     * @param username nazwa zalogowanego użytkownika
     * @throws ResourceNotFoundException jeśli aukcja nie zostanie znaleziona
     * @throws RuntimeException jeśli użytkownik nie jest właścicielem aukcji
     */
    @Transactional
    public void deleteAuction(UUID id, String username) {
        AuctionEntity auction = auctionRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Auction not found"));

        if (!auction.getSeller().getUsername().equals(username)) {
            throw new RuntimeException("You are not authorized to delete this auction");
        }

        auctionRepository.delete(auction);
    }

    /**
     * Aktualizuje dane aukcji. Tylko właściciel aukcji ma prawo ją modyfikować.
     * <p>
     * Metoda selektywnie aktualizuje tylko pola, które zostały podane w żądaniu.
     * </p>
     *
     * @param id identyfikator aukcji
     * @param request dane do zaktualizowania
     * @param username nazwa zalogowanego użytkownika
     * @return zaktualizowana aukcja
     * @throws ResourceNotFoundException jeśli aukcja nie zostanie znaleziona
     * @throws RuntimeException jeśli użytkownik nie jest właścicielem aukcji
     */
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

    /**
     * Dodaje aukcję do ulubionych użytkownika.
     *
     * @param auctionId identyfikator aukcji
     * @param username nazwa zalogowanego użytkownika
     * @throws ResourceNotFoundException jeśli aukcja lub użytkownik nie zostanie znaleziony
     */
    @Transactional
    public void addToFavorites(UUID auctionId, String username) {
        UserEntity user = userRepository.findByUsername(username)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));

        AuctionEntity auction = auctionRepository.findById(auctionId)
                .orElseThrow(() -> new ResourceNotFoundException("Auction not found"));

        user.getFavoriteAuctions().add(auction);
        userRepository.save(user);
    }

    /**
     * Usuwa aukcję z ulubionych użytkownika.
     *
     * @param auctionId identyfikator aukcji
     * @param username nazwa zalogowanego użytkownika
     * @throws ResourceNotFoundException jeśli aukcja lub użytkownik nie zostanie znaleziony
     */
    @Transactional
    public void removeFromFavorites(UUID auctionId, String username) {
        UserEntity user = userRepository.findByUsername(username)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));

        AuctionEntity auction = auctionRepository.findById(auctionId)
                .orElseThrow(() -> new ResourceNotFoundException("Auction not found"));

        user.getFavoriteAuctions().remove(auction);
        userRepository.save(user);
    }

    /**
     * Pobiera listę ulubionych aukcji użytkownika.
     *
     * @param username nazwa użytkownika
     * @return lista ulubionych aukcji użytkownika
     * @throws ResourceNotFoundException jeśli użytkownik nie zostanie znaleziony
     */
    @Transactional(readOnly = true)
    public List<AuctionResponseDto> getFavoriteAuctions(String username) {
        UserEntity user = userRepository.findByUsername(username)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));

        return user.getFavoriteAuctions().stream()
                .map(auctionMapper::toDto)
                .toList();
    }
}