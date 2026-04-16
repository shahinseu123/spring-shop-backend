package com.shop.shop.authenticate.filter;

import com.shop.shop.authenticate.service.AuthenticateService;
import com.shop.shop.authenticate.util.JwtUtil;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Slf4j
@Configuration
@RequiredArgsConstructor
public class AuthFilter extends OncePerRequestFilter {

    private static final String BEARER="Bearer ";

    private final JwtUtil jwtUtil;
    private final AuthenticateService authenticateService;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        String clientSecret = request.getHeader("clientsecret");
        String token = getToken(request);
        UserDetails user;
        String endpoint = request.getRequestURI();
        String method = request.getMethod();
        log.info("Request  received API endpoint: {}; Method: {}", endpoint, method);
        if(StringUtils.hasText(clientSecret)){
            user = authenticateService.loadUserByClientSecret(clientSecret);
            UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken = new UsernamePasswordAuthenticationToken(
                    user, null, user.getAuthorities());
            usernamePasswordAuthenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
            SecurityContextHolder.getContext().setAuthentication(usernamePasswordAuthenticationToken);
        }
        if(StringUtils.hasText(token)){
            String username = jwtUtil.getUsernameFromToken(token);
            user = authenticateService.loadUserByUsername(username);

            if(StringUtils.hasText(username) && Boolean.TRUE.equals(jwtUtil.validateToken(token,user))){
                UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken = new UsernamePasswordAuthenticationToken(
                        user, null, user.getAuthorities());
                usernamePasswordAuthenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                SecurityContextHolder.getContext().setAuthentication(usernamePasswordAuthenticationToken);
            }
        }

        if(!StringUtils.hasText(clientSecret) && !StringUtils.hasText(token)){
            log.error("Request missing authentication");
        }

        filterChain.doFilter(request, response);
    }

    private String getToken(HttpServletRequest request){
        String bearerToken = request.getHeader("Authorization");
        if(StringUtils.hasText(bearerToken) && bearerToken.startsWith(BEARER)){
            return bearerToken.replace(BEARER,"");
        }
        return null;
    }
}
