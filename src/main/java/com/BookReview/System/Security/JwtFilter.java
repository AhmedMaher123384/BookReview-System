package com.BookReview.System.Security;

import com.BookReview.System.Model.Entity.UserEntity;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

import static com.BookReview.System.Security.SecurityConstants.SECRET_KEY;

public class JwtFilter extends OncePerRequestFilter {
    @Autowired
    private JwtUtil jwtUtil;
    @Autowired
    private CustomeUserDetailsService userDetailsService;

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain)
            throws ServletException, IOException {

        final String header =  request.getHeader(SecurityConstants.TOKEN_HEADER);
        final SecurityContext securityContext= SecurityContextHolder.getContext();

        if(header!=null && securityContext.getAuthentication()==null){
            String token = header.substring("bearer ".length());
            String username = jwtUtil.extractUserName(token,SECRET_KEY);
            if(username!=null){
                UserDetails user =  userDetailsService.loadUserByUsername(username);
                if(jwtUtil.isAccessTokenValidate(token,user)){
                    UsernamePasswordAuthenticationToken authentication =
                            new UsernamePasswordAuthenticationToken
                                    (user, null, user.getAuthorities());
                authentication.setDetails(new WebAuthenticationDetailsSource()
                        .buildDetails(request));
                SecurityContextHolder.getContext().setAuthentication(authentication);
                }

            }

        }
        filterChain.doFilter(request,response);

    }


}
