package com.sbnavneet.projects.ai_app_builder.security;

import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Date;

import javax.crypto.SecretKey;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.AuthenticationCredentialsNotFoundException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import com.sbnavneet.projects.ai_app_builder.entity.User;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;

@Component
public class AuthUtility {

    @Value("${jwt.secret-key}")
    private String SECRET_KEY;

    public SecretKey getSecretKey(){
        return Keys.hmacShaKeyFor(SECRET_KEY.getBytes(StandardCharsets.UTF_8));
    }


    public String generateAccessToken(User user){
        return Jwts.builder()
                .signWith(getSecretKey())
                .subject(user.getUsername())
                .issuedAt(new Date())
                .expiration(new Date(System.currentTimeMillis() + 1000*60*10))
                .claim("userId", user.getId().toString())
                .claim("tokenType", "access")
                .claim("roles", new ArrayList<>())
                .compact();
    }

    public String generateRefreshToken(User user){
        return Jwts.builder()
                .signWith(getSecretKey())
                .subject(user.getUsername())
                .issuedAt(new Date())
                .expiration(new Date(System.currentTimeMillis() + 1000L * 60 * 60 * 24 * 7))
                .claim("userId", user.getId().toString())
                .claim("tokenType", "refresh")
                .claim("roles", new ArrayList<>())
                .compact();
    }

    public String validateRefreshToken(String token){
        var claims = Jwts.parser()
                .verifyWith(getSecretKey())
                .build()
                .parseSignedClaims(token)
                .getPayload();

        String tokenType = claims.get("tokenType", String.class);
        if (!"refresh".equals(tokenType)) {
            throw new IllegalArgumentException("Invalid token type: expected refresh token");
        }
        return claims.getSubject();
    }

    public JwtUserPrincipal validateAccessToken(String token){
        var claims = Jwts.parser()
                .verifyWith(getSecretKey())
                .build()
                .parseSignedClaims(token)
                .getPayload();

        String tokenType = claims.get("tokenType", String.class);
        if (!"access".equals(tokenType)) {
            throw new IllegalArgumentException("Invalid token type: expected access token");
        }

        Long userId = Long.parseLong(claims.get("userId", String.class));
        String username = claims.getSubject();
        return new JwtUserPrincipal(userId, username, new ArrayList<>());
    }

    public Long getCurrentUser(){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if(authentication == null || !(authentication.getPrincipal() instanceof JwtUserPrincipal)){
            throw new AuthenticationCredentialsNotFoundException("No JWT Found");
        }
        JwtUserPrincipal jwtUserPrincipal = (JwtUserPrincipal) authentication.getPrincipal();
        return jwtUserPrincipal.userId();
    }
}
