package com.demo.demo.security.filter;

import java.io.IOException;
import java.util.Collections;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import com.demo.demo.security.authentication.JwtAuthenticationToken;
import com.demo.demo.security.manager.RestAuthenticationManager;
import com.demo.demo.security.service.RestUserDetailsService;
import com.demo.demo.util.JwtUtil;
import com.demo.demo.util.ResourceUtil;

/**
 * Centralized security logic. Rejects invalid or unauthenticated
 * calls early before current request reaches the controller layer.
 */
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final RestAuthenticationManager restAuthenticationManager;
    private final JwtUtil jwtUtil;
    private final RestUserDetailsService restUserDetailsService;
    private final ResourceUtil resourceUtil;

    private static final String AUTHORIZATION_HEADER = "Authorization";
    private static final String BEARER_PREFIX = "Bearer ";
    private static final String USERNAME = "username";
    private static final String PASSWORD = "password";

    public JwtAuthenticationFilter(RestAuthenticationManager restAuthenticationManager, JwtUtil jwtUtil, RestUserDetailsService restUserDetailsService, ResourceUtil resourceUtil) {
        this.restAuthenticationManager = restAuthenticationManager;
        this.jwtUtil = jwtUtil;
        this.restUserDetailsService = restUserDetailsService;
        this.resourceUtil = resourceUtil;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {
        final var loginRequest = resourceUtil.attemptToLogin(request);
        final var privateResourceAccessRequest = resourceUtil.attemptToAccessPrivateResource(request);

        if (Boolean.TRUE.equals(loginRequest)) {
            final var authentication = attemptAuthentication(request);
            if (isAuthenticated(authentication)){
                setAuthenticationToContext(authentication);
            }
        }

        if (Boolean.TRUE.equals(privateResourceAccessRequest)) {
            final var token = request.getHeader(AUTHORIZATION_HEADER);

            if (jwtUtil.isExtractedTokenValid(token, BEARER_PREFIX)) {
                final var tokenWithoutPrefix = jwtUtil.trimToken(token);
                final var username = jwtUtil.extractUsername(tokenWithoutPrefix);

                if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {
                    final var userPrincipal = restUserDetailsService.loadUserByUsername(username);

                    if (jwtUtil.isTokenSubjectValid(tokenWithoutPrefix, userPrincipal)) {
                        final var authentication = new JwtAuthenticationToken(userPrincipal, null, true, userPrincipal.getAuthorities()); //TODO: get authorities from the jwt payload
                        setAuthenticationToContext(authentication);
                    }
                }
            }
        }
        filterChain.doFilter(request, response);
    }

    /**
     * Attempts  to authenticate the user.
     * @param request
     */
    private Authentication attemptAuthentication(HttpServletRequest request) {
        final var username = request.getParameter(USERNAME);
        final var password = request.getParameter(PASSWORD);
        final Authentication authentication;

        final var customAuthentication = new JwtAuthenticationToken(username, password, false, Collections.emptyList());

        authentication = restAuthenticationManager.authenticate(customAuthentication);

        if (isAuthenticated(authentication) && authentication instanceof JwtAuthenticationToken) {
            customAuthentication.eraseCredentials();
        }
        return authentication;
    }

    /**
     * @Desc checks if authentication is successful.
     * @param authentication
     */
    private boolean isAuthenticated(Authentication authentication) {
        return authentication.isAuthenticated();
    }

    /**
     * @Desc Sets the context with the authentication object.
     * The authentication object will be used for Authorization.
     * @param authentication
     */
    private void setAuthenticationToContext(Authentication authentication) {
        SecurityContext securityContext = SecurityContextHolder.createEmptyContext();
        securityContext.setAuthentication(authentication);
        SecurityContextHolder.setContext(securityContext);
    }
}