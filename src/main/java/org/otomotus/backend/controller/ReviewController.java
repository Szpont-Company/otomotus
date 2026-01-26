package org.otomotus.backend.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.otomotus.backend.dto.ReviewCreateRequestDto;
import org.otomotus.backend.dto.ReviewResponseDto;
import org.otomotus.backend.dto.SellerRatingSummaryDto;
import org.otomotus.backend.service.ReviewService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

/**
 * Kontroler REST API dla systemu ocen i opinii.
 * <p>
 * Udostępnia endpointy do dodawania opinii oraz pobierania rankingu sprzedawców.
 * </p>
 *
 * @author Otomotus Development Team
 * @version 1.0
 */
@RestController
@RequestMapping("/api/reviews")
@RequiredArgsConstructor
public class ReviewController {

    private final ReviewService reviewService;

    /**
     * Dodaje nową opinię
     *
     * @param request dane opinii
     * @param authentication dane zalogowanego użytkownika
     * @return utworzona opinia
     */
    @PostMapping
    public ResponseEntity<ReviewResponseDto> addReview(
            @Valid @RequestBody ReviewCreateRequestDto request,
            Authentication authentication) {
        return ResponseEntity.ok(reviewService.addReview(request, authentication.getName()));
    }

    /**
     * Pobiera listę opinii dla konkretnego sprzedawcy.
     *
     * @param sellerId identyfikator sprzedawcy
     * @param pageable parametry paginacji (domyślnie sortowane po dacie malejąco)
     * @return strona z opiniami
     */
    @GetMapping("/seller/{sellerId}")
    public ResponseEntity<Page<ReviewResponseDto>> getSellerReviews(
            @PathVariable UUID sellerId,
            @PageableDefault(sort = "createdAt", direction = Sort.Direction.DESC) Pageable pageable) {
        return ResponseEntity.ok(reviewService.getSellerReviews(sellerId, pageable));
    }

    /**
     * Pobiera statystyki rankingu sprzedawcy
     *
     * @param sellerId identyfikator sprzedawcy
     * @return podsumowanie rankingu
     */
    @GetMapping("/seller/{sellerId}/summary")
    public ResponseEntity<SellerRatingSummaryDto> getSellerRatingSummary(@PathVariable UUID sellerId) {
        return ResponseEntity.ok(reviewService.getSellerRatingSummary(sellerId));
    }
}