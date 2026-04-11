package com.duoc.recetas.repository;

import com.duoc.recetas.entity.MedioRecetaEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface MedioRecetaRepository extends JpaRepository<MedioRecetaEntity, Long> {
    List<MedioRecetaEntity> findByRecetaIdOrderByFechaSubidaDesc(Long recetaId);
}
