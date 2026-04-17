package com.duoc.recetas.security;

import com.duoc.recetas.service.UsuarioDetailsService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@SuppressWarnings("null")
class JwtAuthFilterTest {

    @Mock
    private JwtUtil jwtUtil;

    @Mock
    private UsuarioDetailsService usuarioDetailsService;

    @Mock
    private HttpServletRequest request;

    @Mock
    private HttpServletResponse response;

    @Mock
    private FilterChain filterChain;

    @InjectMocks
    private JwtAuthFilter jwtAuthFilter;

    private UserDetails userDetails;

    @BeforeEach
    void setUp() {
        SecurityContextHolder.clearContext();
        userDetails = new User("admin", "encoded",
                List.of(new SimpleGrantedAuthority("ROLE_ADMIN")));
    }

    @Test
    void sinHeaderAuthorizationPasaAlSiguienteFiltro() throws Exception {
        when(request.getHeader("Authorization")).thenReturn(null);

        jwtAuthFilter.doFilterInternal(request, response, filterChain);

        verify(filterChain).doFilter(request, response);
        assertNull(SecurityContextHolder.getContext().getAuthentication());
    }

    @Test
    void headerSinBearerPasaAlSiguienteFiltro() throws Exception {
        when(request.getHeader("Authorization")).thenReturn("Basic abc123");

        jwtAuthFilter.doFilterInternal(request, response, filterChain);

        verify(filterChain).doFilter(request, response);
        assertNull(SecurityContextHolder.getContext().getAuthentication());
    }

    @Test
    void tokenValidoSeteoAutenticacion() throws Exception {
        when(request.getHeader("Authorization")).thenReturn("Bearer token.valido.jwt");
        when(jwtUtil.extraerUsername("token.valido.jwt")).thenReturn("admin");
        when(usuarioDetailsService.loadUserByUsername("admin")).thenReturn(userDetails);
        when(jwtUtil.validarToken("token.valido.jwt", userDetails)).thenReturn(true);

        jwtAuthFilter.doFilterInternal(request, response, filterChain);

        verify(filterChain).doFilter(request, response);
        assertNotNull(SecurityContextHolder.getContext().getAuthentication());
        assertEquals("admin", SecurityContextHolder.getContext().getAuthentication().getName());
    }

    @Test
    void tokenInvalidoNoPropagaExcepcion() throws Exception {
        when(request.getHeader("Authorization")).thenReturn("Bearer token.invalido");
        when(jwtUtil.extraerUsername("token.invalido"))
                .thenThrow(new RuntimeException("Token inválido"));

        assertDoesNotThrow(() ->
                jwtAuthFilter.doFilterInternal(request, response, filterChain));
        verify(filterChain).doFilter(request, response);
    }

    @Test
    void tokenValidoPeroValidacionFallaNoSeteoAuth() throws Exception {
        when(request.getHeader("Authorization")).thenReturn("Bearer token.expirado");
        when(jwtUtil.extraerUsername("token.expirado")).thenReturn("admin");
        when(usuarioDetailsService.loadUserByUsername("admin")).thenReturn(userDetails);
        when(jwtUtil.validarToken("token.expirado", userDetails)).thenReturn(false);

        jwtAuthFilter.doFilterInternal(request, response, filterChain);

        verify(filterChain).doFilter(request, response);
        assertNull(SecurityContextHolder.getContext().getAuthentication());
    }
}
