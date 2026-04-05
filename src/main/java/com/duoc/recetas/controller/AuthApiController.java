package com.duoc.recetas.controller;

import com.duoc.recetas.dto.LoginRequest;
import com.duoc.recetas.dto.LoginResponse;
import com.duoc.recetas.security.JwtUtil;
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

@RestController
@RequestMapping("/api")
public class AuthApiController {

    private final AuthenticationManager authenticationManager;
    private final JwtUtil jwtUtil;
    private final UserDetailsService userDetailsService;

    @Value("${app.jwt.expiration}")
    private long expirationMs;

    public AuthApiController(AuthenticationManager authenticationManager,
                              JwtUtil jwtUtil,
                              UserDetailsService userDetailsService) {
        this.authenticationManager = authenticationManager;
        this.jwtUtil = jwtUtil;
        this.userDetailsService = userDetailsService;
    }

    @PostMapping("/auth/login")
    public ResponseEntity<Object> login(@RequestBody LoginRequest request) {
        try {
            authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                    request.getUsername(), request.getPassword())
            );
        } catch (BadCredentialsException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(Map.of("error", "Credenciales incorrectas"));
        }

        UserDetails userDetails =
            userDetailsService.loadUserByUsername(request.getUsername());
        String token = jwtUtil.generarToken(userDetails);
        String roles = userDetails.getAuthorities().toString();

        return ResponseEntity.ok(
            new LoginResponse(token, request.getUsername(), roles, expirationMs));
    }

    @GetMapping("/me")
    public ResponseEntity<Map<String, String>> me(
            org.springframework.security.core.Authentication auth) {
        return ResponseEntity.ok(Map.of(
            "username", auth.getName(),
            "roles",    auth.getAuthorities().toString()
        ));
    }
}
