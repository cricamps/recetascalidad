package com.duoc.recetas.service;

import com.duoc.recetas.entity.ComentarioEntity;
import com.duoc.recetas.repository.ComentarioRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Service
public class ModerationService {

    private final ComentarioRepository comentarioRepository;

    public ModerationService(ComentarioRepository comentarioRepository) {
        this.comentarioRepository = comentarioRepository;
    }

    public List<ComentarioEntity> listarTodos() {
        return comentarioRepository.findAll()
                .stream()
                .sorted((a, b) -> b.getFechaCreacion().compareTo(a.getFechaCreacion()))
                .toList();
    }

    public void eliminar(Long id) {
        Objects.requireNonNull(id, "id no puede ser null");
        comentarioRepository.deleteById(id);
    }

    public Optional<ComentarioEntity> buscarPorId(Long id) {
        Objects.requireNonNull(id, "id no puede ser null");
        return comentarioRepository.findById(id);
    }
}
