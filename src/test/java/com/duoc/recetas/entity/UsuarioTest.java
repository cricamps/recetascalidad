package com.duoc.recetas.entity;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("Tests unitarios - Entidad Usuario")
class UsuarioTest {

    @Test
    @DisplayName("Constructor vacío crea usuario sin datos")
    void constructorVacio_creaUsuarioSinDatos() {
        Usuario usuario = new Usuario();
        assertThat(usuario.getUsername()).isNull();
        assertThat(usuario.getPassword()).isNull();
        assertThat(usuario.getRoles()).isNull();
    }

    @Test
    @DisplayName("Constructor con 3 parámetros asigna username, password y roles")
    void constructorTresParams_asignaCamposCorrectamente() {
        Usuario usuario = new Usuario("admin", "pass123", "ROLE_ADMIN");
        assertThat(usuario.getUsername()).isEqualTo("admin");
        assertThat(usuario.getPassword()).isEqualTo("pass123");
        assertThat(usuario.getRoles()).isEqualTo("ROLE_ADMIN");
    }

    @Test
    @DisplayName("Constructor con 5 parámetros asigna todos los campos")
    void constructorCincoParams_asignaTodosLosCampos() {
        Usuario usuario = new Usuario("maria", "pass", "ROLE_USER", "María González", "maria@test.cl");
        assertThat(usuario.getUsername()).isEqualTo("maria");
        assertThat(usuario.getNombreCompleto()).isEqualTo("María González");
        assertThat(usuario.getCorreo()).isEqualTo("maria@test.cl");
    }

    @Test
    @DisplayName("Setters actualizan los campos correctamente")
    void setters_actualizanCampos() {
        Usuario usuario = new Usuario();
        usuario.setId(1L);
        usuario.setUsername("chef");
        usuario.setPassword("encoded");
        usuario.setRoles("ROLE_USER,ROLE_CHEF");
        usuario.setNombreCompleto("Chef Pro");
        usuario.setCorreo("chef@cocina.cl");

        assertThat(usuario.getId()).isEqualTo(1L);
        assertThat(usuario.getUsername()).isEqualTo("chef");
        assertThat(usuario.getRoles()).isEqualTo("ROLE_USER,ROLE_CHEF");
        assertThat(usuario.getNombreCompleto()).isEqualTo("Chef Pro");
        assertThat(usuario.getCorreo()).isEqualTo("chef@cocina.cl");
    }
}
