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
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.filter.OncePerRequestFilter;

import com.demo.demo.security.authentication.JwtAuthenticationToken;
import com.demo.demo.security.manager.RestAuthenticationManager;
import com.demo.demo.security.service.JwtService;
import com.demo.demo.security.service.RestUserDetailsService;
import com.demo.demo.util.ResourceUtil;

/**
 * Centralized security logic. Rejects invalid or unauthenticated
 * calls early before current request reaches the controller layer.
 */
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final RestAuthenticationManager restAuthenticationManager;
    private final JwtService jwtService;
    private final RestUserDetailsService restUserDetailsService;
    private final ResourceUtil resourceUtil;

    public JwtAuthenticationFilter(RestAuthenticationManager restAuthenticationManager, JwtService jwtService, RestUserDetailsService restUserDetailsService, ResourceUtil resourceUtil) {
        this.restAuthenticationManager = restAuthenticationManager;
        this.jwtService = jwtService;
        this.restUserDetailsService = restUserDetailsService;
        this.resourceUtil = resourceUtil;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {

        if (resourceUtil.attemptToLogin(request)) {
            Authentication authentication = attemptAuthentication(request);
            if (isAuthenticated(authentication)){
                setAuthenticationToContext(authentication);
            }
        }

        if (resourceUtil.attemptToAccessPrivateResource(request)) {
            String extractedToken = request.getHeader("Authorization");

            if (extractedToken == null || !extractedToken.startsWith("Bearer ")) {
                response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Token not Valid");
                return;
            }

            String token = jwtService.trimToken(extractedToken);

            jwtService.checkTokenSemantics(token);

            if (jwtService.isTokenExpired(token)) {
                response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Token is Expired");
                return;
            }

            String username = jwtService.extractUsername(token);

            if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {
                UserDetails userPrincipal = restUserDetailsService.loadUserByUsername(username);

                if (jwtService.isTokenValid(token, userPrincipal)) { // does token subject match loaded username
                    JwtAuthenticationToken authentication = new JwtAuthenticationToken(userPrincipal,
                            null,
                            true,
                            userPrincipal.getAuthorities()); //TODO: or get the authorities from the jwt payload

                    setAuthenticationToContext(authentication);
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
        String username = request.getParameter("username");
        String password = request.getParameter("password");
        Authentication authentication;

        var customAuthentication = new JwtAuthenticationToken(username, password, false, Collections.emptyList());

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
    private boolean isAuthenticated(@NotNull Authentication authentication) {
        return authentication.isAuthenticated();
    }

    /**
     * @Desc Sets the context with the authentication object.
     * The authentication object will be used for Authorization.
     * @param authentication
     */
    private void setAuthenticationToContext(@NotNull Authentication authentication) {
        SecurityContext securityContext = SecurityContextHolder.createEmptyContext();
        securityContext.setAuthentication(authentication);
        SecurityContextHolder.setContext(securityContext);
    }

}