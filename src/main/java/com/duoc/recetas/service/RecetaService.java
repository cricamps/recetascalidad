package com.duoc.recetas.service;

import com.duoc.recetas.entity.RecetaEntity;
import com.duoc.recetas.repository.RecetaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Service
public class RecetaService {

    @Autowired
    private RecetaRepository recetaRepository;

    public List<RecetaEntity> getTodas() {
        return recetaRepository.findAll();
    }

    public List<RecetaEntity> getPopulares() {
        return recetaRepository.findByPopularTrue();
    }

    public List<RecetaEntity> getRecientes() {
        List<RecetaEntity> todas = recetaRepository.findRecientes();
        return todas.subList(0, Math.min(6, todas.size()));
    }

    public Optional<RecetaEntity> getPorId(Long id) {
        // Línea 31: findById recibe @NonNull Long en Spring Data.
        // Objects.requireNonNull satisface al compilador JDT sin cambiar la lógica.
        Objects.requireNonNull(id, "id no puede ser null");
        return recetaRepository.findById(id);
    }

    public List<RecetaEntity> buscar(String nombre, String tipoCocina,
                                      String paisOrigen, String dificultad,
                                      String ingrediente) {
        String n = (nombre    != null && !nombre.isBlank())    ? nombre    : null;
        String t = (tipoCocina != null && !tipoCocina.isBlank()) ? tipoCocina : null;
        String p = (paisOrigen != null && !paisOrigen.isBlank()) ? paisOrigen : null;
        String d = (dificultad != null && !dificultad.isBlank()) ? dificultad : null;
        String i = (ingrediente != null && !ingrediente.isBlank()) ? ingrediente : null;
        return recetaRepository.buscar(n, t, p, d, i);
    }

    public RecetaEntity guardar(RecetaEntity receta) {
        // Línea 46: save() recibe @NonNull — verificamos explícitamente para el compilador.
        Objects.requireNonNull(receta, "receta no puede ser null");
        return recetaRepository.save(receta);
    }
}
