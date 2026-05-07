package com.duoc.recetas.config;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.web.csrf.CsrfTokenRepository;
import org.springframework.security.web.csrf.HttpSessionCsrfTokenRepository;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.cors.CorsConfigurationSource;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Tests de integración extendidos para WebSecurityConfig.
 * Cubren los beans de CORS, CSRF, AuthenticationManager y las reglas de seguridad
 * tanto de la cadena API (stateless/JWT) como de la cadena Web (sesión/formulario).
 */
@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
@DisplayName("WebSecurityConfig - cobertura extendida de beans y reglas")
class WebSecurityConfigExtendedTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private CorsConfigurationSource corsConfigurationSource;

    @Autowired
    private CsrfTokenRepository csrfTokenRepository;

    @Autowired
    private AuthenticationManager authenticationManager;

    // ── BEANS ─────────────────────────────────────────────────────────────────

    @Test
    @DisplayName("corsConfigurationSource bean se crea correctamente")
    void corsConfigurationSource_beancreado() {
        assertThat(corsConfigurationSource).isNotNull();
    }

    @Test
    @DisplayName("csrfTokenRepository bean es de tipo HttpSessionCsrfTokenRepository")
    void csrfTokenRepository_esHttpSession() {
        assertThat(csrfTokenRepository).isInstanceOf(HttpSessionCsrfTokenRepository.class);
    }

    @Test
    @DisplayName("authenticationManager bean se crea correctamente")
    void authenticationManager_beancreado() {
        assertThat(authenticationManager).isNotNull();
    }

    // ── CADENA WEB — rutas públicas ───────────────────────────────────────────

    @Test
    @DisplayName("GET / (raíz) es accesible sin autenticación")
    void root_esAccesible() throws Exception {
        int status = mockMvc.perform(get("/"))
                .andReturn().getResponse().getStatus();
        assertThat(status).isBetween(200, 299);
    }

    @Test
    @DisplayName("GET /buscar es accesible sin autenticación")
    void buscar_esPublica() throws Exception {
        mockMvc.perform(get("/buscar"))
                .andExpect(status().isOk());
    }

    @Test
    @DisplayName("GET /registro es accesible sin autenticación")
    void registro_esPublica() throws Exception {
        mockMvc.perform(get("/registro"))
                .andExpect(status().isOk());
    }

    @Test
    @DisplayName("GET /css/* retorna status distinto de 401/403")
    void recursos_css_sonPublicos() throws Exception {
        int status = mockMvc.perform(get("/css/styles.css"))
                .andReturn().getResponse().getStatus();
        assertThat(status).isNotIn(401, 403);
    }

    // ── CADENA WEB — rutas privadas ───────────────────────────────────────────

    @Test
    @DisplayName("GET /nueva-receta sin auth redirige a login")
    void nuevaReceta_sinAuth_redirige() throws Exception {
        mockMvc.perform(get("/nueva-receta"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrlPattern("**/login**"));
    }

    @Test
    @DisplayName("GET /admin/usuarios sin auth redirige a login")
    void adminUsuarios_sinAuth_redirige() throws Exception {
        mockMvc.perform(get("/admin/usuarios"))
                .andExpect(status().is3xxRedirection());
    }

    // ── CADENA API — rutas públicas ───────────────────────────────────────────

    @Test
    @DisplayName("POST /api/auth/login sin body retorna distinto de 403")
    void apiAuthLogin_sinBody_noRetornaForbidden() throws Exception {
        int status = mockMvc.perform(post("/api/auth/login")
                .contentType("application/json")
                .content("{}"))
                .andReturn().getResponse().getStatus();
        assertThat(status).isNotEqualTo(403);
    }

    // ── CADENA API — rutas privadas ───────────────────────────────────────────

    @Test
    @DisplayName("GET /api/me sin token retorna 403")
    void apiMe_sinToken_retornaForbidden() throws Exception {
        mockMvc.perform(get("/api/me"))
                .andExpect(status().isForbidden());
    }

    @Test
    @DisplayName("GET /api/comentarios/* sin token retorna 403")
    void apiComentarios_sinToken_retornaForbidden() throws Exception {
        mockMvc.perform(get("/api/comentarios/1"))
                .andExpect(status().isForbidden());
    }

    @Test
    @DisplayName("GET /api/medios/* sin token retorna 403")
    void apiMedios_sinToken_retornaForbidden() throws Exception {
        mockMvc.perform(get("/api/medios/1"))
                .andExpect(status().isForbidden());
    }

    // ── HEADERS DE SEGURIDAD ──────────────────────────────────────────────────

    @Test
    @DisplayName("Respuesta web incluye header X-Content-Type-Options: nosniff")
    void respuestaWeb_incluyeContentTypeOptions() throws Exception {
        mockMvc.perform(get("/home"))
                .andExpect(header().string("X-Content-Type-Options", "nosniff"));
    }

    @Test
    @DisplayName("Respuesta web incluye header X-Frame-Options: DENY")
    void respuestaWeb_incluyeFrameOptionsDeny() throws Exception {
        mockMvc.perform(get("/home"))
                .andExpect(header().string("X-Frame-Options", "DENY"));
    }

    @Test
    @DisplayName("Respuesta web incluye header Content-Security-Policy")
    void respuestaWeb_incluyeCSP() throws Exception {
        mockMvc.perform(get("/home"))
                .andExpect(header().exists("Content-Security-Policy"));
    }
}
