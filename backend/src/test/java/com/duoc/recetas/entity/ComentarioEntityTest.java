package com.duoc.recetas.entity;

import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

class ComentarioEntityTest {

    @Test
    void constructorVacioYGettersSetters() {
        ComentarioEntity comentario = new ComentarioEntity();
        RecetaEntity receta = new RecetaEntity();

        comentario.setId(1L);
        comentario.setReceta(receta);
        comentario.setAutor("Juan Pérez");
        comentario.setContenido("Excelente receta, muy fácil de hacer");
        comentario.setValoracion(5);

        LocalDateTime ahora = LocalDateTime.now();
        comentario.setFechaCreacion(ahora);

        assertEquals(1L, comentario.getId());
        assertEquals(receta, comentario.getReceta());
        assertEquals("Juan Pérez", comentario.getAutor());
        assertEquals("Excelente receta, muy fácil de hacer", comentario.getContenido());
        assertEquals(5, comentario.getValoracion());
        assertEquals(ahora, comentario.getFechaCreacion());
    }

    @Test
    void valoracionMinima() {
        ComentarioEntity comentario = new ComentarioEntity();
        comentario.setValoracion(1);
        assertEquals(1, comentario.getValoracion());
    }

    @Test
    void valoracionMaxima() {
        ComentarioEntity comentario = new ComentarioEntity();
        comentario.setValoracion(5);
        assertEquals(5, comentario.getValoracion());
    }

    @Test
    void autorNoNulo() {
        ComentarioEntity comentario = new ComentarioEntity();
        comentario.setAutor("María");
        assertNotNull(comentario.getAutor());
        assertEquals("María", comentario.getAutor());
    }

    @Test
    void contenidoNoNulo() {
        ComentarioEntity comentario = new ComentarioEntity();
        comentario.setContenido("Muy buena receta");
        assertNotNull(comentario.getContenido());
    }
}
