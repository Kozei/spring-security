package com.demo.demo.security.filter;

import java.io.IOException;
import java.time.Instant;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;

import com.demo.demo.error.ErrorResponse;
import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * Centralized exception handling at the security chain level.
 */
public class CustomAuthenticationEntryPoint implements AuthenticationEntryPoint {

    private static final String LOGIN_FAILED = "login failed. Please try again with valid credentials";

    private final ObjectMapper objectMapper;

    public CustomAuthenticationEntryPoint(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    @Override
    public void commence(HttpServletRequest request,
                         HttpServletResponse response,
                         AuthenticationException authException) throws IOException {


        var errorResponse = new ErrorResponse.Builder(Instant.now(),authException.getMessage())
                .status(HttpStatus.UNAUTHORIZED.value())
                .error(HttpStatus.UNAUTHORIZED.getReasonPhrase())
                .userMessage(LOGIN_FAILED)
                .path(request.getRequestURI()) //TODO: why path /error instead of /login is displayed.
                .build();

        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);

        String jsonResponse = objectMapper.writeValueAsString(errorResponse);
        response.getWriter().write(jsonResponse);
    }
}
