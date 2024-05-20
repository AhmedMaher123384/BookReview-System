package com.BookReview.System.Controller;

import com.BookReview.System.Model.Dto.AddReviewDto;
import com.BookReview.System.Model.Dto.ReviewDto;
import com.BookReview.System.Service.Impl.ReviewServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/review")
public class ReviewController {
    @Autowired
    private ReviewServiceImpl reviewService;

    @PostMapping("/{bookId}/")
    public ResponseEntity<ReviewDto> createReview(@PathVariable(value = "bookId") Long bookId, @RequestBody AddReviewDto addReviewDto) {
        return new ResponseEntity<>(reviewService.createReview(bookId, addReviewDto), HttpStatus.CREATED);
    }

    @GetMapping("/{bookId}/")
    public List<ReviewDto> getReviewsByPokemonId(@PathVariable(value = "bookId") Long pokemonId) {
        return reviewService.getReviewsByBookId(pokemonId);
    }

    @GetMapping("/{bookId}/{id}")
    public ResponseEntity<ReviewDto> getReviewById(@PathVariable(value = "bookId") Long bookId, @PathVariable(value = "id") Long reviewId) {
        ReviewDto reviewDto = reviewService.getReviewById(bookId, reviewId);
        return new ResponseEntity<>(reviewDto, HttpStatus.OK);
    }

    @PutMapping("/{bookId}/{id}")
    public ResponseEntity<ReviewDto> updateReview(@PathVariable(value = "bookId") Long bookId, @PathVariable(value = "id") Long reviewId,
                                                  @RequestBody ReviewDto reviewDto) {
        ReviewDto updatedReview = reviewService.updateReview(bookId, reviewId, reviewDto);
        return new ResponseEntity<>(updatedReview, HttpStatus.OK);
    }

    @DeleteMapping("/{bookId}/{id}")
    public ResponseEntity<String> deleteReview(@PathVariable(value = "bookId") Long bookId, @PathVariable(value = "id") Long reviewId) {
        reviewService.deleteReview(bookId, reviewId);
        return new ResponseEntity<>("Review deleted successfully", HttpStatus.NO_CONTENT);
    }


}
