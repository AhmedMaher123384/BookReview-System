package com.BookReview.System.Service;


import com.BookReview.System.Exception.TokenValidationException;
import com.BookReview.System.Model.Dto.Security.LogoutRequest;
import com.BookReview.System.Repository.AccessTokenBlackListRepository;
import com.BookReview.System.Repository.RefreshTokenBlackListRepository;
import com.BookReview.System.Security.CustomeUserDetailsService;
import com.BookReview.System.Security.JwtUtil;
import com.BookReview.System.Service.Impl.LogoutServiceImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class LogoutServiceTest {

    @Mock
    private JwtUtil jwtUtil;

    @Mock
    private CustomeUserDetailsService userDetailsService;

    @Mock
    private AccessTokenBlackListRepository accessTokenBlackListRepository;

    @Mock
    private RefreshTokenBlackListRepository refreshTokenBlackListRepository;

    @InjectMocks
    private LogoutServiceImpl logoutService;

    @Test
    public void logout_ValidTokens_Success() {
        LogoutRequest logoutRequest = new LogoutRequest("validAccessToken", "validRefreshToken");

        when(jwtUtil.isTokenValidate(any(), any(), any())).thenReturn(true);

        ResponseEntity<String> response = logoutService.logout(logoutRequest);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("Logout Success", response.getBody());
    }

    @Test
    public void logout_InvalidAccessToken_Failure() {
        LogoutRequest logoutRequest = new LogoutRequest("invalidAccessToken", "validRefreshToken");

        when(jwtUtil.isTokenValidate(any(), any(), any())).thenReturn(false);

        assertThrows(TokenValidationException.class, () -> {
            logoutService.logout(logoutRequest);
        });
    }

    @Test
    public void logout_InvalidRefreshToken_Failure() {
        LogoutRequest logoutRequest = new LogoutRequest("validAccessToken", "invalidRefreshToken");

        when(jwtUtil.isTokenValidate(eq("validAccessToken"), any(), any())).thenReturn(true);
        when(jwtUtil.isTokenValidate(eq("invalidRefreshToken"), any(), any())).thenReturn(false);

        assertThrows(TokenValidationException.class, () -> {
            logoutService.logout(logoutRequest);
        });
    }

}
