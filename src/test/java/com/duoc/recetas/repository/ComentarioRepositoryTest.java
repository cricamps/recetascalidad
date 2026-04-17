package com.duoc.recetas.repository;

import com.duoc.recetas.entity.ComentarioEntity;
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
class ComentarioRepositoryTest {

    @Autowired
    private ComentarioRepository comentarioRepository;

    @Autowired
    private RecetaRepository recetaRepository;

    private RecetaEntity recetaGuardada;

    @BeforeEach
    void setUp() {
        RecetaEntity receta = new RecetaEntity();
        receta.setNombre("Arroz Test");
        receta.setTipoCocina("Tradicional");
        receta.setPaisOrigen("Chile");
        receta.setDificultad("Fácil");
        receta.setTiempoCoccion(20);
        receta.setPopular(true);
        receta.setDescripcionCorta("Desc corta");
        receta.setDescripcionLarga("Desc larga");
        receta.setIngredientes("Arroz|Agua");
        receta.setInstrucciones("Hervir|Servir");
        receta.setImagen("arroz.jpg");
        recetaGuardada = recetaRepository.save(receta);
    }

    @Test
    void guardarYRecuperarComentario() {
        ComentarioEntity comentario = new ComentarioEntity();
        comentario.setReceta(recetaGuardada);
        comentario.setAutor("Ana");
        comentario.setContenido("Deliciosa");
        comentario.setValoracion(5);

        ComentarioEntity guardado = comentarioRepository.save(comentario);

        assertNotNull(guardado.getId());
        assertEquals("Ana", guardado.getAutor());
    }

    @Test
    void findByRecetaIdRetornaComentariosOrdenados() {
        ComentarioEntity c1 = new ComentarioEntity();
        c1.setReceta(recetaGuardada);
        c1.setAutor("Juan");
        c1.setContenido("Buena");
        c1.setValoracion(4);
        comentarioRepository.save(c1);

        ComentarioEntity c2 = new ComentarioEntity();
        c2.setReceta(recetaGuardada);
        c2.setAutor("María");
        c2.setContenido("Excelente");
        c2.setValoracion(5);
        comentarioRepository.save(c2);

        List<ComentarioEntity> comentarios = comentarioRepository
                .findByRecetaIdOrderByFechaCreacionDesc(recetaGuardada.getId());

        assertEquals(2, comentarios.size());
    }

    @Test
    void findByRecetaIdSinComentariosRetornaListaVacia() {
        List<ComentarioEntity> comentarios = comentarioRepository
                .findByRecetaIdOrderByFechaCreacionDesc(9999L);
        assertTrue(comentarios.isEmpty());
    }
}
