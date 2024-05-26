package com.BookReview.System.Repository;


import com.BookReview.System.Model.Entity.AccessTokenBlackList;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.Optional;

@Repository
public interface AccessTokenBlackListRepository extends JpaRepository<AccessTokenBlackList, Long> {
    Optional<AccessTokenBlackList> findByToken(String token);
}