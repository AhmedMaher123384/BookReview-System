package com.BookReview.System.Controller;

import com.BookReview.System.Model.Dto.*;
import com.BookReview.System.Model.Entity.Role;
import com.BookReview.System.Model.Entity.UserEntity;
import com.BookReview.System.Repository.RoleRepository;
import com.BookReview.System.Repository.UserRepository;
import com.BookReview.System.Security.CustomeUserDetailsService;
import com.BookReview.System.Security.JwtUtil;
import com.BookReview.System.Security.SecurityConstants;
import com.BookReview.System.Service.Impl.AuthServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;


@Slf4j
@RestController
@RequestMapping("/auth")
public class AuthController {

    @Autowired
    private AuthServiceImpl authService;


    @PostMapping("/register")
    public ResponseEntity<String> register(@RequestBody RegisterDto registerDto) {
        return this.authService.register(registerDto);
    }

    @PostMapping("/login")
    public ResponseEntity<TokenResponse> login(@RequestBody LoginDto loginDto) {
       return  this.authService.login(loginDto);
    }

    @PostMapping("/refresh")
    public ResponseEntity<AccessTokenResponse> refreshToken(@RequestBody RefreshTokenRequest request) {
        return this.authService.refreshToken(request);
    }

    @PatchMapping("/reset-password")
    public ResponseEntity<String> refreshToken(@RequestBody ChangePasswordRequest request , Authentication authentication) {
         return this.authService.changePassword(request,authentication);
    }


}
