package com.demo.demo.security.authentication;

import static com.demo.demo.util.MessageConstants.ACCESS_DENIED;
import static com.demo.demo.util.MessageConstants.LOGIN_FAILED;

import java.io.IOException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import org.springframework.http.HttpStatus;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;

import com.demo.demo.exception.MalformedJwtAuthenticationException;
import com.demo.demo.util.ResourceUtil;

/**
 * Centralized exception handling within the security chain decoupled from the business layer.
 * This entry point is designed to handle authentication errors. Every call that fails to authenticate
 * will be rejected here early, before reaching the API layer.
 */
public class AuthenticationFailureHandler implements AuthenticationEntryPoint {

    private final ResourceUtil resourceUtil;

    public AuthenticationFailureHandler(ResourceUtil resourceUtil) {
        this.resourceUtil = resourceUtil;
    }

    @Override
    public void commence(HttpServletRequest request,
                         HttpServletResponse response,
                         AuthenticationException authException) throws IOException {

        if (resourceUtil.attemptToLogin(request)) {
            resourceUtil.sendErrorResponse(request, response, authException, LOGIN_FAILED, HttpStatus.UNAUTHORIZED);
        }

        if (resourceUtil.attemptToAccessPrivateResource(request)) {
            resourceUtil.sendErrorResponse(request, response, authException, ACCESS_DENIED, HttpStatus.FORBIDDEN);
        }

        if (authException instanceof MalformedJwtAuthenticationException) {
           resourceUtil.sendErrorResponse(request, response, authException, ACCESS_DENIED, HttpStatus.UNAUTHORIZED);
        }
    }
}