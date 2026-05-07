package com.duoc.recetas.repository;

import com.duoc.recetas.entity.MedioRecetaEntity;
import com.duoc.recetas.entity.RecetaEntity;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@ActiveProfiles("test")
class MedioRecetaRepositoryTest {

    @Autowired
    private MedioRecetaRepository medioRepository;

    @Autowired
    private RecetaRepository recetaRepository;

    private RecetaEntity recetaGuardada;

    @BeforeEach
    void setUp() {
        RecetaEntity receta = new RecetaEntity();
        receta.setNombre("Cazuela Test");
        receta.setTipoCocina("Tradicional");
        receta.setPaisOrigen("Chile");
        receta.setDificultad("Media");
        receta.setTiempoCoccion(60);
        receta.setPopular(false);
        receta.setDescripcionCorta("Desc corta");
        receta.setDescripcionLarga("Desc larga");
        receta.setIngredientes("Pollo|Papas");
        receta.setInstrucciones("Hervir|Servir");
        receta.setImagen("cazuela.jpg");
        recetaGuardada = recetaRepository.save(receta);
    }

    @Test
    void guardarYRecuperarMedio() {
        MedioRecetaEntity medio = new MedioRecetaEntity();
        medio.setReceta(recetaGuardada);
        medio.setTipo("FOTO");
        medio.setNombreArchivo("foto123.jpg");
        medio.setSubidoPor("chef");

        MedioRecetaEntity guardado = medioRepository.save(medio);

        assertNotNull(guardado.getId());
        assertEquals("FOTO", guardado.getTipo());
        assertEquals("foto123.jpg", guardado.getNombreArchivo());
    }

    @Test
    void findByRecetaIdRetornaMedios() {
        MedioRecetaEntity medio = new MedioRecetaEntity();
        medio.setReceta(recetaGuardada);
        medio.setTipo("VIDEO");
        medio.setNombreArchivo("video123.mp4");
        medio.setSubidoPor("admin");
        medioRepository.save(medio);

        List<MedioRecetaEntity> medios = medioRepository
                .findByRecetaIdOrderByFechaSubidaDesc(recetaGuardada.getId());

        assertEquals(1, medios.size());
        assertEquals("VIDEO", medios.get(0).getTipo());
    }

    @Test
    void findByRecetaIdInexistenteRetornaVacio() {
        List<MedioRecetaEntity> medios = medioRepository
                .findByRecetaIdOrderByFechaSubidaDesc(9999L);
        assertTrue(medios.isEmpty());
    }
}
