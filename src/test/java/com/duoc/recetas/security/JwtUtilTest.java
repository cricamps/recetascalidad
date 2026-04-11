package com.duoc.recetas.security;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.List;
import java.util.Objects;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("Tests unitarios - JwtUtil")
@SuppressWarnings("null") // Mockito no tiene anotaciones null-safety compatibles con JDT
class JwtUtilTest {

    private JwtUtil jwtUtil;
    private UserDetails userDetails;

    @BeforeEach
    void setUp() {
        jwtUtil = new JwtUtil();

        // ReflectionTestUtils.setField espera @NonNull Object como primer argumento.
        // Objects.requireNonNull satisface al compilador JDT sin cambiar la lógica.
        Objects.requireNonNull(jwtUtil, "jwtUtil no debe ser null");
        ReflectionTestUtils.setField(jwtUtil, "secret",
            "3f8a2b1c4d5e6f7a8b9c0d1e2f3a4b5c6d7e8f9a0b1c2d3e4f5a6b7c8d9e0f1a");
        ReflectionTestUtils.setField(jwtUtil, "expirationMs", 86400000L);

        userDetails = new User("usuario",
            "$2a$12$encodedpassword",
            List.of(new SimpleGrantedAuthority("ROLE_USER")));
    }

    @Test
    @DisplayName("generarToken produce un token no nulo y no vacío")
    void generarToken_producTokenValido() {
        String token = jwtUtil.generarToken(userDetails);
        assertThat(token).isNotNull().isNotBlank();
    }

    @Test
    @DisplayName("generarToken produce token con 3 partes separadas por punto")
    void generarToken_tieneEstructuraJwt() {
        String token = jwtUtil.generarToken(userDetails);
        String[] partes = token.split("\\.");
        assertThat(partes).hasSize(3);
    }

    @Test
    @DisplayName("extraerUsername devuelve el username correcto del token")
    void extraerUsername_retornaUsernameDelToken() {
        String token = jwtUtil.generarToken(userDetails);
        String username = jwtUtil.extraerUsername(token);
        assertThat(username).isEqualTo("usuario");
    }

    @Test
    @DisplayName("validarToken retorna true para token válido y usuario correcto")
    void validarToken_conTokenValidoYUsuarioCorrecto_retornaTrue() {
        String token = jwtUtil.generarToken(userDetails);
        boolean valido = jwtUtil.validarToken(token, userDetails);
        assertThat(valido).isTrue();
    }

    @Test
    @DisplayName("validarToken retorna false para usuario diferente al del token")
    void validarToken_conUsuarioDiferente_retornaFalse() {
        String token = jwtUtil.generarToken(userDetails);

        UserDetails otroUsuario = new User("otro",
            "$2a$12$otropassword",
            List.of(new SimpleGrantedAuthority("ROLE_USER")));

        boolean valido = jwtUtil.validarToken(token, otroUsuario);
        assertThat(valido).isFalse();
    }

    @Test
    @DisplayName("validarToken retorna false para token manipulado")
    void validarToken_conTokenManipulado_retornaFalse() {
        String tokenManipulado = "eyJhbGciOiJIUzI1NiJ9.invalido.firma_invalida";
        boolean valido = jwtUtil.validarToken(tokenManipulado, userDetails);
        assertThat(valido).isFalse();
    }
}
