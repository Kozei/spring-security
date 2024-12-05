package com.demo.demo.security.service.impl;

import static com.demo.demo.util.MessageConstants.SECRET_KEY;

import java.security.Key;
import java.util.Date;
import java.util.Map;

import jakarta.servlet.http.HttpServletRequest;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import com.demo.demo.domain.AppUser;
import com.demo.demo.security.service.JwtService;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;

@Service
public class JwtServiceImpl implements JwtService {

    @Override
    public String generateToken(UserDetails userDetails) {
        return Jwts.builder()
                .subject(userDetails.getUsername())
                .claim("authorities", userDetails.getAuthorities())
                .issuedAt(new Date(System.currentTimeMillis()))
                .expiration(new Date(System.currentTimeMillis() + 1000 * 60)) //1 minute
                .signWith(getSignInKey())
                .compact();
    }

    @Override
    public String extractToken(HttpServletRequest request) {
        String token = request.getHeader("Authorization");
        if (token != null && token.startsWith("Bearer ")) {
            return token.substring(7);
        } else throw new RuntimeException();
    }

    private Key getSignInKey() {
        byte[] keyBytes = Decoders.BASE64.decode(SECRET_KEY);
        return Keys.hmacShaKeyFor(keyBytes);
    }

    @Override
    public void saveToken(String token, AppUser user) {

    }

    @Override
    public boolean isTokenValid(String token, AppUser user) {
        return false;
    }

    @Override
    public boolean isTokenExpired(String token) {
        return false;
    }

    @Override
    public Map<String, Object> decodeToken(String token) {
        return Map.of();
    }

}
