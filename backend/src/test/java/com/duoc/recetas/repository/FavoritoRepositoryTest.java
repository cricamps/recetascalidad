package com.duoc.recetas.repository;

import com.duoc.recetas.entity.FavoritoEntity;
import com.duoc.recetas.entity.RecetaEntity;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.List;

import static org.assertj.core.api.Assertions.*;

@DataJpaTest
class FavoritoRepositoryTest {

    @Autowired FavoritoRepository favoritoRepository;
    @Autowired RecetaRepository   recetaRepository;

    private RecetaEntity crearReceta(String nombre) {
        RecetaEntity r = new RecetaEntity();
        r.setNombre(nombre);
        r.setTipoCocina("Tradicional");
        r.setPaisOrigen("Chile");
        r.setDificultad("Fácil");
        r.setTiempoCoccion(30);
        r.setDescripcionCorta("Desc");
        r.setDescripcionLarga("Desc larga");
        r.setIngredientes("Ingrediente");
        r.setInstrucciones("Paso 1");
        r.setImagen("img.jpg");
        r.setPopular(false);
        return recetaRepository.save(r);
    }

    @Test
    void saveAndFindByUsername() {
        RecetaEntity receta = crearReceta("Cazuela");
        FavoritoEntity fav = new FavoritoEntity("user1", receta);
        favoritoRepository.save(fav);

        List<FavoritoEntity> result =
                favoritoRepository.findByUsernameOrderByFechaAgregadoDesc("user1");
        assertThat(result).hasSize(1);
        assertThat(result.get(0).getUsername()).isEqualTo("user1");
    }

    @Test
    void existsByUsernameAndRecetaId_true() {
        RecetaEntity receta = crearReceta("Empanada");
        favoritoRepository.save(new FavoritoEntity("user2", receta));

        assertThat(favoritoRepository.existsByUsernameAndRecetaId("user2", receta.getId()))
                .isTrue();
    }

    @Test
    void existsByUsernameAndRecetaId_false() {
        assertThat(favoritoRepository.existsByUsernameAndRecetaId("unknown", 999L))
                .isFalse();
    }

    @Test
    void deleteByUsernameAndRecetaId_removesEntry() {
        RecetaEntity receta = crearReceta("Arroz");
        favoritoRepository.save(new FavoritoEntity("user3", receta));

        favoritoRepository.deleteByUsernameAndRecetaId("user3", receta.getId());

        assertThat(favoritoRepository.existsByUsernameAndRecetaId("user3", receta.getId()))
                .isFalse();
    }

    @Test
    void findByUsernameAndRecetaId_returnsCorrectEntry() {
        RecetaEntity receta = crearReceta("Leche asada");
        favoritoRepository.save(new FavoritoEntity("user4", receta));

        var result = favoritoRepository.findByUsernameAndRecetaId("user4", receta.getId());
        assertThat(result).isPresent();
    }
}
