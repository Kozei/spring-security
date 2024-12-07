package com.demo.demo.util;

import static com.demo.demo.util.MessageConstants.ORIGINAL_PATH;
import static com.demo.demo.util.MessageConstants.PRIVATE_API;

import java.util.Optional;
import jakarta.servlet.http.HttpServletRequest;

import org.springframework.http.server.PathContainer;
import org.springframework.web.util.pattern.PathPattern;
import org.springframework.web.util.pattern.PathPatternParser;

public class Resource {

    private final PathPatternParser pathPatternParser;

    public Resource(PathPatternParser pathPatternParser) {
        this.pathPatternParser = pathPatternParser;
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
}
