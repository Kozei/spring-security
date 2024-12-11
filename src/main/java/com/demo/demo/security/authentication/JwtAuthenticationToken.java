package com.demo.demo.security.authentication;

import java.util.Collection;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;

/**
 * Represents the authenticated user.
 */
public class JwtAuthenticationToken implements Authentication {

    private final Object principal;
    private Object password;
    private Boolean isAuthenticated;
    private final Collection<? extends GrantedAuthority> authorities;

    public JwtAuthenticationToken(Object principal, Object password, Boolean isAuthenticated, Collection<? extends GrantedAuthority> authorities) {
        this.principal = principal;
        this.password = password;
        this.isAuthenticated = isAuthenticated;
        this.authorities = authorities;
    }

    @Override
    public boolean isAuthenticated() {
        return this.isAuthenticated;
    }

    @Override
    public void setAuthenticated(boolean isAuthenticated) throws IllegalArgumentException {
        this.isAuthenticated = isAuthenticated;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return this.authorities;
    }

    @Override
    public Object getCredentials() {
        return this.password;
    }

    @Override
    public Object getDetails() {
        return null;
    }

    @Override
    public Object getPrincipal() {
        return this.principal;
    }

    @Override
    public String getName() {
        return this.principal.toString();
    }

    public void eraseCredentials() {
        this.password = null;
    }
}