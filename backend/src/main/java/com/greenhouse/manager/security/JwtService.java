/*
 * Proyecto: GreenHouse Manager
 * Archivo: JwtService.java
 * Descripcion: Utilidad para generacion y validacion de JWT.
 * Autor: Equipo GreenHouse Manager
 * Fecha: 2026-05-02
 * Version: 1.0.0
 */
package com.greenhouse.manager.security;

import com.greenhouse.manager.domain.entity.Usuario;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.util.Date;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

/**
 * Service for creating and validating JSON Web Tokens.
 */
@Service
public class JwtService {

    private final String secret;
    private final long expirationMinutes;

    /**
     * Creates a new JwtService.
     *
     * @param secret jwt secret
     * @param expirationMinutes expiration in minutes
     */
    public JwtService(
        @Value("${security.jwt.secret}") String secret,
        @Value("${security.jwt.expiration-minutes:60}") long expirationMinutes
    ) {
        this.secret = secret;
        this.expirationMinutes = expirationMinutes;
    }

    /**
     * Generates a JWT for a user.
     *
     * @param usuario user entity
     * @return jwt token
     */
    public String generateToken(Usuario usuario) {
        Date now = new Date();
        Date expiryDate = new Date(now.getTime() + expirationMinutes * 60L * 1000L);

        return Jwts.builder()
            .setSubject(usuario.getEmail())
            .claim("role", usuario.getRol().name())
            .setIssuedAt(now)
            .setExpiration(expiryDate)
            .signWith(getSigningKey(), SignatureAlgorithm.HS256)
            .compact();
    }

    /**
     * Validates a JWT token.
     *
     * @param token jwt token
     * @return true if valid
     */
    public boolean validateToken(String token) {
        try {
            parseClaims(token);
            return true;
        } catch (JwtException | IllegalArgumentException ex) {
            return false;
        }
    }

    /**
     * Extracts the username (email) from a token.
     *
     * @param token jwt token
     * @return username
     */
    public String getUsernameFromToken(String token) {
        return parseClaims(token).getSubject();
    }

    /**
     * Extracts the role from a token.
     *
     * @param token jwt token
     * @return role name
     */
    public String getRoleFromToken(String token) {
        return parseClaims(token).get("role", String.class);
    }

    private Claims parseClaims(String token) {
        return Jwts.parserBuilder()
            .setSigningKey(getSigningKey())
            .build()
            .parseClaimsJws(token)
            .getBody();
    }

    private Key getSigningKey() {
        return Keys.hmacShaKeyFor(secret.getBytes(StandardCharsets.UTF_8));
    }
}
