package com.duoc.recetas.controller;

import com.duoc.recetas.dto.LoginRequest;
import com.duoc.recetas.security.JwtUtil;
import com.duoc.recetas.service.UsuarioDetailsService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Tests de integración extendidos para AuthApiController.
 * Cubre el endpoint /api/me que requiere autenticación JWT.
 */
@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
@DisplayName("AuthApiController - tests extendidos (endpoint /api/me)")
@SuppressWarnings("null")
class AuthApiControllerExtendedTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    private UsuarioDetailsService usuarioDetailsService;

    @Autowired
    private ObjectMapper objectMapper;

    private String tokenAdmin;
    private String tokenChef;
    private String tokenUsuario;

    @BeforeEach
    void setUp() {
        UserDetails admin   = usuarioDetailsService.loadUserByUsername("admin");
        UserDetails chef    = usuarioDetailsService.loadUserByUsername("chef");
        UserDetails usuario = usuarioDetailsService.loadUserByUsername("usuario");
        tokenAdmin   = jwtUtil.generarToken(admin);
        tokenChef    = jwtUtil.generarToken(chef);
        tokenUsuario = jwtUtil.generarToken(usuario);
    }

    // ── /api/me ───────────────────────────────────────────────────────────────

    @Test
    @DisplayName("GET /api/me sin token retorna 403")
    void apiMe_sinToken_retornaForbidden() throws Exception {
        mockMvc.perform(get("/api/me"))
                .andExpect(status().isForbidden());
    }

    @Test
    @DisplayName("GET /api/me con token admin retorna username y roles")
    void apiMe_conTokenAdmin_retornaInfo() throws Exception {
        mockMvc.perform(get("/api/me")
                .header("Authorization", "Bearer " + tokenAdmin))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.username").value("admin"))
                .andExpect(jsonPath("$.roles").isString());
    }

    @Test
    @DisplayName("GET /api/me con token chef retorna username chef")
    void apiMe_conTokenChef_retornaInfo() throws Exception {
        mockMvc.perform(get("/api/me")
                .header("Authorization", "Bearer " + tokenChef))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.username").value("chef"));
    }

    @Test
    @DisplayName("GET /api/me con token usuario retorna username usuario")
    void apiMe_conTokenUsuario_retornaInfo() throws Exception {
        mockMvc.perform(get("/api/me")
                .header("Authorization", "Bearer " + tokenUsuario))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.username").value("usuario"));
    }

    @Test
    @DisplayName("GET /api/me con token inválido retorna 403")
    void apiMe_conTokenInvalido_retornaForbidden() throws Exception {
        mockMvc.perform(get("/api/me")
                .header("Authorization", "Bearer token.invalido.jwt"))
                .andExpect(status().isForbidden());
    }

    // ── /api/auth/login extendido ─────────────────────────────────────────────

    @Test
    @DisplayName("Login con usuario chef retorna token y roles correctos")
    void login_conChef_retornaToken() throws Exception {
        LoginRequest req = new LoginRequest();
        req.setUsername("chef");
        req.setPassword("chef123");

        mockMvc.perform(post("/api/auth/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(req)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.token").isNotEmpty())
                .andExpect(jsonPath("$.username").value("chef"))
                .andExpect(jsonPath("$.roles").isString());
    }

    @Test
    @DisplayName("Login con usuario normal retorna token válido")
    void login_conUsuario_retornaToken() throws Exception {
        LoginRequest req = new LoginRequest();
        req.setUsername("usuario");
        req.setPassword("usuario123");

        mockMvc.perform(post("/api/auth/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(req)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.token").isNotEmpty());
    }

    // ── /api/recetas con usuario autenticado ──────────────────────────────────

    @Test
    @DisplayName("GET /api/recetas/{id} existente retorna receta")
    void apiRecetaById_existente_retornaReceta() throws Exception {
        mockMvc.perform(get("/api/recetas/1")
                .header("Authorization", "Bearer " + tokenAdmin))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.nombre").isString());
    }
}
