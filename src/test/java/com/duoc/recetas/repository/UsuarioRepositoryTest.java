package com.duoc.recetas.repository;

import com.duoc.recetas.entity.Usuario;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@ActiveProfiles("test")
class UsuarioRepositoryTest {

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Test
    void guardarYBuscarPorUsername() {
        Usuario usuario = new Usuario("testuser", "hashedpassword", "ROLE_USER");
        usuarioRepository.save(usuario);

        Optional<Usuario> encontrado = usuarioRepository.findByUsername("testuser");

        assertTrue(encontrado.isPresent());
        assertEquals("testuser", encontrado.get().getUsername());
        assertEquals("ROLE_USER", encontrado.get().getRoles());
    }

    @Test
    void findByUsernameInexistenteRetornaVacio() {
        Optional<Usuario> resultado = usuarioRepository.findByUsername("noexiste");
        assertFalse(resultado.isPresent());
    }

    @Test
    void guardarMultiplesUsuariosConDistintoRol() {
        usuarioRepository.save(new Usuario("user1", "pass1", "ROLE_USER"));
        usuarioRepository.save(new Usuario("admin1", "pass2", "ROLE_ADMIN"));

        assertTrue(usuarioRepository.findByUsername("user1").isPresent());
        assertTrue(usuarioRepository.findByUsername("admin1").isPresent());

        assertEquals("ROLE_ADMIN",
                usuarioRepository.findByUsername("admin1").get().getRoles());
    }

    @Test
    void contarUsuariosRetornaCantidadCorrecta() {
        long antes = usuarioRepository.count();
        usuarioRepository.save(new Usuario("nuevouser", "pass", "ROLE_USER"));
        long despues = usuarioRepository.count();
        assertEquals(antes + 1, despues);
    }
}
