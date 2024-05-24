package com.BookReview.System.Security;

import com.BookReview.System.Model.Entity.UserEntity;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import io.jsonwebtoken.security.Keys;
import java.security.Key;

import static com.BookReview.System.Security.SecurityConstants.*;

@Component
public class JwtUtil {

    public String generateToken(UserDetails user) {
        return createToken(user, ACCESS_TOKEN_EXPIRATION, SECRET_KEY);
    }

    public String generateRefreshToken(UserDetails user) {
        return createToken(user, REFRESH_TOKEN_EXPIRATION, REFRESH_SECRET_KEY);
    }

    private String createToken(UserDetails user, Long expiration, byte[] key) {
        Map<String, Object> claims = new HashMap<>();
        claims.put("sub", user.getUsername());
        claims.put("created", new Date());
        return Jwts.builder()
                .setClaims(claims)
                .setExpiration(new Date(System.currentTimeMillis() + expiration))
                .signWith(SignatureAlgorithm.HS512, key)
                .compact();
    }


    private Claims extractAllClaims(String token,byte[] key){
        return Jwts.parser().setSigningKey(key).parseClaimsJws(token).getBody();
    }

    private  Date extractExpirationDate(String token,byte[] key){
        return extractAllClaims(token,key).getExpiration();
    }

    public String extractUserName(String token,byte[] key) {
        return extractAllClaims(token,key).getSubject();
    }

    private Boolean isTokenExpired(String token,byte[] key){
        return extractExpirationDate(token,key).before(new Date());
    }

    public Boolean isTokenValidate(String token ,UserDetails user,byte[] key) {
        final String tokenName = extractUserName(token, key);
              String userName  = user.getUsername();
        return (!isTokenExpired(token,key) && tokenName.equals(userName));
    }

    public Boolean isAccessTokenValidate(String token ,UserDetails user) {
        return isTokenValidate(token, user,SECRET_KEY);
    }

    public Boolean isRefreshTokenValidate(String token ,UserDetails user) {
        return isTokenValidate(token, user,REFRESH_SECRET_KEY);
    }


}
