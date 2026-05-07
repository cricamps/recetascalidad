package com.duoc.recetas.security;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.List;
import java.util.Objects;

import static org.assertj.core.api.Assertions.*;

class JwtUtilRefreshTest {

    private JwtUtil jwtUtil;

    @BeforeEach
    void setUp() {
        jwtUtil = new JwtUtil();
        Objects.requireNonNull(jwtUtil, "jwtUtil no puede ser null");
        ReflectionTestUtils.setField(jwtUtil, "secret",
                "claveSecretaSuperLargaParaPruebasISY2202RecetasChef!!");
        ReflectionTestUtils.setField(jwtUtil, "expirationMs", 3600000L);
    }

    private UserDetails makeUser() {
        return new User("testuser", "password",
                List.of(new SimpleGrantedAuthority("ROLE_USER")));
    }

    @Test
    void generarRefreshToken_notNull() {
        assertThat(jwtUtil.generarRefreshToken(makeUser())).isNotBlank();
    }

    @Test
    void validarRefreshToken_validToken_returnsTrue() {
        String rt = jwtUtil.generarRefreshToken(makeUser());
        assertThat(jwtUtil.validarRefreshToken(rt)).isTrue();
    }

    @Test
    void validarRefreshToken_accessToken_returnsFalse() {
        String at = jwtUtil.generarToken(makeUser());
        assertThat(jwtUtil.validarRefreshToken(at)).isFalse();
    }

    @Test
    void validarRefreshToken_invalidToken_returnsFalse() {
        assertThat(jwtUtil.validarRefreshToken("token.invalido.xyz")).isFalse();
    }

    @Test
    void extraerUsername_fromRefreshToken_returnsCorrectUsername() {
        String rt = jwtUtil.generarRefreshToken(makeUser());
        assertThat(jwtUtil.extraerUsername(rt)).isEqualTo("testuser");
    }

    @Test
    void getExpirationMs_returnsConfiguredValue() {
        assertThat(jwtUtil.getExpirationMs()).isEqualTo(3600000L);
    }
}
