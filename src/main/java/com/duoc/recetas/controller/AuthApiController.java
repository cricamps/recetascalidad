package com.duoc.recetas.controller;

import com.duoc.recetas.dto.LoginRequest;
import com.duoc.recetas.dto.LoginResponse;
import com.duoc.recetas.security.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

/**
 * API REST de autenticación JWT.
 *
 * POST /api/auth/login   →  { "username": "...", "password": "..." }
 *                         ←  { "token": "...", "username": "...", "roles": "...", "expiresIn": ... }
 *
 * Uso del token:
 *   GET /api/recetas
 *   Header:  Authorization: Bearer <token>
 */
@RestController
@RequestMapping("/api")
public class AuthApiController {

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    private UserDetailsService userDetailsService;

    @Value("${app.jwt.expiration}")
    private long expirationMs;

    @PostMapping("/auth/login")
    public ResponseEntity<?> login(@RequestBody LoginRequest request) {
        try {
            authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.getUsername(), request.getPassword())
            );
        } catch (BadCredentialsException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(Map.of("error", "Credenciales incorrectas"));
        }

        UserDetails userDetails = userDetailsService.loadUserByUsername(request.getUsername());
        String token = jwtUtil.generarToken(userDetails);
        String roles = userDetails.getAuthorities().toString();

        return ResponseEntity.ok(new LoginResponse(token, request.getUsername(), roles, expirationMs));
    }

    /** Ejemplo de endpoint privado que requiere JWT */
    @GetMapping("/recetas")
    public ResponseEntity<?> recetasPrivadas() {
        return ResponseEntity.ok(Map.of(
            "mensaje", "Acceso autorizado con JWT",
            "info", "Este endpoint solo es accesible con un token válido"
        ));
    }

    /** Información del usuario autenticado */
    @GetMapping("/me")
    public ResponseEntity<?> me(org.springframework.security.core.Authentication auth) {
        return ResponseEntity.ok(Map.of(
            "username", auth.getName(),
            "roles", auth.getAuthorities().toString()
        ));
    }
}
