package com.duoc.recetas.service;

import com.duoc.recetas.dto.RegistroRequest;
import com.duoc.recetas.entity.Usuario;
import com.duoc.recetas.repository.UsuarioRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UsuarioService {

    private final UsuarioRepository usuarioRepository;
    private final PasswordEncoder passwordEncoder;

    public UsuarioService(UsuarioRepository usuarioRepository,
                           PasswordEncoder passwordEncoder) {
        this.usuarioRepository = usuarioRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public boolean registrar(RegistroRequest request) {
        if (usuarioRepository.findByUsername(request.getUsername()).isPresent()) {
            return false;
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
