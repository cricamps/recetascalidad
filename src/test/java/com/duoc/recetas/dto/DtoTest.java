package com.duoc.recetas.dto;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("Tests unitarios - DTOs")
class DtoTest {

    @Test
    @DisplayName("LoginRequest constructor vacío y setters funcionan")
    void loginRequest_constructorVacio() {
        LoginRequest req = new LoginRequest();
        req.setUsername("admin");
        req.setPassword("admin123");
        assertThat(req.getUsername()).isEqualTo("admin");
        assertThat(req.getPassword()).isEqualTo("admin123");
    }

    @Test
    @DisplayName("LoginRequest constructor con parámetros funciona")
    void loginRequest_constructorConParams() {
        LoginRequest req = new LoginRequest("usuario", "pass123");
        assertThat(req.getUsername()).isEqualTo("usuario");
        assertThat(req.getPassword()).isEqualTo("pass123");
    }

    @Test
    @DisplayName("LoginResponse constructor asigna todos los campos")
    void loginResponse_constructor() {
        LoginResponse resp = new LoginResponse("token123", "usuario", "ROLE_USER", 86400000L);
        assertThat(resp.getToken()).isEqualTo("token123");
        assertThat(resp.getUsername()).isEqualTo("usuario");
        assertThat(resp.getRoles()).isEqualTo("ROLE_USER");
        assertThat(resp.getExpiresIn()).isEqualTo(86400000L);
    }

    @Test
    @DisplayName("LoginResponse setters actualizan valores")
    void loginResponse_setters() {
        LoginResponse resp = new LoginResponse("t", "u", "r", 1000L);
        resp.setToken("nuevoToken");
        resp.setUsername("nuevoUser");
        resp.setRoles("ROLE_ADMIN");
        resp.setExpiresIn(3600000L);
        assertThat(resp.getToken()).isEqualTo("nuevoToken");
        assertThat(resp.getUsername()).isEqualTo("nuevoUser");
        assertThat(resp.getRoles()).isEqualTo("ROLE_ADMIN");
        assertThat(resp.getExpiresIn()).isEqualTo(3600000L);
    }

    @Test
    @DisplayName("RegistroRequest getters y setters funcionan")
    void registroRequest_gettersSetters() {
        RegistroRequest req = new RegistroRequest();
        req.setNombreCompleto("María González");
        req.setCorreo("maria@test.cl");
        req.setUsername("mariag");
        req.setPassword("pass456");
        assertThat(req.getNombreCompleto()).isEqualTo("María González");
        assertThat(req.getCorreo()).isEqualTo("maria@test.cl");
        assertThat(req.getUsername()).isEqualTo("mariag");
        assertThat(req.getPassword()).isEqualTo("pass456");
    }
}
