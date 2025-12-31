package com.ebank.authService.security;

import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.util.Date;
import java.util.List;

@Component
public class JwtTokenProvider {

    private final SecretKey secretKey;
    private final long expiration;

    public JwtTokenProvider(
            @Value("${jwt.secret}") String secret,
            @Value("${jwt.expiration}") long expiration
    ) {
        this.secretKey = Keys.hmacShaKeyFor(secret.getBytes());
        this.expiration = expiration;
    }

    public String generateToken(Authentication authentication) {

        System.out.println("Principal class = " + authentication.getPrincipal().getClass());
        System.out.println("Authorities = " + authentication.getAuthorities());

        List<String> roles;
        String email;

        Object principal = authentication.getPrincipal();
        if (principal instanceof com.ebank.authService.dto.UserDetailsDto user) {
            email = user.getEmail();   // or user.getUsername() if email is username
        } else {
            email = authentication.getName();
        }
        if (principal instanceof com.ebank.authService.dto.UserDetailsDto user) {

            roles = user.getRoles()
                    .stream()
                    .map(r -> r.getName())
                    .toList();
        } else {
            roles = authentication.getAuthorities()
                    .stream()
                    .map(a -> a.getAuthority())
                    .filter(a -> a.startsWith("ROLE_"))
                    .map(a -> a.substring(5))
                    .toList();
        }

        List<String> permissions = authentication.getAuthorities()
                .stream()
                .map(a -> a.getAuthority())
                .filter(a -> !a.startsWith("ROLE_"))
                .toList();

        return Jwts.builder()
                .setSubject(email)
                .claim("roles", roles)
                .claim("permissions", permissions)
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + expiration))
                .signWith(secretKey, SignatureAlgorithm.HS256)
                .compact();
    }

    public String extractUsername(String token) {
        return parseClaims(token).getSubject();
    }

    public boolean validateToken(String token) {
        try {
            parseClaims(token);
            return true;
        } catch (JwtException | IllegalArgumentException e) {
            return false;
        }
    }

    @SuppressWarnings("unchecked")
    public List<String> extractRoles(String token) {
        return parseClaims(token).get("roles", List.class);
    }

    private Claims parseClaims(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(secretKey)
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

    @SuppressWarnings("unchecked")
    public List<String> extractPermissions(String token) {
        return parseClaims(token).get("permissions", List.class);
    }

}
