package com.BookReview.System.Service;

import com.BookReview.System.Model.Dto.Security.LogoutRequest;
import org.springframework.http.ResponseEntity;

public interface LogoutService {
    ResponseEntity<String> logout(LogoutRequest logoutRequestDto) ;

}
