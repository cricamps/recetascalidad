package com.duoc.recetas.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.util.Date;

@Component
public class JwtUtil {

    @Value("${app.jwt.secret}")
    private String secret;

    @Value("${app.jwt.expiration}")
    private long expirationMs;

    /** Factor de renovación: refresh válido hasta 7× el tiempo de expiración */
    private static final long REFRESH_FACTOR = 7L;

    private SecretKey getKey() {
        return Keys.hmacShaKeyFor(secret.getBytes(StandardCharsets.UTF_8));
    }

    public String generarToken(UserDetails userDetails) {
        return buildToken(userDetails.getUsername(),
                userDetails.getAuthorities().toString(),
                expirationMs);
    }

    /**
     * Genera un refresh token con tiempo de vida = expirationMs × REFRESH_FACTOR.
     * Incluye claim "type=refresh" para distinguirlo del access token.
     */
    public String generarRefreshToken(UserDetails userDetails) {
        return Jwts.builder()
                .subject(userDetails.getUsername())
                .claim("roles", userDetails.getAuthorities().toString())
                .claim("type", "refresh")
                .issuedAt(new Date())
                .expiration(new Date(System.currentTimeMillis() + expirationMs * REFRESH_FACTOR))
                .signWith(getKey())
                .compact();
    }

    private String buildToken(String username, String roles, long ttlMs) {
        return Jwts.builder()
                .subject(username)
                .claim("roles", roles)
                .issuedAt(new Date())
                .expiration(new Date(System.currentTimeMillis() + ttlMs))
                .signWith(getKey())
                .compact();
    }

    public String extraerUsername(String token) {
        return parsearClaims(token).getSubject();
    }

    public boolean validarToken(String token, UserDetails userDetails) {
        try {
            String username = extraerUsername(token);
            return username.equals(userDetails.getUsername()) && !tokenExpirado(token);
        } catch (JwtException | IllegalArgumentException e) {
            return false;
        }
    }

    /**
     * Valida que el token sea un refresh token válido (no expirado y con claim type=refresh).
     */
    public boolean validarRefreshToken(String token) {
        try {
            Claims claims = parsearClaims(token);
            boolean notExpired = !claims.getExpiration().before(new Date());
            boolean isRefresh  = "refresh".equals(claims.get("type", String.class));
            return notExpired && isRefresh;
        } catch (JwtException | IllegalArgumentException e) {
            return false;
        }
    }

    public long getExpirationMs() {
        return expirationMs;
    }

    private boolean tokenExpirado(String token) {
        return parsearClaims(token).getExpiration().before(new Date());
    }

    private Claims parsearClaims(String token) {
        return Jwts.parser()
                .verifyWith(getKey())
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }
}
