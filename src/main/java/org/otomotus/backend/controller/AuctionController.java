package org.otomotus.backend.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.otomotus.backend.dto.AuctionCreateRequestDto;
import org.otomotus.backend.dto.AuctionResponseDto;
import org.otomotus.backend.dto.AuctionUpdateRequestDto;
import org.otomotus.backend.entity.AuctionEntity;
import org.otomotus.backend.entity.AuctionImageEntity;
import org.otomotus.backend.exception.ResourceNotFoundException;
import org.otomotus.backend.repository.AuctionImageRepository;
import org.otomotus.backend.repository.AuctionRepository;
import org.otomotus.backend.service.AuctionService;
import org.otomotus.backend.service.ContractService;
import org.otomotus.backend.service.ImageStorageService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.UUID;

/**
 * Kontroler REST API dla zarządzania aukcjami samochodów.
 * <p>
 * Udostępnia operacje CRUD dla aukcji, obsługę zdjęć oraz generowanie umów sprzedaży.
 * Wszystkie operacje są zabezpieczone uwierzytelnianiem.
 * </p>
 *
 * @author Otomotus Development Team
 * @version 1.0
 */
@RestController
@RequestMapping("/api/auctions")
@RequiredArgsConstructor
public class AuctionController {

    private final AuctionService auctionService;
    private final ImageStorageService imageStorageService;
    private final ContractService contractService;

    private final AuctionRepository auctionRepository;
    private final AuctionImageRepository auctionImageRepository;

    /**
     * Tworzy nową aukcję dla zalogowanego użytkownika.
     *
     * @param request dane nowej aukcji
     * @param authentication dane uwierzytelniającego się użytkownika
     * @return odpowiedź 200 OK z danymi utworzonej aukcji
     */
    @PostMapping
    public ResponseEntity<AuctionResponseDto> createAuction(
            @Valid @RequestBody AuctionCreateRequestDto request,
            Authentication authentication) {
        return ResponseEntity.ok(auctionService.createAuction(request, authentication.getName()));
    }

    /**
     * Pobiera listę wszystkich aktywnych aukcji z paginacją.
     *
     * @param pageable parametry paginacji
     * @return strona z aukcjami
     */
    @GetMapping
    public ResponseEntity<Page<AuctionResponseDto>> getAllAuctions(Pageable pageable) {
        return ResponseEntity.ok(auctionService.getAllActiveAuctions(pageable));
    }

    /**
     * Pobiera szczegóły konkretnej aukcji i inkrementuje licznik wyświetleń.
     *
     * @param id identyfikator aukcji
     * @return dane aukcji
     * @throws ResourceNotFoundException jeśli aukcja nie zostanie znaleziona
     */
    @GetMapping("/{id}")
    public ResponseEntity<AuctionResponseDto> getAuction(@PathVariable UUID id) {
        return ResponseEntity.ok(auctionService.getAuctionDetails(id));
    }

    /**
     * Pobiera aukcje utworzone przez zalogowanego użytkownika.
     *
     * @param authentication dane uwierzytelniającego się użytkownika
     * @return lista aukcji użytkownika
     */
    @GetMapping("/my-auctions")
    public ResponseEntity<List<AuctionResponseDto>> getMyAuctions(Authentication authentication) {
        return ResponseEntity.ok(auctionService.getUserAuctions(authentication.getName()));
    }

    /**
     * Aktualizuje istniejącą aukcję.
     * Tylko właściciel aukcji ma prawo ją modyfikować.
     *
     * @param id identyfikator aukcji
     * @param request nowe dane aukcji
     * @param authentication dane uwierzytelniającego się użytkownika
     * @return zaktualizowane dane aukcji
     */
    @PutMapping("/{id}")
    public ResponseEntity<AuctionResponseDto> updateAuction(
            @PathVariable UUID id,
            @RequestBody AuctionUpdateRequestDto request,
            Authentication authentication) {
        return ResponseEntity.ok(auctionService.updateAuction(id, request, authentication.getName()));
    }

