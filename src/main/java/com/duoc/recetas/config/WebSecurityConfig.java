package com.duoc.recetas.config;

import com.duoc.recetas.security.JwtAuthFilter;
import com.duoc.recetas.service.UsuarioDetailsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.header.writers.ReferrerPolicyHeaderWriter;

@Configuration
@EnableWebSecurity
public class WebSecurityConfig {

    @Autowired
    private UsuarioDetailsService usuarioDetailsService;

    @Autowired
    private JwtAuthFilter jwtAuthFilter;

    // ─────────────────────────────────────────────────────────────────
    // 1) Cadena API REST  /api/**  →  stateless + JWT
    // ─────────────────────────────────────────────────────────────────
    @Bean
    @Order(1)
    public SecurityFilterChain apiFilterChain(HttpSecurity http) throws Exception {
        http
            .securityMatcher("/api/**")
            .csrf(csrf -> csrf.disable())
            .sessionManagement(sm -> sm.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
            .authorizeHttpRequests(auth -> auth
                .requestMatchers("/api/auth/login").permitAll()
                .anyRequest().authenticated()
            )
            .addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }

    // ─────────────────────────────────────────────────────────────────
    // 2) Cadena Web (Thymeleaf)  →  form login con sesión
    // ─────────────────────────────────────────────────────────────────
    @Bean
    @Order(2)
    public SecurityFilterChain webFilterChain(HttpSecurity http) throws Exception {
        http
            .authorizeHttpRequests(requests -> requests
                // Rutas públicas
                .requestMatchers("/", "/home", "/buscar").permitAll()
                .requestMatchers("/css/**", "/img/**", "/js/**").permitAll()
                .requestMatchers("/login", "/login?error", "/login?logout").permitAll()
                .requestMatchers("/registro", "/registro?registrado").permitAll()
                .requestMatchers("/h2-console/**").permitAll()
                // Todo lo demás requiere autenticación
                .anyRequest().authenticated()
            )
            .csrf(csrf -> csrf
                .ignoringRequestMatchers("/h2-console/**")
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
                .frameOptions(fo -> fo.sameOrigin())
                .xssProtection(xss -> xss.disable())
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

    // ─────────────────────────────────────────────────────────────────
    // AuthenticationManager
    // ─────────────────────────────────────────────────────────────────
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
        return new BCryptPasswordEncoder();
    }
}
