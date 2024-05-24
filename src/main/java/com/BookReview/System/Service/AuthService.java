package com.BookReview.System.Service;

import com.BookReview.System.Model.Dto.*;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;

public interface AuthService {
    ResponseEntity<String> register(@RequestBody RegisterDto registerDto);
   ResponseEntity<TokenResponse> login(@RequestBody LoginDto loginDto);
    ResponseEntity<AccessTokenResponse> refreshToken(@RequestBody RefreshTokenRequest request);
}
