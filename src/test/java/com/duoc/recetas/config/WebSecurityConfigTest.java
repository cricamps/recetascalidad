package com.duoc.recetas.config;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
class WebSecurityConfigTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Test
    void passwordEncoderEsBCrypt() {
        String encoded = passwordEncoder.encode("testpassword");
        assertNotNull(encoded);
        assertTrue(passwordEncoder.matches("testpassword", encoded));
        assertFalse(passwordEncoder.matches("wrongpassword", encoded));
    }

    @Test
    void rutaPublicaHomeEsAccesible() throws Exception {
        mockMvc.perform(get("/home"))
                .andExpect(status().isOk());
    }

    @Test
    void rutaPublicaLoginEsAccesible() throws Exception {
        mockMvc.perform(get("/login"))
                .andExpect(status().isOk());
    }

    @Test
    void rutaPrivadaNuevaRecetaRedirigeSinAuth() throws Exception {
        mockMvc.perform(get("/nueva-receta"))
                .andExpect(status().is3xxRedirection());
    }

    @Test
    void apiSinTokenRetornaForbidden() throws Exception {
        mockMvc.perform(get("/api/recetas"))
                .andExpect(status().isForbidden());
    }

    @Test
    void apiLoginEsPublica() throws Exception {
        mockMvc.perform(get("/api/auth/login"))
                .andExpect(status().isMethodNotAllowed()); // GET no está mapeado, pero no da 403
    }
}
