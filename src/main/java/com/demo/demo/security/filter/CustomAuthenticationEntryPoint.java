package com.demo.demo.security.filter;

import static com.demo.demo.util.MessageConstants.ACCESS_DENIED;
import static com.demo.demo.util.MessageConstants.LOGIN_FAILED;

import java.io.IOException;
import java.time.Instant;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import org.modelmapper.spi.ErrorMessage;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;

import com.demo.demo.error.ErrorResponse;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * Centralized exception handling at the security chain level.
 */
public class CustomAuthenticationEntryPoint implements AuthenticationEntryPoint {

    private final ObjectMapper objectMapper;

    public CustomAuthenticationEntryPoint(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    @Override
    public void commence(HttpServletRequest request,
                         HttpServletResponse response,
                         AuthenticationException authException) throws IOException {

        if (isLoginRequest(request)) {
            sendErrorResponse(request, response, authException, LOGIN_FAILED, HttpStatus.UNAUTHORIZED);
        } else {
            sendErrorResponse(request, response, authException, ACCESS_DENIED, HttpStatus.FORBIDDEN);
        }
    }

    /**
     * Checks if the request is of type login.
     * @param request
     * @return
     */
    private boolean isLoginRequest(HttpServletRequest request) {
        String originalUri = (String) request.getAttribute("originalRequestUri");

        if (originalUri == null) {
            throw new IllegalStateException("Original request URI not captured. Check filter configuration.");
        }

        return originalUri.equals("/public/login") && request.getMethod().equals("POST");
    }

    private void sendErrorResponse(HttpServletRequest request,
                                   HttpServletResponse response,
                                   AuthenticationException authException,
                                   String userMessage,
                                   HttpStatus status) throws IOException {
         var errorResponse = new ErrorResponse.Builder(Instant.now(), status.value(), authException.getMessage())
                .userMessage(userMessage)
                .path(request.getRequestURI())
                .build();

        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        response.setStatus(status.value());

        String jsonResponse = objectMapper.writeValueAsString(errorResponse);
        response.getWriter().write(jsonResponse);
    }

}
