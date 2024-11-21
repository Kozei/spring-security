package com.demo.demo.security.authentication;

import java.util.Collection;
import java.util.List;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Component;

@Component
public class CustomAuthentication implements Authentication {

    private Object principal;
    private Object password;
    private Collection<? extends GrantedAuthority> authorities;
    private Boolean isAuthenticated;

    public CustomAuthentication(Object principal, Object password, Boolean isAuthenticated, Collection<? extends GrantedAuthority> authorities) {
        this.principal = principal;
        this.password = password;
        this.isAuthenticated = isAuthenticated;
        this.authorities = authorities;
    }

    public CustomAuthentication(){

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
}
