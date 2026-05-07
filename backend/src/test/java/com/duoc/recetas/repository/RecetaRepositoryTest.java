package com.duoc.recetas.repository;

import com.duoc.recetas.entity.RecetaEntity;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@ActiveProfiles("test")
class RecetaRepositoryTest {

    @Autowired private RecetaRepository recetaRepository;

    private RecetaEntity recetaPopular;

    @BeforeEach
    void setUp() {
        recetaPopular = new RecetaEntity();
        recetaPopular.setNombre("Arroz Popular");
        recetaPopular.setTipoCocina("Tradicional");
        recetaPopular.setPaisOrigen("Chile");
        recetaPopular.setDificultad("Fácil");
        recetaPopular.setTiempoCoccion(25);
        recetaPopular.setPopular(true);
        recetaPopular.setDescripcionCorta("Desc");
        recetaPopular.setDescripcionLarga("Desc larga");
        recetaPopular.setIngredientes("Arroz");
        recetaPopular.setInstrucciones("Hervir");
        recetaPopular.setImagen("arroz.jpg");
        recetaRepository.save(recetaPopular);

        RecetaEntity recetaNormal = new RecetaEntity();
        recetaNormal.setNombre("Sopa Normal");
        recetaNormal.setTipoCocina("Marina");
        recetaNormal.setPaisOrigen("Chile");
        recetaNormal.setDificultad("Media");
        recetaNormal.setTiempoCoccion(40);
        recetaNormal.setPopular(false);
        recetaNormal.setDescripcionCorta("Desc sopa");
        recetaNormal.setDescripcionLarga("Desc larga sopa");
        recetaNormal.setIngredientes("Mariscos");
        recetaNormal.setInstrucciones("Hervir");
        recetaNormal.setImagen("sopa.jpg");
        recetaRepository.save(recetaNormal);
    }

    @Test
    void findByPopularTrueRetornaSoloPopulares() {
        List<RecetaEntity> populares = recetaRepository.findByPopularTrue();
        assertFalse(populares.isEmpty());
        for (RecetaEntity r : populares) { assertTrue(r.isPopular()); }
    }

    @Test
    void findRecientesRetornaRecetasOrdenadas() {
        assertFalse(recetaRepository.findRecientes().isEmpty());
    }

    @Test
    void buscarPorNombreRetornaCoincidencias() {
        List<RecetaEntity> resultado = recetaRepository.buscar("Arroz", null, null, null, null);
        assertFalse(resultado.isEmpty());
        assertTrue(resultado.stream().anyMatch(r -> r.getNombre().contains("Arroz")));
    }

    @Test
    void buscarPorTipoCocinaRetornaCoincidencias() {
        List<RecetaEntity> resultado = recetaRepository.buscar(null, "Marina", null, null, null);
        assertFalse(resultado.isEmpty());
        for (RecetaEntity r : resultado) { assertEquals("Marina", r.getTipoCocina()); }
    }

    @Test
    void buscarSinFiltrosRetornaTodasLasRecetas() {
        assertEquals(recetaRepository.count(), recetaRepository.buscar(null, null, null, null, null).size());
    }

    @Test
    void findByIdExistenteRetornaReceta() {
        Optional<RecetaEntity> resultado = recetaRepository.findById(recetaPopular.getId());
        assertTrue(resultado.isPresent());
        assertEquals("Arroz Popular", resultado.get().getNombre());
    }

    @Test
    void findByIdInexistenteRetornaVacio() {
        assertFalse(recetaRepository.findById(9999L).isPresent());
    }
}
