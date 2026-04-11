package com.duoc.recetas.service;

import com.duoc.recetas.dto.RegistroRequest;
import com.duoc.recetas.entity.Usuario;
import com.duoc.recetas.repository.UsuarioRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Objects;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("Tests unitarios - UsuarioService")
@SuppressWarnings("null") // Mockito no tiene anotaciones null-safety compatibles con JDT
class UsuarioServiceTest {

    @Mock
    private UsuarioRepository usuarioRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private UsuarioService usuarioService;

    private RegistroRequest request;

    @BeforeEach
    void setUp() {
        request = new RegistroRequest();
        request.setUsername("juantest");
        request.setPassword("password123");
        request.setNombreCompleto("Juan Test");
        request.setCorreo("juan@test.cl");
    }

    @Test
    @DisplayName("Registro exitoso cuando el username no existe")
    void registrar_cuandoUsernameNoExiste_retornaTrue() {
        when(usuarioRepository.findByUsername("juantest")).thenReturn(Optional.empty());
        when(passwordEncoder.encode("password123")).thenReturn("$2a$12$encodedPassword");
        // thenAnswer evita el warning @NonNull que JDT lanza con thenReturn(new Usuario())
        when(usuarioRepository.save(any(Usuario.class))).thenAnswer(inv -> new Usuario());

        boolean resultado = usuarioService.registrar(request);

        assertThat(resultado).isTrue();
        verify(usuarioRepository, times(1)).save(any(Usuario.class));
    }

    @Test
    @DisplayName("Registro falla cuando el username ya existe")
    void registrar_cuandoUsernameExiste_retornaFalse() {
        // thenAnswer evita el warning @NonNull en el argumento de thenReturn
        Usuario existente = new Usuario("juantest", "pass", "ROLE_USER");
        when(usuarioRepository.findByUsername("juantest"))
                .thenAnswer(inv -> Optional.of(existente));

        boolean resultado = usuarioService.registrar(request);

        assertThat(resultado).isFalse();
        verify(usuarioRepository, never()).save(any(Usuario.class));
    }

    @Test
    @DisplayName("La contraseña se encripta antes de guardar")
    void registrar_encriptaContrasena() {
        when(usuarioRepository.findByUsername(anyString())).thenReturn(Optional.empty());
        when(passwordEncoder.encode("password123")).thenReturn("$2a$12$hashedValue");
        when(usuarioRepository.save(any(Usuario.class))).thenAnswer(inv -> inv.getArgument(0));

        usuarioService.registrar(request);

        verify(passwordEncoder, times(1)).encode("password123");
    }

    @Test
    @DisplayName("El nuevo usuario recibe rol ROLE_USER por defecto")
    void registrar_asignaRolUserPorDefecto() {
        when(usuarioRepository.findByUsername(anyString())).thenReturn(Optional.empty());
        when(passwordEncoder.encode(anyString())).thenReturn("hashed");

        // Array para capturar el argumento; thenAnswer evita el warning @NonNull
        Usuario[] capturado = new Usuario[1];
        when(usuarioRepository.save(any(Usuario.class))).thenAnswer(inv -> {
            Usuario u = inv.getArgument(0);
            capturado[0] = u;
            return u;
        });

        usuarioService.registrar(request);

        // requireNonNull confirma a JDT que capturado[0] no es null antes del assertThat
        Objects.requireNonNull(capturado[0], "El usuario capturado no debe ser null");
        assertThat(capturado[0].getRoles()).isEqualTo("ROLE_USER");
    }
}
