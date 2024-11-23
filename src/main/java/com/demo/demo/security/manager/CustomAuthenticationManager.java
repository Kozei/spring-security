package com.demo.demo.security.manager;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationServiceException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;

import com.demo.demo.security.provider.CustomAuthenticationProvider;

public class CustomAuthenticationManager implements AuthenticationManager {

    private final CustomAuthenticationProvider provider;

    public CustomAuthenticationManager(CustomAuthenticationProvider provider) {
        this.provider = provider;
    }

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {

        if (provider.supports(authentication.getClass()))  {
            return provider.authenticate(authentication);
        }

        throw new AuthenticationServiceException("Unsupported Authentication Method");
    }
}
