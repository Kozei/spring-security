package com.demo.demo.security.service;

import java.util.Map;

import jakarta.servlet.http.HttpServletRequest;

import org.springframework.security.core.userdetails.UserDetails;

import com.demo.demo.domain.AppUser;

public interface JwtService {

    String generateToken(UserDetails userDetails);
    String extractToken(HttpServletRequest request);
    void saveToken(String token, AppUser user);

    boolean isTokenValid(String token, AppUser user);
    boolean isTokenExpired(String token);

    Map<String, Object> decodeToken(String token);

}
