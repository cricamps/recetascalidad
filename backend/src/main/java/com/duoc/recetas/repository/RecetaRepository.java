package com.duoc.recetas.repository;

import com.duoc.recetas.entity.RecetaEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RecetaRepository extends JpaRepository<RecetaEntity, Long> {

    List<RecetaEntity> findByPopularTrue();

    @Query("SELECT r FROM RecetaEntity r ORDER BY r.id DESC")
    List<RecetaEntity> findRecientes();

    @Query("""
        SELECT r FROM RecetaEntity r
        WHERE (:nombre IS NULL OR LOWER(r.nombre) LIKE LOWER(CONCAT('%', :nombre, '%')))
        AND (:tipoCocina IS NULL OR r.tipoCocina = :tipoCocina)
        AND (:paisOrigen IS NULL OR r.paisOrigen = :paisOrigen)
        AND (:dificultad IS NULL OR r.dificultad = :dificultad)
        AND (:ingrediente IS NULL OR LOWER(r.ingredientes) LIKE LOWER(CONCAT('%', :ingrediente, '%')))
    """)
    List<RecetaEntity> buscar(
        @Param("nombre") String nombre,
        @Param("tipoCocina") String tipoCocina,
        @Param("paisOrigen") String paisOrigen,
        @Param("dificultad") String dificultad,
        @Param("ingrediente") String ingrediente
    );
}
