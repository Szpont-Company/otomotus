package org.otomotus.backend.service;

import lombok.RequiredArgsConstructor;
import org.otomotus.backend.dto.ReviewCreateRequestDto;
import org.otomotus.backend.dto.ReviewResponseDto;
import org.otomotus.backend.dto.SellerRatingSummaryDto;
import org.otomotus.backend.entity.AuctionEntity;
import org.otomotus.backend.entity.ReviewEntity;
import org.otomotus.backend.entity.UserEntity;
import org.otomotus.backend.exception.ResourceNotFoundException;
import org.otomotus.backend.mapper.ReviewMapper;
import org.otomotus.backend.repository.AuctionRepository;
import org.otomotus.backend.repository.ReviewRepository;
import org.otomotus.backend.repository.UserRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

/**
 * Serwis do zarządzania ocenami i opiniami.
 * <p>
 * Obsługuje dodawanie nowych opinii oraz generowanie rankingu sprzedawców.
 * Zapewnia walidację, aby użytkownik nie mógł ocenić samego siebie ani dodać dublujących się opinii.
 * </p>
 *
 * @author Otomotus Development Team
 * @version 1.0
 */
@Service
@RequiredArgsConstructor
public class ReviewService {

    private final ReviewRepository reviewRepository;
    private final AuctionRepository auctionRepository;
    private final UserRepository userRepository;
    private final ReviewMapper reviewMapper;

    /**
     * Dodaje nową opinię dla sprzedawcy na podstawie aukcji.
     *
     * @param request dane opinii
     * @param reviewerUsername nazwa użytkownika wystawiającego opinię
     * @return utworzona opinia jako DTO
     * @throws ResourceNotFoundException jeśli aukcja lub użytkownik nie istnieje
     * @throws IllegalStateException jeśli użytkownik próbuje ocenić samego siebie lub już ocenił tę aukcję
     */
    @Transactional
    public ReviewResponseDto addReview(ReviewCreateRequestDto request, String reviewerUsername) {
        UserEntity reviewer = userRepository.findByUsername(reviewerUsername)
                .orElseThrow(() -> new ResourceNotFoundException("Reviewer not found"));

        AuctionEntity auction = auctionRepository.findById(request.getAuctionId())
                .orElseThrow(() -> new ResourceNotFoundException("Auction not found"));

        UserEntity seller = auction.getSeller();

        if (seller.getId().equals(reviewer.getId())) {
            throw new IllegalStateException("You cannot rate your own auction");
        }

        if (reviewRepository.existsByAuctionIdAndReviewerId(auction.getId(), reviewer.getId())) {
            throw new IllegalStateException("You have already reviewed this auction");
        }

        ReviewEntity review = new ReviewEntity();
        review.setAuction(auction);
        review.setReviewer(reviewer);
        review.setSeller(seller);
        review.setRating(request.getRating());
        review.setComment(request.getComment());

        return reviewMapper.toDto(reviewRepository.save(review));
    }

    /**
     * Pobiera opinie wystawione danemu sprzedawcy z paginacją.
     *
     * @param sellerId identyfikator sprzedawcy
     * @param pageable parametry paginacji
     * @return strona z opiniami
     */
    @Transactional(readOnly = true)
    public Page<ReviewResponseDto> getSellerReviews(UUID sellerId, Pageable pageable) {
        if (!userRepository.existsById(sellerId)) {
            throw new ResourceNotFoundException("Seller not found");
        }
        return reviewRepository.findAllBySellerId(sellerId, pageable)
                .map(reviewMapper::toDto);
    }

    /**
     * Pobiera podsumowanie rankingu sprzedawcy.
     *
     * @param sellerId identyfikator sprzedawcy
     * @return podsumowanie (średnia ocena, liczba opinii)
     */
    @Transactional(readOnly = true)
    public SellerRatingSummaryDto getSellerRatingSummary(UUID sellerId) {
        UserEntity seller = userRepository.findById(sellerId)
                .orElseThrow(() -> new ResourceNotFoundException("Seller not found"));

        Double average = reviewRepository.getAverageRatingForSeller(sellerId);
        long count = reviewRepository.countBySellerId(sellerId);

        return SellerRatingSummaryDto.builder()
                .averageRating(average != null ? Math.round(average * 10.0) / 10.0 : 0.0)
                .totalReviews(count)
                .sellerUsername(seller.getUsername())
                .build();
    }
}