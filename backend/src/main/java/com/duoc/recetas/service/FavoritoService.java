package com.duoc.recetas.service;

import com.duoc.recetas.entity.FavoritoEntity;
import com.duoc.recetas.entity.RecetaEntity;
import com.duoc.recetas.repository.FavoritoRepository;
import com.duoc.recetas.repository.RecetaRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Objects;

@Service
public class FavoritoService {

    private final FavoritoRepository favoritoRepository;
    private final RecetaRepository   recetaRepository;

    public FavoritoService(FavoritoRepository favoritoRepository,
                           RecetaRepository recetaRepository) {
        this.favoritoRepository = favoritoRepository;
        this.recetaRepository   = recetaRepository;
    }

    public List<FavoritoEntity> getFavoritos(String username) {
        return favoritoRepository.findByUsernameOrderByFechaAgregadoDesc(username);
    }

    public boolean esFavorito(String username, Long recetaId) {
        Objects.requireNonNull(recetaId, "recetaId no puede ser null");
        return favoritoRepository.existsByUsernameAndRecetaId(username, recetaId);
    }

    @Transactional
    public void agregar(String username, Long recetaId) {
        Objects.requireNonNull(recetaId, "recetaId no puede ser null");
        if (favoritoRepository.existsByUsernameAndRecetaId(username, recetaId)) {
            return;
        }
        RecetaEntity receta = recetaRepository.findById(recetaId)
                .orElseThrow(() -> new IllegalArgumentException("Receta no encontrada: " + recetaId));
        favoritoRepository.save(new FavoritoEntity(username, receta));
    }

    @Transactional
    public void eliminar(String username, Long recetaId) {
        Objects.requireNonNull(recetaId, "recetaId no puede ser null");
        favoritoRepository.deleteByUsernameAndRecetaId(username, recetaId);
    }

    @Transactional
    public boolean toggle(String username, Long recetaId) {
        Objects.requireNonNull(recetaId, "recetaId no puede ser null");
        if (favoritoRepository.existsByUsernameAndRecetaId(username, recetaId)) {
            favoritoRepository.deleteByUsernameAndRecetaId(username, recetaId);
            return false;
        } else {
            RecetaEntity receta = recetaRepository.findById(recetaId)
                    .orElseThrow(() -> new IllegalArgumentException("Receta no encontrada: " + recetaId));
            favoritoRepository.save(new FavoritoEntity(username, receta));
            return true;
        }
    }
}
