package com.BookReview.System.Repository;

import com.BookReview.System.Model.Entity.Review;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
@Repository
public interface ReviewRepository extends JpaRepository<Review,Long> {
    List<Review>findByBookId(Long id);
}
