package com.BookReview.System.Service;

import com.BookReview.System.Exception.*;
import com.BookReview.System.Model.Dto.Security.*;
import com.BookReview.System.Model.Entity.Role;
import com.BookReview.System.Model.Entity.UserEntity;
import com.BookReview.System.Repository.RoleRepository;
import com.BookReview.System.Repository.UserRepository;
import com.BookReview.System.Security.CustomeUserDetailsService;
import com.BookReview.System.Security.JwtUtil;
import com.BookReview.System.Security.SecurityConstants;
import com.BookReview.System.Service.Impl.AuthServiceImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import java.util.Optional;
import static org.mockito.Mockito.*;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class AuthServiceTest {

    @Mock
    private AuthenticationManager authenticationManager;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private UserRepository userRepository;

    @Mock
    private RoleRepository roleRepository;

    @Mock
    private JwtUtil jwtUtil;

    @Mock
    private CustomeUserDetailsService userDetailsService;

    @InjectMocks
    private AuthServiceImpl authService;

    @Test
    public void login_Valid_Success() {
        LoginDto loginDto = new LoginDto("validUser", "validPassword");
        UserDetails userDetails = Mockito.mock(UserDetails.class);

        when(authenticationManager.authenticate(any()))
                .thenReturn(Mockito.mock(Authentication.class));
        when(userDetailsService.loadUserByUsername(loginDto.getUsername()))
                .thenReturn(userDetails);
        when(jwtUtil.generateToken(userDetails))
                .thenReturn("validAccessToken");
        when(jwtUtil.generateRefreshToken(userDetails))
                .thenReturn("validRefreshToken");

        ResponseEntity<TokenResponse> response = authService.login(loginDto);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals("validAccessToken", response.getBody().getAccessToken());
        assertEquals("validRefreshToken", response.getBody().getRefreshToken());
    }
    @Test
    public void login_Invalid_Failure() {
        LoginDto loginDto = new LoginDto("invalidUser", "invalidPassword");

        when(authenticationManager.authenticate(any()))
                .thenThrow(new AuthenticationException("Invalid credentials") {});

        assertThrows(LoginException.class, () -> {
            authService.login(loginDto);
        });
    }

    @Test
    public void refreshToken_ValidRefreshToken_Success() {
        RefreshTokenRequest request = new RefreshTokenRequest("validRefreshToken");
        UserDetails userDetails = Mockito.mock(UserDetails.class);

        when(jwtUtil.extractUserName(request.getRefreshToken(), SecurityConstants.REFRESH_SECRET_KEY))
                .thenReturn("validUsername");
        when(userDetailsService.loadUserByUsername("validUsername"))
                .thenReturn(userDetails);
        when(jwtUtil.isRefreshTokenValidate(request.getRefreshToken(), userDetails))
                .thenReturn(true);
        when(jwtUtil.generateToken(userDetails))
                .thenReturn("newAccessToken");

        ResponseEntity<AccessTokenResponse> response = authService.refreshToken(request);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals("newAccessToken", response.getBody().getAccessToken());
    }


    @Test
    public void refreshToken_InvalidRefreshToken_Failure() {
        RefreshTokenRequest request = new RefreshTokenRequest("invalidRefreshToken");
        UserDetails userDetails = Mockito.mock(UserDetails.class);

        when(jwtUtil.extractUserName(request.getRefreshToken(), SecurityConstants.REFRESH_SECRET_KEY))
                .thenReturn("invalidUsername");
        when(userDetailsService.loadUserByUsername("invalidUsername"))
                .thenReturn(userDetails);
        when(jwtUtil.isRefreshTokenValidate(request.getRefreshToken(), userDetails))
                .thenReturn(false);

        assertThrows(TokenValidationException.class, () -> {
            authService.refreshToken(request);
        });
    }


    @Test
    public void register_NewUser_Success() {
        RegisterDto registerDto = new RegisterDto("newUser", "password");

        when(userRepository.existsByUsername(registerDto.getUsername())).thenReturn(false);
        when(roleRepository.findByName("USER")).thenReturn(Optional.of(new Role()));

        ResponseEntity<String> response = authService.register(registerDto);

        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertEquals("Username registered successfully", response.getBody());
    }

    @Test
    public void register_ExistingUsername_Failure() {
        RegisterDto registerDto = new RegisterDto("existingUser", "password");

        when(userRepository.existsByUsername(registerDto.getUsername())).thenReturn(true);

        assertThrows(UsernameAlreadyExistsException.class, () -> {
            authService.register(registerDto);
        });
    }

    @Test
    public void register_RoleNotFound_Failure() {
        RegisterDto registerDto = new RegisterDto("newUser", "password");

        when(userRepository.existsByUsername(registerDto.getUsername())).thenReturn(false);
        when(roleRepository.findByName("USER")).thenReturn(Optional.empty());

        assertThrows(RoleNotFoundException.class, () -> {
            authService.register(registerDto);
        });
    }

    @Test
    public void changePassword_Success() {
        ChangePasswordRequest request = new ChangePasswordRequest("currentPassword", "newPassword", "newPassword");
        Authentication authentication = mock(Authentication.class);
        UserDetails userDetails = mock(UserDetails.class);
        UserEntity user = mock(UserEntity.class);

        when(authentication.getPrincipal()).thenReturn(userDetails);
        when(userDetails.getUsername()).thenReturn("validUser");
        when(userRepository.findByUsername("validUser")).thenReturn(Optional.of(user));
        when(passwordEncoder.matches("currentPassword", user.getPassword())).thenReturn(true);
        when(passwordEncoder.encode("newPassword")).thenReturn("encodedNewPassword");

        ResponseEntity<String> response = authService.changePassword(request, authentication);

        verify(user).setPassword("encodedNewPassword");
        verify(userRepository).save(user);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("Password changed successfully", response.getBody());
    }

    @Test
    public void changePassword_Failure_WrongCurrentPassword() {
        ChangePasswordRequest request = new ChangePasswordRequest("wrongCurrentPassword", "newPassword", "newPassword");
        Authentication authentication = mock(Authentication.class);
        UserDetails userDetails = mock(UserDetails.class);
        UserEntity user = mock(UserEntity.class);

        when(authentication.getPrincipal()).thenReturn(userDetails);
        when(userDetails.getUsername()).thenReturn("validUser");
        when(userRepository.findByUsername("validUser")).thenReturn(Optional.of(user));
        when(passwordEncoder.matches("wrongCurrentPassword", user.getPassword())).thenReturn(false);

        assertThrows(PasswordMismatchException.class, () -> {
            authService.changePassword(request, authentication);
        });
    }

    @Test
    public void changePassword_Failure_PasswordsDoNotMatch() {
        ChangePasswordRequest request = new ChangePasswordRequest("currentPassword", "newPassword", "differentNewPassword");
        Authentication authentication = mock(Authentication.class);
        UserDetails userDetails = mock(UserDetails.class);
        UserEntity user = mock(UserEntity.class);

        when(authentication.getPrincipal()).thenReturn(userDetails);
        when(userDetails.getUsername()).thenReturn("validUser");
        when(userRepository.findByUsername("validUser")).thenReturn(Optional.of(user));
        when(passwordEncoder.matches("currentPassword", user.getPassword())).thenReturn(true);

        assertThrows(PasswordMismatchException.class, () -> {
            authService.changePassword(request, authentication);
        });
    }



}

