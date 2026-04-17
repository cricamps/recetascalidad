package com.duoc.recetas.service;

import com.duoc.recetas.entity.Usuario;
import com.duoc.recetas.repository.UsuarioRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@SuppressWarnings("null")
public class AdminService {

    private final UsuarioRepository usuarioRepository;
    private final PasswordEncoder passwordEncoder;

    public AdminService(UsuarioRepository usuarioRepository,
                        PasswordEncoder passwordEncoder) {
        this.usuarioRepository = usuarioRepository;
        this.passwordEncoder   = passwordEncoder;
    }

    public List<Usuario> listarUsuarios() {
        return usuarioRepository.findAll();
    }

    public Optional<Usuario> buscarPorId(Long id) {
        return usuarioRepository.findById(id);
    }

    public void cambiarRol(Long id, String nuevoRol) {
        usuarioRepository.findById(id).ifPresent(u -> {
            u.setRoles(nuevoRol);
            usuarioRepository.save(u);
        });
    }

    public void resetearPassword(Long id, String nuevaPassword) {
        usuarioRepository.findById(id).ifPresent(u -> {
            u.setPassword(passwordEncoder.encode(nuevaPassword));
            usuarioRepository.save(u);
        });
    }

    public void eliminarUsuario(Long id) {
        usuarioRepository.deleteById(id);
    }

    public void crearUsuario(String username, String password, String roles,
                              String nombreCompleto, String correo) {
        if (usuarioRepository.findByUsername(username).isPresent()) {
            throw new IllegalArgumentException("El usuario '" + username + "' ya existe.");
        }
        Usuario nuevo = new Usuario(
            username,
            passwordEncoder.encode(password),
            roles,
            nombreCompleto,
            correo
        );
        usuarioRepository.save(nuevo);
    }
}
