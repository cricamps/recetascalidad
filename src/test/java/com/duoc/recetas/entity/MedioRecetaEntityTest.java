package com.duoc.recetas.entity;

import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

class MedioRecetaEntityTest {

    @Test
    void constructorVacioYGettersSetters() {
        MedioRecetaEntity medio = new MedioRecetaEntity();
        RecetaEntity receta = new RecetaEntity();

        medio.setId(1L);
        medio.setReceta(receta);
        medio.setTipo("FOTO");
        medio.setNombreArchivo("foto_receta.jpg");
        medio.setSubidoPor("chef");

        LocalDateTime ahora = LocalDateTime.now();
        medio.setFechaSubida(ahora);

        assertEquals(1L, medio.getId());
        assertEquals(receta, medio.getReceta());
        assertEquals("FOTO", medio.getTipo());
        assertEquals("foto_receta.jpg", medio.getNombreArchivo());
        assertEquals("chef", medio.getSubidoPor());
        assertEquals(ahora, medio.getFechaSubida());
    }

    @Test
    void tipoFoto() {
        MedioRecetaEntity medio = new MedioRecetaEntity();
        medio.setTipo("FOTO");
        assertEquals("FOTO", medio.getTipo());
    }

    @Test
    void tipoVideo() {
        MedioRecetaEntity medio = new MedioRecetaEntity();
        medio.setTipo("VIDEO");
        assertEquals("VIDEO", medio.getTipo());
    }

    @Test
    void nombreArchivoSetCorrectamente() {
        MedioRecetaEntity medio = new MedioRecetaEntity();
        medio.setNombreArchivo("uuid-generado.mp4");
        assertNotNull(medio.getNombreArchivo());
        assertEquals("uuid-generado.mp4", medio.getNombreArchivo());
    }

    @Test
    void subidoPorSetCorrectamente() {
        MedioRecetaEntity medio = new MedioRecetaEntity();
        medio.setSubidoPor("usuario123");
        assertEquals("usuario123", medio.getSubidoPor());
    }
}
