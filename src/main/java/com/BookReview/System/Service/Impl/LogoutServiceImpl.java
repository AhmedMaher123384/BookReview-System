package com.BookReview.System.Service.Impl;

import com.BookReview.System.Exception.TokenValidationException;
import com.BookReview.System.Model.Dto.Security.LogoutRequest;
import com.BookReview.System.Model.Entity.AccessTokenBlackList;
import com.BookReview.System.Model.Entity.RefreshTokenBlackList;
import com.BookReview.System.Repository.AccessTokenBlackListRepository;
import com.BookReview.System.Repository.RefreshTokenBlackListRepository;
import com.BookReview.System.Security.CustomeUserDetailsService;
import com.BookReview.System.Security.JwtUtil;
import com.BookReview.System.Security.SecurityConstants;
import com.BookReview.System.Service.LogoutService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.Date;
@Service
public class LogoutServiceImpl implements LogoutService {

    @Autowired
    private JwtUtil jwtUtil;
    @Autowired
    private CustomeUserDetailsService userDetailsService;
    @Autowired
    private AccessTokenBlackListRepository accessTokenBlackListRepository;
    @Autowired
    private RefreshTokenBlackListRepository refreshTokenBlackListRepository;

    @Override
    public ResponseEntity<String> logout(LogoutRequest logoutRequest) {
        if (!jwtUtil.isTokenValidate(logoutRequest.getAccessToken(), userDetailsService.loadUserByUsername(jwtUtil.extractUserName(logoutRequest.getAccessToken(), SecurityConstants.SECRET_KEY)), SecurityConstants.SECRET_KEY) ||
                !jwtUtil.isTokenValidate(logoutRequest.getRefreshToken(), userDetailsService.loadUserByUsername(jwtUtil.extractUserName(logoutRequest.getRefreshToken(), SecurityConstants.REFRESH_SECRET_KEY)), SecurityConstants.REFRESH_SECRET_KEY)) {
            throw new TokenValidationException("Invalid token");
        }

        Date accessTokenExpiry = jwtUtil.extractExpirationDate(logoutRequest.getAccessToken(), SecurityConstants.SECRET_KEY);
        Date refreshTokenExpiry = jwtUtil.extractExpirationDate(logoutRequest.getRefreshToken(), SecurityConstants.REFRESH_SECRET_KEY);

        AccessTokenBlackList blacklistedAccessToken = new AccessTokenBlackList(null, logoutRequest.getAccessToken(), accessTokenExpiry);
        RefreshTokenBlackList blacklistedRefreshToken = new RefreshTokenBlackList(null, logoutRequest.getRefreshToken(), refreshTokenExpiry);

        accessTokenBlackListRepository.save(blacklistedAccessToken);
        refreshTokenBlackListRepository.save(blacklistedRefreshToken);
        return new ResponseEntity<>("Logout Success", HttpStatus.OK);
    }
}
