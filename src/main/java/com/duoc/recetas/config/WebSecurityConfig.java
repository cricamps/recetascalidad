package com.duoc.recetas.config;

import com.duoc.recetas.security.JwtAuthFilter;
import com.duoc.recetas.service.UsuarioDetailsService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.csrf.CsrfTokenRepository;
import org.springframework.security.web.csrf.HttpSessionCsrfTokenRepository;
import org.springframework.security.web.header.writers.ReferrerPolicyHeaderWriter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.List;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
public class WebSecurityConfig {

    private final UsuarioDetailsService usuarioDetailsService;
    private final JwtAuthFilter jwtAuthFilter;

    public WebSecurityConfig(UsuarioDetailsService usuarioDetailsService,
                              JwtAuthFilter jwtAuthFilter) {
        this.usuarioDetailsService = usuarioDetailsService;
        this.jwtAuthFilter = jwtAuthFilter;
    }

    // ═══════════════════════════════════════════════════════════════
    // CADENA 1 — API REST  /api/**
    // ---------------------------------------------------------------
    // Capa BACKEND pura: stateless, sin sesión, sin CSRF.
    // Autenticación exclusivamente por header: Authorization: Bearer <JWT>
    // Rutas públicas:  POST /api/auth/login
    // Rutas privadas:  todo lo demás bajo /api/**
    // ═══════════════════════════════════════════════════════════════
    @Bean
    @Order(1)
    @SuppressWarnings("java:S4502")
    public SecurityFilterChain apiFilterChain(HttpSecurity http) throws Exception {
        http
            .securityMatcher("/api/**")
            .cors(cors -> cors.configurationSource(corsConfigurationSource()))
            .csrf(csrf -> csrf.disable())
            .sessionManagement(sm ->
                sm.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
            .authorizeHttpRequests(auth -> auth
                // ── PÚBLICAS ──────────────────────────────────────
                .requestMatchers("/api/auth/login").permitAll()
                // ── PRIVADAS (requieren JWT válido) ───────────────
                .requestMatchers("/api/me").authenticated()
                .requestMatchers("/api/recetas/**").authenticated()
                .requestMatchers("/api/comentarios/**").authenticated()
                .requestMatchers("/api/medios/**").authenticated()
                .anyRequest().authenticated()
            )
            .addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }

    // ═══════════════════════════════════════════════════════════════
    // CADENA 2 — WEB  (Thymeleaf / SSR)
    // ---------------------------------------------------------------
    // Capa FRONTEND SSR: con sesión HTTP y protección CSRF.
    // Rutas públicas:  /, /home, /buscar, /receta/{id} (lectura),
    //                  /login, /registro, /css/**, /img/**, /js/**
    // Rutas privadas:  publicar, comentar, subir medios, compartir
    // ═══════════════════════════════════════════════════════════════
    @Bean
    @Order(2)
    public SecurityFilterChain webFilterChain(HttpSecurity http) throws Exception {
        http
            .authorizeHttpRequests(auth -> auth
                // ── RUTAS PÚBLICAS ────────────────────────────────
                .requestMatchers("/", "/home").permitAll()
                .requestMatchers("/buscar").permitAll()
                .requestMatchers("/receta/{id}").permitAll()          // lectura pública
                .requestMatchers("/login", "/registro").permitAll()
                .requestMatchers("/css/**", "/img/**", "/js/**").permitAll()
                .requestMatchers("/error").permitAll()
                // ── RUTAS PRIVADAS ────────────────────────────────
                .requestMatchers("/admin/**").hasRole("ADMIN")
                .requestMatchers("/nueva-receta").authenticated()
                .requestMatchers("/receta/*/comentar").authenticated()
                .requestMatchers("/receta/*/subir-medio").authenticated()
                .requestMatchers("/receta/*/compartir").authenticated()
                // ── CUALQUIER OTRA → privada por defecto ──────────
                .anyRequest().authenticated()
            )
            .csrf(csrf -> csrf
                .csrfTokenRepository(csrfTokenRepository())
            )
            .formLogin(form -> form
                .loginPage("/login")
                .defaultSuccessUrl("/home", true)
                .failureUrl("/login?error=true")
                .permitAll()
            )
            .logout(logout -> logout
                .logoutSuccessUrl("/home")
                .invalidateHttpSession(true)
                .deleteCookies("JSESSIONID")
                .permitAll()
            )
            .headers(headers -> headers
                .frameOptions(fo -> fo.deny())
                .contentTypeOptions(cto -> {})
                .httpStrictTransportSecurity(hsts -> hsts
                    .includeSubDomains(true)
                    .maxAgeInSeconds(31536000)
                )
                .referrerPolicy(ref -> ref
                    .policy(ReferrerPolicyHeaderWriter.ReferrerPolicy.STRICT_ORIGIN_WHEN_CROSS_ORIGIN)
                )
                .contentSecurityPolicy(csp -> csp
                    .policyDirectives(
                        "default-src 'self'; " +
                        "script-src 'self' 'unsafe-inline'; " +
                        "style-src 'self' 'unsafe-inline' https://fonts.googleapis.com; " +
                        "font-src 'self' https://fonts.gstatic.com; " +
                        "img-src 'self' data:; " +
                        "frame-ancestors 'none';"
                    )
                )
            );

        return http.build();
    }

    // ═══════════════════════════════════════════════════════════════
    // CORS — permite que el frontend separado (otro origen)
    // consuma la API REST sin bloqueos del navegador.
    // En producción reemplazar "*" por el dominio real del frontend.
    // ═══════════════════════════════════════════════════════════════
    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration config = new CorsConfiguration();
        config.setAllowedOriginPatterns(List.of("*"));
        config.setAllowedMethods(List.of("GET", "POST", "PUT", "DELETE", "OPTIONS"));
        config.setAllowedHeaders(List.of("Authorization", "Content-Type", "X-CSRF-TOKEN"));
        config.setExposedHeaders(List.of("Authorization"));
        config.setAllowCredentials(false);
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/api/**", config);
        return source;
    }

    @Bean
    public CsrfTokenRepository csrfTokenRepository() {
        HttpSessionCsrfTokenRepository repository = new HttpSessionCsrfTokenRepository();
        repository.setHeaderName("X-CSRF-TOKEN");
        return repository;
    }

    @Bean
    public AuthenticationManager authenticationManager(HttpSecurity http) throws Exception {
        AuthenticationManagerBuilder builder =
                http.getSharedObject(AuthenticationManagerBuilder.class);
        builder
            .userDetailsService(usuarioDetailsService)
            .passwordEncoder(passwordEncoder());
        return builder.build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder(12);
    }
}
