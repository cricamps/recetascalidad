package com.duoc.recetas.service;

import com.duoc.recetas.dto.RegistroRequest;
import com.duoc.recetas.entity.Usuario;
import com.duoc.recetas.repository.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UsuarioService {

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    /**
     * Registra un nuevo usuario. Devuelve true si el registro fue exitoso,
     * false si el username ya existe.
     */
    public boolean registrar(RegistroRequest request) {
        if (usuarioRepository.findByUsername(request.getUsername()).isPresent()) {
            return false; // username ocupado
        }
        Usuario nuevo = new Usuario(
            request.getUsername(),
            passwordEncoder.encode(request.getPassword()),
            "ROLE_USER",
            request.getNombreCompleto(),
            request.getCorreo()
        );
        usuarioRepository.save(nuevo);
        return true;
    }
}
