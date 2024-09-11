package com.mercadona.api.config;

import com.mercadona.api.services.JWTService;
import com.mercadona.api.services.MyUserDetailsService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

import static com.mercadona.api.constants.ApiConstants.AUTH_HEADER_IDX;
import static com.mercadona.api.constants.ApiConstants.BEARER_TOKEN_AUTH_TYPE;
import static org.springframework.http.HttpHeaders.AUTHORIZATION;

/**
 * JwtFilter is a custom security filter that intercepts HTTP requests to extract
 * and validate JWT tokens for authentication. The filter is executed once per request.
 */
@Component
public class JwtFilter extends OncePerRequestFilter {

    private final JWTService jwtService;
    private final MyUserDetailsService userDetailsService;

    /**
     * Constructor-based dependency injection for services.
     *
     * @param jwtService         the service for handling JWT operations
     * @param userDetailsService the service for loading user details
     */
    public JwtFilter(JWTService jwtService, MyUserDetailsService userDetailsService) {
        this.jwtService = jwtService;
        this.userDetailsService = userDetailsService;
    }

    /**
     * Intercepts incoming HTTP requests, checks for a valid JWT token in the Authorization header,
     * and sets up the security context for authenticated users.
     *
     * @param request     the incoming HTTP request
     * @param response    the HTTP response
     * @param filterChain the chain of filters
     * @throws ServletException in case of Servlet-related errors
     * @throws IOException      in case of I/O errors
     */
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        // Extract the Authorization header
        String authHeader = request.getHeader(AUTHORIZATION);
        String token = null;
        String username = null;

        // Check if the Authorization header starts with "Bearer "
        if (authHeader != null && authHeader.startsWith(BEARER_TOKEN_AUTH_TYPE)) {
            token = authHeader.substring(AUTH_HEADER_IDX); // Extract the JWT token
            username = jwtService.extractUserName(token); // Extract the username from the token
        }

        // Authenticate the user if the username is valid and no existing authentication is present
        if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {
            UserDetails userDetails = userDetailsService.loadUserByUsername(username);

            // Validate the token and set the security context
            if (jwtService.validateToken(token, userDetails)) {
                UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(
                        userDetails, null, userDetails.getAuthorities());
                authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

                // Set the authentication in the security context
                SecurityContextHolder.getContext().setAuthentication(authToken);
            }
        }

        // Continue with the filter chain
        filterChain.doFilter(request, response);
    }
}
