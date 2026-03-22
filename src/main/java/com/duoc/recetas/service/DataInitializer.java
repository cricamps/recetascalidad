package com.duoc.recetas.service;

import com.duoc.recetas.entity.Usuario;
import com.duoc.recetas.repository.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

/**
 * Inicializa los 3 usuarios requeridos en la BD al arrancar la app.
 * Si ya existen, no los duplica.
 */
@Component
public class DataInitializer implements CommandLineRunner {

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public void run(String... args) {
        crearSiNoExiste("usuario", "usuario123", "ROLE_USER");
        crearSiNoExiste("chef",    "chef123",    "ROLE_USER,ROLE_CHEF");
        crearSiNoExiste("admin",   "admin123",   "ROLE_USER,ROLE_ADMIN");
        System.out.println("✅ Usuarios inicializados en la base de datos.");
    }

    private void crearSiNoExiste(String username, String rawPassword, String roles) {
        if (usuarioRepository.findByUsername(username).isEmpty()) {
            usuarioRepository.save(new Usuario(username, passwordEncoder.encode(rawPassword), roles));
        }
    }
}
