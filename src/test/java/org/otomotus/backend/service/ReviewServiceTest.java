package org.otomotus.backend.service;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.otomotus.backend.dto.ReviewCreateRequestDto;
import org.otomotus.backend.dto.ReviewResponseDto;
import org.otomotus.backend.entity.AuctionEntity;
import org.otomotus.backend.entity.ReviewEntity;
import org.otomotus.backend.entity.UserEntity;
import org.otomotus.backend.exception.ResourceNotFoundException;
import org.otomotus.backend.mapper.ReviewMapper;
import org.otomotus.backend.repository.AuctionRepository;
import org.otomotus.backend.repository.ReviewRepository;
import org.otomotus.backend.repository.UserRepository;

import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ReviewServiceTest {

    @Mock private ReviewRepository reviewRepository;
    @Mock private AuctionRepository auctionRepository;
    @Mock private UserRepository userRepository;
    @Mock private ReviewMapper reviewMapper;

    @InjectMocks
    private ReviewService reviewService;

    @Test
    @DisplayName("Should throw IllegalStateException when seller tries to review their own auction")
    void addReview_ShouldThrowException_WhenSellerReviewsOwnAuction() {
        // Given
        UUID userId = UUID.randomUUID();
        String username = "seller";

        UserEntity user = new UserEntity();
        user.setId(userId);
        user.setUsername(username);

        AuctionEntity auction = new AuctionEntity();
        auction.setId(UUID.randomUUID());
        auction.setSeller(user); // Seller is the same as reviewer

        ReviewCreateRequestDto request = new ReviewCreateRequestDto(auction.getId(), 5, "Great!");

        when(userRepository.findByUsername(username)).thenReturn(Optional.of(user));
        when(auctionRepository.findById(request.getAuctionId())).thenReturn(Optional.of(auction));

        // When & Then
        assertThrows(IllegalStateException.class,
                () -> reviewService.addReview(request, username),
                "Should throw exception for self-review"
        );
    }

    @Test
    @DisplayName("Should throw IllegalStateException when user has already reviewed the auction")
    void addReview_ShouldThrowException_WhenReviewAlreadyExists() {
        // Given
        UUID reviewerId = UUID.randomUUID();
        UUID sellerId = UUID.randomUUID();
        String reviewerName = "buyer";

        UserEntity reviewer = new UserEntity();
        reviewer.setId(reviewerId);
        reviewer.setUsername(reviewerName);

        UserEntity seller = new UserEntity();
        seller.setId(sellerId);

        AuctionEntity auction = new AuctionEntity();
        auction.setId(UUID.randomUUID());
        auction.setSeller(seller);

        ReviewCreateRequestDto request = new ReviewCreateRequestDto(auction.getId(), 5, "Good");

        when(userRepository.findByUsername(reviewerName)).thenReturn(Optional.of(reviewer));
        when(auctionRepository.findById(request.getAuctionId())).thenReturn(Optional.of(auction));
        when(reviewRepository.existsByAuctionIdAndReviewerId(auction.getId(), reviewerId)).thenReturn(true);

        // When & Then
        assertThrows(IllegalStateException.class,
                () -> reviewService.addReview(request, reviewerName)
        );
    }

    @Test
    @DisplayName("Should save review successfully when valid data is provided")
    void addReview_ShouldSaveReview_WhenDataIsValid() {
        // Given
        UUID reviewerId = UUID.randomUUID();
        UUID sellerId = UUID.randomUUID();
        String reviewerName = "buyer";

        UserEntity reviewer = new UserEntity();
        reviewer.setId(reviewerId);

        UserEntity seller = new UserEntity();
        seller.setId(sellerId);

        AuctionEntity auction = new AuctionEntity();
        auction.setId(UUID.randomUUID());
        auction.setSeller(seller);

        ReviewCreateRequestDto request = new ReviewCreateRequestDto(auction.getId(), 5, "Nice car");

        when(userRepository.findByUsername(reviewerName)).thenReturn(Optional.of(reviewer));
        when(auctionRepository.findById(request.getAuctionId())).thenReturn(Optional.of(auction));
        when(reviewRepository.existsByAuctionIdAndReviewerId(any(), any())).thenReturn(false);
        when(reviewRepository.save(any(ReviewEntity.class))).thenAnswer(i -> i.getArguments()[0]);
        when(reviewMapper.toDto(any())).thenReturn(new ReviewResponseDto());

        // When
        reviewService.addReview(request, reviewerName);

        // Then
        verify(reviewRepository).save(any(ReviewEntity.class));
    }

    @Test
    @DisplayName("Should throw ResourceNotFoundException when auction does not exist")
    void addReview_ShouldThrowException_WhenAuctionNotFound() {
        // Given
        String username = "user";
        ReviewCreateRequestDto request = new ReviewCreateRequestDto(UUID.randomUUID(), 5, "Comment");

        when(userRepository.findByUsername(username)).thenReturn(Optional.of(new UserEntity()));
        when(auctionRepository.findById(request.getAuctionId())).thenReturn(Optional.empty());

        // When & Then
        assertThrows(ResourceNotFoundException.class,
                () -> reviewService.addReview(request, username)
        );
    }
}