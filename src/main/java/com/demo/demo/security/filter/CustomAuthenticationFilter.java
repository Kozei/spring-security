package com.demo.demo.security.filter;

import java.io.IOException;
import java.util.Collections;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.constraints.NotNull;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import com.demo.demo.security.authentication.CustomAuthentication;
import com.demo.demo.security.manager.CustomAuthenticationManager;

/**
 * Centralized security logic. Rejects invalid or unauthenticated
 * calls early before current request reaches the controller layer.
 */
public class CustomAuthenticationFilter extends OncePerRequestFilter {

    private final CustomAuthenticationManager customAuthenticationManager;

    public CustomAuthenticationFilter(CustomAuthenticationManager customAuthenticationManager) {
        this.customAuthenticationManager = customAuthenticationManager;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {
        if (isLoginRequest(request)) {
            Authentication authentication = attemptAuthentication(request);
            if (isAuthenticated(authentication)){
                setAuthenticationToContext(authentication);
            }
        }
        filterChain.doFilter(request, response);
    }

    /**
     * Checks if the request is of type login.
     * @param request
     * @return
     */
    private boolean isLoginRequest(HttpServletRequest request) {
        return request.getRequestURI().equals("/login") && request.getMethod().equals("POST");
    }

    /**
     * Attempts  to authenticate the user.
     * @param request
     */
    private Authentication attemptAuthentication(HttpServletRequest request) {
        String username = request.getParameter("username");
        String password = request.getParameter("password");

        //TODO: next iteration extract username and password from json payload. Implement builder design.
        var customAuthentication = new CustomAuthentication(username, password, false, Collections.emptyList());

        Authentication authentication = customAuthenticationManager.authenticate(customAuthentication);

        if (authentication instanceof CustomAuthentication) {
            customAuthentication.eraseCredentials();
        }

        return authentication;
    }

    /**
     * @Desc checks if authentication is successful.
     * @param authentication
     */
    private boolean isAuthenticated(@NotNull Authentication authentication) {
        return authentication.isAuthenticated();
    }

    /**
     * @Desc Sets the context with the authentication object.
     * The authentication object will be used for Authorization.
     * @param authentication
     */
    private void setAuthenticationToContext(@NotNull Authentication authentication) {
        SecurityContext context = SecurityContextHolder.createEmptyContext();
        context.setAuthentication(authentication);
        SecurityContextHolder.setContext(context);
    }

}