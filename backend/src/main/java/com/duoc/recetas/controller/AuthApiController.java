package com.duoc.recetas.controller;

import com.duoc.recetas.dto.LoginRequest;
import com.duoc.recetas.security.JwtUtil;
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
    private final JwtUtil               jwtUtil;
    private final UserDetailsService    userDetailsService;

    public AuthApiController(AuthenticationManager authenticationManager,
                              JwtUtil jwtUtil,
                              UserDetailsService userDetailsService) {
        this.authenticationManager = authenticationManager;
        this.jwtUtil               = jwtUtil;
        this.userDetailsService    = userDetailsService;
    }

    // ── LOGIN ─────────────────────────────────────────────────────────

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

        UserDetails userDetails = userDetailsService.loadUserByUsername(request.getUsername());
        String accessToken  = jwtUtil.generarToken(userDetails);
        String refreshToken = jwtUtil.generarRefreshToken(userDetails);

        return ResponseEntity.ok(Map.of(
            "accessToken",  accessToken,
            "refreshToken", refreshToken,
            "username",     request.getUsername(),
            "roles",        userDetails.getAuthorities().toString(),
            "expiresIn",    jwtUtil.getExpirationMs()
        ));
    }

    // ── REFRESH TOKEN ─────────────────────────────────────────────────

    @PostMapping("/auth/refresh-token")
    public ResponseEntity<Object> refresh(@RequestBody Map<String, String> body) {
        String refreshToken = body.get("refreshToken");

        if (refreshToken == null || refreshToken.isBlank()) {
            return ResponseEntity.badRequest()
                    .body(Map.of("error", "refreshToken requerido"));
        }

        if (!jwtUtil.validarRefreshToken(refreshToken)) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(Map.of("error", "Refresh token inválido o expirado"));
        }

        String username = jwtUtil.extraerUsername(refreshToken);
        UserDetails userDetails = userDetailsService.loadUserByUsername(username);
        String nuevoAccessToken = jwtUtil.generarToken(userDetails);

        return ResponseEntity.ok(Map.of(
            "accessToken", nuevoAccessToken,
            "username",    username,
            "roles",       userDetails.getAuthorities().toString(),
            "expiresIn",   jwtUtil.getExpirationMs()
        ));
    }

    // ── ME ────────────────────────────────────────────────────────────

    @GetMapping("/me")
    public ResponseEntity<Map<String, String>> me(
            org.springframework.security.core.Authentication auth) {
        return ResponseEntity.ok(Map.of(
            "username", auth.getName(),
            "roles",    auth.getAuthorities().toString()
        ));
    }
}
