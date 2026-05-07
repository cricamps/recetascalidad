package com.duoc.recetas.repository;

import com.duoc.recetas.entity.FavoritoEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface FavoritoRepository extends JpaRepository<FavoritoEntity, Long> {

    List<FavoritoEntity> findByUsernameOrderByFechaAgregadoDesc(String username);

    Optional<FavoritoEntity> findByUsernameAndRecetaId(String username, Long recetaId);

    boolean existsByUsernameAndRecetaId(String username, Long recetaId);

    void deleteByUsernameAndRecetaId(String username, Long recetaId);
}
