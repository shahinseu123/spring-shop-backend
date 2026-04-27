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
import java.util.Arrays;
import java.util.List;

@Slf4j
@Configuration
@RequiredArgsConstructor
public class AuthFilter extends OncePerRequestFilter {

    private static final String BEARER = "Bearer ";

    // Public endpoints that don't require authentication
    private static final List<String> PUBLIC_GET_ENDPOINTS = Arrays.asList(
            "/api/v1/products",
            "/api/v1/categories",
            "/api/v1/brands",
            "/uploads",
            "/upload"
    );

    private static final List<String> PUBLIC_POST_ENDPOINTS = Arrays.asList(
            "/authenticate",
            "/users/register"
    );

    private final JwtUtil jwtUtil;
    private final AuthenticateService authenticateService;

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {
        String clientSecret = request.getHeader("clientsecret");
        String token = getToken(request);
        String endpoint = request.getRequestURI();
        String method = request.getMethod();

        log.info("Request received API endpoint: {}; Method: {}", endpoint, method);

        // Check if this is a public endpoint that doesn't need auth
        boolean isPublicGet = method.equals("GET") && PUBLIC_GET_ENDPOINTS.stream().anyMatch(endpoint::startsWith);
        boolean isPublicPost = method.equals("POST") && PUBLIC_POST_ENDPOINTS.stream().anyMatch(endpoint::startsWith);

        if (isPublicGet || isPublicPost) {
            log.info("Public endpoint access granted without auth");
            filterChain.doFilter(request, response);
            return;
        }

        // For POST to products, require authentication
        boolean isCreateProduct = endpoint.equals("/api/v1/products") && method.equals("POST");

        // Authenticate the request
        boolean isAuthenticated = false;

        if (StringUtils.hasText(clientSecret)) {
            UserDetails user = authenticateService.loadUserByClientSecret(clientSecret);
            UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(
                    user, null, user.getAuthorities());
            authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
            SecurityContextHolder.getContext().setAuthentication(authToken);
            isAuthenticated = true;
            log.info("Authenticated via client secret");
        }

        if (StringUtils.hasText(token)) {
            String username = jwtUtil.getUsernameFromToken(token);
            UserDetails user = authenticateService.loadUserByUsername(username);

            if (StringUtils.hasText(username) && Boolean.TRUE.equals(jwtUtil.validateToken(token, user))) {
                UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(
                        user, null, user.getAuthorities());
                authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                SecurityContextHolder.getContext().setAuthentication(authToken);
                isAuthenticated = true;
                log.info("Authenticated via JWT token");
            }
        }

        // Block unauthenticated POST requests to products
        if (isCreateProduct && !isAuthenticated) {
            log.error("Authentication required for POST to {}", endpoint);
            response.setStatus(HttpServletResponse.SC_FORBIDDEN);
            response.setContentType("application/json");
            response.getWriter().write("{\"error\": \"Authentication required for product creation\"}");
            return;
        }

        // For other endpoints, allow but log warning
        if (!isAuthenticated) {
            log.warn("Request missing authentication for endpoint: {} {}", method, endpoint);
        }

        filterChain.doFilter(request, response);
    }

    private String getToken(HttpServletRequest request) {
        String bearerToken = request.getHeader("Authorization");
        if (StringUtils.hasText(bearerToken) && bearerToken.startsWith(BEARER)) {
            return bearerToken.replace(BEARER, "");
        }
        return null;
    }
}