    /**
     * Usuwa aukcję. Tylko właściciel aukcji ma prawo ją usunąć.
     *
     * @param id identyfikator aukcji
     * @param authentication dane uwierzytelniającego się użytkownika
     * @return odpowiedź 204 No Content
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteAuction(@PathVariable UUID id, Authentication authentication) {
        auctionService.deleteAuction(id, authentication.getName());
        return ResponseEntity.noContent().build();
    }

    /**
     * Przesyła zdjęcie do aukcji.
     * Tylko właściciel aukcji ma prawo przesyłać zdjęcia.
     *
     * @param id identyfikator aukcji
     * @param file plik zdjęcia (MultipartFile)
     * @param authentication dane uwierzytelniającego się użytkownika
     * @return komunikat o powodzeniu z nazwą zapisanego pliku
     */
    @PostMapping(value = "/{id}/images", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<String> uploadImage(
            @PathVariable UUID id,
            @RequestParam("file") MultipartFile file,
            Authentication authentication) {

        AuctionEntity auction = auctionRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Auction not found"));

        if (!auction.getSeller().getUsername().equals(authentication.getName())) {
            return ResponseEntity.status(403).body("You are not the seller of this auction");
        }

        String fileName = imageStorageService.storeFile(file);

        AuctionImageEntity imageEntity = new AuctionImageEntity();
        imageEntity.setAuction(auction);
        imageEntity.setImageUrl(fileName);

        auctionImageRepository.save(imageEntity);

        return ResponseEntity.ok("Photo saved: " + fileName);
    }

    /**
     * Generuje umowę sprzedaży samochodu w formacie PDF.
     * Tylko właściciel aukcji ma prawo generować umowę.
     *
     * @param id identyfikator aukcji
     * @param authentication dane uwierzytelniającego się użytkownika
     * @return plik PDF umowy
     */
    @GetMapping("/{id}/contract")
    public ResponseEntity<byte[]> generateContract(@PathVariable UUID id, Authentication authentication) {
        byte[] pdfBytes = contractService.generateCarSaleContract(id, authentication.getName());

        HttpHeaders headers = new HttpHeaders();
        headers.add(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=umowa_" + id + ".pdf");
        headers.add(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_PDF_VALUE);

        return ResponseEntity.ok()
                .headers(headers)
                .body(pdfBytes);
    }

    /**
     * Dodaje aukcję do ulubionych zalogowanego użytkownika.
     *
     * @param id identyfikator aukcji
     * @param authentication dane uwierzytelniającego się użytkownika
     * @return odpowiedź 200 OK
     */
    @PostMapping("/{id}/favorite")
    public ResponseEntity<Void> addToFavorites(@PathVariable UUID id, Authentication authentication) {
        auctionService.addToFavorites(id, authentication.getName());
        return ResponseEntity.ok().build();
    }

    /**
     * Usuwa aukcję z ulubionych zalogowanego użytkownika.
     *
     * @param id identyfikator aukcji
     * @param authentication dane uwierzytelniającego się użytkownika
     * @return odpowiedź 200 OK
     */
    @DeleteMapping("/{id}/favorite")
    public ResponseEntity<Void> removeFromFavorites(@PathVariable UUID id, Authentication authentication) {
        auctionService.removeFromFavorites(id, authentication.getName());
        return ResponseEntity.ok().build();
    }

    /**
     * Pobiera listę ulubionych aukcji zalogowanego użytkownika.
     *
     * @param authentication dane uwierzytelniającego się użytkownika
     * @return lista ulubionych aukcji
     */
    @GetMapping("/favorites")
    public ResponseEntity<List<AuctionResponseDto>> getFavoriteAuctions(Authentication authentication) {
        return ResponseEntity.ok(auctionService.getFavoriteAuctions(authentication.getName()));
    }
}