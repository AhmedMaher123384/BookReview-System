package com.BookReview.System.Repository;

import com.BookReview.System.Model.Dto.ReviewDto;
import com.BookReview.System.Model.Entity.Review;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ReviewRepository extends JpaRepository<Review,Long> {
    List<Review>findByBookId(Long id);
}
