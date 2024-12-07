package com.demo.demo.util;

import static com.demo.demo.util.MessageConstants.LOGIN_API;
import static com.demo.demo.util.MessageConstants.ORIGINAL_PATH;
import static com.demo.demo.util.MessageConstants.POST_METHOD;
import static com.demo.demo.util.MessageConstants.PRIVATE_API;

import java.io.IOException;
import java.time.Instant;
import java.util.Optional;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.server.PathContainer;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.util.pattern.PathPattern;
import org.springframework.web.util.pattern.PathPatternParser;

import com.demo.demo.error.ErrorResponse;
import com.fasterxml.jackson.databind.ObjectMapper;

public class ResourceUtil {


    private final PathPatternParser pathPatternParser;
    private final ObjectMapper objectMapper;

    public ResourceUtil(PathPatternParser pathPatternParser, ObjectMapper objectMapper) {
        this.pathPatternParser = pathPatternParser;
        this.objectMapper = objectMapper;
    }

    /**
     * Checks if the request is of type login.
     * @param request
     * @return
     */
    public boolean attemptToLogin(HttpServletRequest request) {
        String path = getPath(request);
        return path.equals(LOGIN_API) && request.getMethod().equals(POST_METHOD);
    }


    /**
     * Checks if the call targets protected resource.
     * @param request
     * @return
     */
    public boolean attemptToAccessPrivateResource(HttpServletRequest request) {
        PathPattern privatePath = pathPatternParser.parse(PRIVATE_API);
        String path = getPath(request);
        PathContainer requestedPath = PathContainer.parsePath(path);

        return privatePath.matches(requestedPath);
    }

    /**
     * Gets the path that was called.
     * @param request
     * @return
     */
    private String getPath(HttpServletRequest request) {
        String path = (String) request.getAttribute(ORIGINAL_PATH);
        return Optional.ofNullable(path)
                .orElseThrow(() -> new IllegalStateException("Original request URI not captured. Check filter configuration."));
    }

    public void sendErrorResponse(HttpServletRequest request,
                                   HttpServletResponse response,
                                   AuthenticationException authException,
                                   String userMessage,
                                   HttpStatus status) throws IOException {

        var errorResponse = new ErrorResponse.Builder(Instant.now(), status.value(), authException.getMessage())
                .userMessage(userMessage)
                .path(getPath(request))
                .build();

        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        response.setStatus(status.value());

        String jsonResponse = objectMapper.writeValueAsString(errorResponse);
        response.getWriter().write(jsonResponse);
    }
}
