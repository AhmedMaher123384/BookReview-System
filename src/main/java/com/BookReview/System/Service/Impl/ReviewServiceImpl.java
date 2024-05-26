package com.BookReview.System.Service.Impl;

import com.BookReview.System.Exception.BookNotFoundException;
import com.BookReview.System.Exception.ReviewNotFoundException;
import com.BookReview.System.Model.Dto.AddReviewDto;
import com.BookReview.System.Model.Dto.ReviewDto;
import com.BookReview.System.Model.Entity.Book;
import com.BookReview.System.Model.Entity.Review;
import com.BookReview.System.Repository.BookRepository;
import com.BookReview.System.Repository.ReviewRepository;
import com.BookReview.System.Service.ReviewService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class ReviewServiceImpl implements ReviewService {

    @Autowired
    private ReviewRepository reviewRepository;
    @Autowired
    private BookRepository bookRepository;

    @Override
    public ReviewDto createReview(Long pokemonId, AddReviewDto addReviewDto) {
        Review review =mapToEntity(addReviewDto);
        Book book = this.bookRepository.findById(pokemonId).orElseThrow(()->new BookNotFoundException("Pokemon could not be delete"));
        review.setBook(book);
        Review newReview = this.reviewRepository.save(review);
        ReviewDto dto =mapToDto(newReview);
        return dto;
    }


    @Override
    public List<ReviewDto> getReviewsByBookId(Long id) {

        List<Review> reviews = this.reviewRepository.findByBookId(id);
        return maptoListDto(reviews);
    }

    @Override
    public ReviewDto getReviewById(Long reviewId, Long pokemonId) {
        Book book = this.bookRepository.findById(pokemonId).orElseThrow(()-> new BookNotFoundException("book could not be delete"));
        Review review = this.reviewRepository.findById(reviewId).orElseThrow(()-> new ReviewNotFoundException("review with associate book not found)"));

        if(book.getId() != review.getBook().getId()){
            throw new ReviewNotFoundException("This review does not belong to a book");
        }
        return mapToDto(review);
    }

    @Override
    public ReviewDto updateReview(Long pokemonId, Long reviewId, ReviewDto reviewDto) {
        Book book = this.bookRepository.findById(pokemonId).orElseThrow(()-> new BookNotFoundException("book could not be delete"));
        Review review = this.reviewRepository.findById(reviewId).orElseThrow(()-> new ReviewNotFoundException("review with associate book not found)"));

        if(review.getBook().getId()!=book.getId()){
            throw new ReviewNotFoundException("This review does not belong to a book");
        }
        review.setContent(reviewDto.getContent());
        review.setTitle(reviewDto.getTitle());
        review.setStars(reviewDto.getStars());
        Review newReview = this.reviewRepository.save(review);
        ReviewDto dto =mapToDto(newReview);
        return dto;
    }

    @Override
    public void deleteReview(Long pokemonId, Long reviewId) {
        Book book = this.bookRepository.findById(pokemonId).orElseThrow(()-> new BookNotFoundException("book could not be delete"));
        Review review = this.reviewRepository.findById(reviewId).orElseThrow(()-> new ReviewNotFoundException("review with associate book not found)"));

        if(review.getBook().getId()!=book.getId()){
            throw new ReviewNotFoundException("This review does not belong to a book");
        }
        this.reviewRepository.delete(review);
    }



    private ReviewDto mapToDto(Review review) {
        return ReviewDto.builder()
                .id(review.getId())
                .title(review.getTitle())
                .content(review.getContent())
                .stars(review.getStars())
                .createdBy(review.getCreatedBy())
                .createdDate(review.getCreatedDate())
                .lastModifiedDate(review.getLastModifiedDate())
                .build();
    }
    public Review mapToEntity(AddReviewDto addReviewDto){
        return Review.builder().title(addReviewDto.getTitle()).content(addReviewDto.getContent()).stars(addReviewDto.getStars()).build();
    }
   public  List<ReviewDto> maptoListDto(List<Review> reviews){
        List<ReviewDto> dtos = reviews.stream().map(review -> {
            ReviewDto reviewDto= ReviewDto
                    .builder()
                    .id(review.getId())
                    .title(review.getTitle())
                    .stars(review.getStars())
                    .content(review.getContent())
                    .build();
            return reviewDto;
        }).collect(Collectors.toList());
        return dtos;
    }
}
