package com.duoc.recetas.controller;

import com.duoc.recetas.security.JwtUtil;
import com.duoc.recetas.service.UsuarioDetailsService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
class RecetaApiControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    private UsuarioDetailsService usuarioDetailsService;

    private String token;

    @BeforeEach
    void setUp() {
        UserDetails userDetails = usuarioDetailsService.loadUserByUsername("admin");
        token = jwtUtil.generarToken(userDetails);
    }

    @Test
    void getTodasRequiereAutenticacion() throws Exception {
        mockMvc.perform(get("/api/recetas"))
                .andExpect(status().isForbidden());
    }

    @Test
    void getTodasConTokenRetornaLista() throws Exception {
        mockMvc.perform(get("/api/recetas")
                .header("Authorization", "Bearer " + token))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray());
    }

    @Test
    void getPopularesConTokenRetornaLista() throws Exception {
        mockMvc.perform(get("/api/recetas/populares")
                .header("Authorization", "Bearer " + token))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray());
    }

    @Test
    void getRecientesConTokenRetornaLista() throws Exception {
        mockMvc.perform(get("/api/recetas/recientes")
                .header("Authorization", "Bearer " + token))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray());
    }

    @Test
    void getPorIdInexistenteRetorna404() throws Exception {
        mockMvc.perform(get("/api/recetas/99999")
                .header("Authorization", "Bearer " + token))
                .andExpect(status().isNotFound());
    }

    @Test
    void buscarConParametroRetornaLista() throws Exception {
        mockMvc.perform(get("/api/recetas/buscar")
                .param("nombre", "arroz")
                .header("Authorization", "Bearer " + token))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray());
    }
}
