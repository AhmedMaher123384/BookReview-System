package com.BookReview.System.Service;

import com.BookReview.System.Model.Dto.*;
import com.BookReview.System.Model.Entity.Book;
import com.BookReview.System.Model.Entity.Review;
import com.BookReview.System.Repository.BookRepository;
import com.BookReview.System.Repository.ReviewRepository;
import com.BookReview.System.Service.Impl.ReviewServiceImpl;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class ReviewServiceTest {
    @Mock
    private ReviewRepository reviewRepository;
    @Mock
    private BookRepository bookRepository;
    @InjectMocks
    private ReviewServiceImpl reviewService;
    private Book book;
    private Review review;
    private ReviewDto reviewDto;
    private AddReviewDto addReviewDto;


    @BeforeEach
    public void init(){
        book =Book.builder().name("cracking code").type("programming type").build();
        review= Review.builder().title("review title").content("test content").stars(5).build();
        addReviewDto= AddReviewDto.builder().title("review title").content("test content").stars(5).build();
        reviewDto= ReviewDto.builder().title("review title").content("test content").stars(5).build();
    }

    @Test
    public void BookService_createReview_ReturnReviewDto(){
        when(bookRepository.findById(book.getId())).thenReturn(Optional.of(book));
        when(reviewRepository.save(Mockito.any(Review.class))).thenReturn(review);
        Assertions.assertThat(reviewService.createReview(book.getId(),addReviewDto)).isEqualTo(reviewDto);
    }

    @Test
    public void BookService_getReviewsByBookId_ReturnListOfReviewDto(){
        Long id =1l;
        List<Review> reviews = Arrays.asList(review);
        List<ReviewDto> reviewDtos = Arrays.asList(reviewDto);

        when(reviewRepository.findByBookId(id)).thenReturn(reviews);
        Assertions.assertThat(reviewService.getReviewsByBookId(id)).isEqualTo(reviewDtos);
    }

    @Test
    public void BookService_getReviewById_ReturnReviewDto(){
        Long id =1l;
        when(bookRepository.findById(id)).thenReturn(Optional.of(book));
        when(reviewRepository.findById(id)).thenReturn(Optional.of(review));
        review.setBook(book);
        Assertions.assertThat(reviewService.getReviewById(id,id)).isNotNull();
        Assertions.assertThat(reviewService.getReviewById(id,id)).isEqualTo(reviewDto);
    }

    @Test
    public void BookService_updateReview_ReturnReviewDto(){
        Long id =1l;
        when(bookRepository.findById(id)).thenReturn(Optional.of(book));
        when(reviewRepository.findById(id)).thenReturn(Optional.of(review));
        review.setBook(book);
        doNothing().when(reviewRepository).delete(review);
        assertAll(() -> reviewService.deleteReview(id,id));

    }


}
