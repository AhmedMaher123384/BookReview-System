package com.BookReview.System.Service;

import com.BookReview.System.Model.Dto.AddReviewDto;
import com.BookReview.System.Model.Dto.ReviewDto;

import java.util.List;

public interface ReviewService {

    ReviewDto createReview(Long pokemonId, AddReviewDto addReviewDto);
    List<ReviewDto> getReviewsByBookId(Long id);
    ReviewDto getReviewById(Long reviewId, Long pokemonId);
    ReviewDto updateReview(Long pokemonId, Long reviewId, ReviewDto reviewDto);
    void deleteReview(Long pokemonId, Long reviewId);
}
