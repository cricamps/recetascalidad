package com.duoc.recetas.service;

import com.duoc.recetas.entity.Usuario;
import com.duoc.recetas.repository.UsuarioRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
@DisplayName("Tests unitarios - UsuarioDetailsService")
class UsuarioDetailsServiceTest {

    @Mock
    private UsuarioRepository usuarioRepository;

    @InjectMocks
    private UsuarioDetailsService usuarioDetailsService;

    @Test
    @DisplayName("loadUserByUsername retorna UserDetails cuando el usuario existe")
    void loadUserByUsername_cuandoExiste_retornaUserDetails() {
        Usuario usuario = new Usuario("chef", "$2a$12$encoded", "ROLE_USER,ROLE_CHEF");
        when(usuarioRepository.findByUsername("chef")).thenReturn(Optional.of(usuario));

        UserDetails result = usuarioDetailsService.loadUserByUsername("chef");

        assertThat(result.getUsername()).isEqualTo("chef");
        assertThat(result.getAuthorities()).hasSize(2);
    }

    @Test
    @DisplayName("loadUserByUsername lanza excepción cuando el usuario no existe")
    void loadUserByUsername_cuandoNoExiste_lanzaExcepcion() {
        when(usuarioRepository.findByUsername("fantasma"))
            .thenReturn(Optional.empty());

        assertThatThrownBy(() ->
            usuarioDetailsService.loadUserByUsername("fantasma"))
            .isInstanceOf(UsernameNotFoundException.class)
            .hasMessageContaining("fantasma");
    }

    @Test
    @DisplayName("loadUserByUsername asigna correctamente un solo rol")
    void loadUserByUsername_conUnRol_asignaUnAuthority() {
        Usuario usuario = new Usuario("usuario", "$2a$12$encoded", "ROLE_USER");
        when(usuarioRepository.findByUsername("usuario")).thenReturn(Optional.of(usuario));

        UserDetails result = usuarioDetailsService.loadUserByUsername("usuario");

        assertThat(result.getAuthorities()).hasSize(1);
        assertThat(result.getAuthorities().iterator().next().getAuthority())
            .isEqualTo("ROLE_USER");
    }

    @Test
    @DisplayName("loadUserByUsername asigna correctamente tres roles")
    void loadUserByUsername_conTresRoles_asignaTresAuthorities() {
        Usuario usuario = new Usuario("admin", "$2a$12$encoded", "ROLE_USER,ROLE_CHEF,ROLE_ADMIN");
        when(usuarioRepository.findByUsername("admin")).thenReturn(Optional.of(usuario));

        UserDetails result = usuarioDetailsService.loadUserByUsername("admin");

        assertThat(result.getAuthorities()).hasSize(3);
    }
}
