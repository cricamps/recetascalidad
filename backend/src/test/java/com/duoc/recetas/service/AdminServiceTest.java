package com.duoc.recetas.service;

import com.duoc.recetas.entity.Usuario;
import com.duoc.recetas.repository.UsuarioRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AdminServiceTest {

    @Mock private UsuarioRepository usuarioRepository;
    @Mock private PasswordEncoder passwordEncoder;
    @InjectMocks private AdminService adminService;

    @Test
    void listarUsuariosRetornaLista() {
        Usuario u1 = new Usuario("admin", "pass", "ROLE_USER,ROLE_ADMIN");
        Usuario u2 = new Usuario("chef",  "pass", "ROLE_USER,ROLE_CHEF");
        when(usuarioRepository.findAll()).thenReturn(List.of(u1, u2));
        assertEquals(2, adminService.listarUsuarios().size());
    }

    @Test
    void cambiarRolActualizaUsuario() {
        Usuario usuario = new Usuario("juan", "pass", "ROLE_USER");
        usuario.setId(1L);
        when(usuarioRepository.findById(1L)).thenReturn(Optional.of(usuario));
        adminService.cambiarRol(1L, "ROLE_USER,ROLE_ADMIN");
        verify(usuarioRepository).save(argThat(u -> u.getRoles().equals("ROLE_USER,ROLE_ADMIN")));
    }

    @Test
    void cambiarRolUsuarioInexistenteNoHaceNada() {
        when(usuarioRepository.findById(999L)).thenReturn(Optional.empty());
        adminService.cambiarRol(999L, "ROLE_ADMIN");
        verify(usuarioRepository, never()).save(any());
    }

    @Test
    void resetearPasswordActualizaPassword() {
        Usuario usuario = new Usuario("juan", "oldpass", "ROLE_USER");
        usuario.setId(1L);
        when(usuarioRepository.findById(1L)).thenReturn(Optional.of(usuario));
        when(passwordEncoder.encode("newpass")).thenReturn("hashedNewPass");
        adminService.resetearPassword(1L, "newpass");
        verify(usuarioRepository).save(argThat(u -> u.getPassword().equals("hashedNewPass")));
    }

    @Test
    void eliminarUsuarioLlamaDeleteById() {
        adminService.eliminarUsuario(1L);
        verify(usuarioRepository).deleteById(1L);
    }

    @Test
    void crearUsuarioNuevoLoGuarda() {
        when(usuarioRepository.findByUsername("nuevo")).thenReturn(Optional.empty());
        when(passwordEncoder.encode("pass123")).thenReturn("hashed");
        adminService.crearUsuario("nuevo", "pass123", "ROLE_USER", "Nuevo User", "nuevo@test.cl");
        verify(usuarioRepository).save(any(Usuario.class));
    }

    @Test
    void crearUsuarioDuplicadoLanzaExcepcion() {
        when(usuarioRepository.findByUsername("admin"))
                .thenReturn(Optional.of(new Usuario("admin", "pass", "ROLE_USER,ROLE_ADMIN")));
        assertThrows(IllegalArgumentException.class, () ->
                adminService.crearUsuario("admin", "pass", "ROLE_USER", null, null));
        verify(usuarioRepository, never()).save(any());
    }

    @Test
    void buscarPorIdExistenteRetornaUsuario() {
        Usuario usuario = new Usuario("test", "pass", "ROLE_USER");
        when(usuarioRepository.findById(1L)).thenReturn(Optional.of(usuario));
        Optional<Usuario> resultado = adminService.buscarPorId(1L);
        assertTrue(resultado.isPresent());
        assertEquals("test", resultado.get().getUsername());
    }
}
