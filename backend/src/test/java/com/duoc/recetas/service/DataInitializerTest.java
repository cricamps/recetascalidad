package com.duoc.recetas.service;

import com.duoc.recetas.repository.RecetaRepository;
import com.duoc.recetas.repository.UsuarioRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@ActiveProfiles("test")
class DataInitializerTest {

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private RecetaRepository recetaRepository;

    @Test
    void usuariosInicializadosExisten() {
        assertTrue(usuarioRepository.findByUsername("admin").isPresent(),
                "El usuario admin debe existir después de inicialización");
        assertTrue(usuarioRepository.findByUsername("chef").isPresent(),
                "El usuario chef debe existir después de inicialización");
        assertTrue(usuarioRepository.findByUsername("usuario").isPresent(),
                "El usuario usuario debe existir después de inicialización");
    }

    @Test
    void recetasInicializadasExisten() {
        long count = recetaRepository.count();
        assertTrue(count > 0, "Deben existir recetas inicializadas en la base de datos");
    }

    @Test
    void adminTieneRolAdmin() {
        var adminOpt = usuarioRepository.findByUsername("admin");
        assertTrue(adminOpt.isPresent());
        assertTrue(adminOpt.get().getRoles().contains("ROLE_ADMIN"),
                "El usuario admin debe tener el rol ROLE_ADMIN");
    }

    @Test
    void chefTieneRolChef() {
        var chefOpt = usuarioRepository.findByUsername("chef");
        assertTrue(chefOpt.isPresent());
        assertTrue(chefOpt.get().getRoles().contains("ROLE_CHEF"),
                "El usuario chef debe tener el rol ROLE_CHEF");
    }
}
