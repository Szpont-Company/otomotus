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

@RestController
@RequestMapping("/api/auctions")
@RequiredArgsConstructor
public class AuctionController {

    private final AuctionService auctionService;
    private final ImageStorageService imageStorageService;
    private final ContractService contractService;

    private final AuctionRepository auctionRepository;
    private final AuctionImageRepository auctionImageRepository;

    @PostMapping
    public ResponseEntity<AuctionResponseDto> createAuction(
            @Valid @RequestBody AuctionCreateRequestDto request,
            Authentication authentication) {
        return ResponseEntity.ok(auctionService.createAuction(request, authentication.getName()));
    }

    @GetMapping
    public ResponseEntity<Page<AuctionResponseDto>> getAllAuctions(Pageable pageable) {
        return ResponseEntity.ok(auctionService.getAllActiveAuctions(pageable));
    }

    @GetMapping("/{id}")
    public ResponseEntity<AuctionResponseDto> getAuction(@PathVariable UUID id) {
        return ResponseEntity.ok(auctionService.getAuctionDetails(id));
    }

    @GetMapping("/my-auctions")
    public ResponseEntity<List<AuctionResponseDto>> getMyAuctions(Authentication authentication) {
        return ResponseEntity.ok(auctionService.getUserAuctions(authentication.getName()));
    }

    @PutMapping("/{id}")
    public ResponseEntity<AuctionResponseDto> updateAuction(
            @PathVariable UUID id,
            @RequestBody AuctionUpdateRequestDto request,
            Authentication authentication) {
        return ResponseEntity.ok(auctionService.updateAuction(id, request, authentication.getName()));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteAuction(@PathVariable UUID id, Authentication authentication) {
        auctionService.deleteAuction(id, authentication.getName());
        return ResponseEntity.noContent().build();
    }

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
}