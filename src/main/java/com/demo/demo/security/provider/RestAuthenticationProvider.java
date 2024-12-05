package com.demo.demo.security.provider;

import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.demo.demo.security.authentication.JwtAuthenticationToken;
import com.demo.demo.security.service.RestUserDetailsService;

public class RestAuthenticationProvider implements AuthenticationProvider {

    private final RestUserDetailsService restUserDetailsService;
    private final PasswordEncoder passwordEncoder;

    public RestAuthenticationProvider(RestUserDetailsService restUserDetailsService, PasswordEncoder passwordEncoder) {
        this.restUserDetailsService = restUserDetailsService;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {

        String username = authentication.getName();
        String password = authentication.getCredentials().toString();

        UserDetails userPrincipal = restUserDetailsService.loadUserByUsername(username);

        if (passwordEncoder.matches(password, userPrincipal.getPassword())) {
            return new JwtAuthenticationToken(userPrincipal, password, true, userPrincipal.getAuthorities());
        }

        throw new BadCredentialsException("Bad credentials");
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return authentication.equals(JwtAuthenticationToken.class);
    }

}
