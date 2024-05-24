package com.BookReview.System.Service.Impl;

import com.BookReview.System.Model.Dto.*;
import com.BookReview.System.Model.Entity.Role;
import com.BookReview.System.Model.Entity.UserEntity;
import com.BookReview.System.Repository.RoleRepository;
import com.BookReview.System.Repository.UserRepository;
import com.BookReview.System.Security.CustomeUserDetailsService;
import com.BookReview.System.Security.JwtUtil;
import com.BookReview.System.Security.SecurityConstants;
import com.BookReview.System.Service.AuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Collections;

@Service
public class AuthServiceImpl implements AuthService {

    @Autowired
    private AuthenticationManager authenticationManager;
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private RoleRepository roleRepository;
    @Autowired
    private JwtUtil jwtUtil;
    @Autowired
    private CustomeUserDetailsService userDetailsService;

    @Override
    public ResponseEntity<String> register(RegisterDto registerDto) {
        if (userRepository.existsByUsername(registerDto.getUsername())) {
            return new ResponseEntity<>("Username already exists", HttpStatus.BAD_REQUEST);
        }

        Role role = roleRepository.findByName("USER").orElse(null);
        if (role == null) {
            return new ResponseEntity<>("Role 'USER' not found", HttpStatus.INTERNAL_SERVER_ERROR);
        }

        String password = passwordEncoder.encode(registerDto.getPassword());

        UserEntity user = UserEntity.builder()
                .username(registerDto.getUsername())
                .password(password)
                .roles(Collections.singletonList(role))
                .build();
        userRepository.save(user);

        return new ResponseEntity<>("Username registered successfully", HttpStatus.CREATED);
    }

    @Override
    public ResponseEntity<TokenResponse> login(LoginDto loginDto) {
        try {
            Authentication auth = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken
                    (loginDto.getUsername(), loginDto.getPassword()));
            SecurityContextHolder.getContext().setAuthentication(auth);
            UserDetails user = userDetailsService.loadUserByUsername(loginDto.getUsername());
            String accessToken = jwtUtil.generateToken(user);
            String refreshToken = jwtUtil.generateRefreshToken(user);
            TokenResponse response= new TokenResponse(accessToken);
            response.setRefreshToken(refreshToken);
            return new ResponseEntity<>(response, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        }

    }



    @Override
    public ResponseEntity<AccessTokenResponse> refreshToken(RefreshTokenRequest request) {
        try {
            String refreshToken = request.getRefreshToken();
            String username = jwtUtil.extractUserName(refreshToken, SecurityConstants.REFRESH_SECRET_KEY);
            UserDetails userDetails = userDetailsService.loadUserByUsername(username);

            if (jwtUtil.isRefreshTokenValidate(refreshToken, userDetails)) {
                String newAccessToken = jwtUtil.generateToken(userDetails);
                AccessTokenResponse tokenResponse = new AccessTokenResponse(newAccessToken);

                return new ResponseEntity<>(tokenResponse, HttpStatus.OK);
            } else {
                return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
            }
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }
}
