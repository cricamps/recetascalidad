package com.duoc.recetas.repository;

import com.duoc.recetas.entity.ComentarioEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface ComentarioRepository extends JpaRepository<ComentarioEntity, Long> {
    List<ComentarioEntity> findByRecetaIdOrderByFechaCreacionDesc(Long recetaId);
}
