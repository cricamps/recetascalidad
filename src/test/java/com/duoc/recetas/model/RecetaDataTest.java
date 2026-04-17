package com.duoc.recetas.model;

import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class RecetaDataTest {

    private final RecetaData recetaData = new RecetaData();

    @Test
    void getTodasLasRecetasRetornaListaNoVacia() {
        List<Receta> recetas = recetaData.getTodasLasRecetas();
        assertNotNull(recetas);
        assertFalse(recetas.isEmpty());
    }

    @Test
    void getRecetasPopularesRetornaSoloPopulares() {
        List<Receta> populares = recetaData.getRecetasPopulares();
        assertNotNull(populares);
        for (Receta r : populares) {
            assertTrue(r.isPopular(), "Todas las recetas deben ser populares");
        }
    }

    @Test
    void getRecetaPorIdExistenteRetornaReceta() {
        Receta receta = recetaData.getRecetaPorId(1);
        assertNotNull(receta);
        assertEquals(1, receta.getId());
    }

    @Test
    void getRecetaPorIdInexistenteRetornaNull() {
        Receta receta = recetaData.getRecetaPorId(9999);
        assertNull(receta);
    }

    @Test
    void getRecetasRecientesRetornaMaximo6() {
        List<Receta> recientes = recetaData.getRecetasRecientes();
        assertNotNull(recientes);
        assertTrue(recientes.size() <= 6);
    }

    @Test
    void buscarRecetasPorNombreRetornaCoincidencias() {
        List<Receta> resultado = recetaData.buscarRecetas("arroz", null, null, null, null);
        assertNotNull(resultado);
        for (Receta r : resultado) {
            assertTrue(r.getNombre().toLowerCase().contains("arroz"));
        }
    }

    @Test
    void buscarRecetasSinFiltrosRetornaTodasLasRecetas() {
        List<Receta> todas = recetaData.getTodasLasRecetas();
        List<Receta> resultado = recetaData.buscarRecetas(null, null, null, null, null);
        assertEquals(todas.size(), resultado.size());
    }

    @Test
    void buscarRecetasPorDificultadRetornaCoincidencias() {
        List<Receta> resultado = recetaData.buscarRecetas(null, null, null, null, "Fácil");
        for (Receta r : resultado) {
            assertEquals("Fácil", r.getDificultad());
        }
    }

    @Test
    void buscarRecetasPorTipoCocinaRetornaCoincidencias() {
        List<Receta> resultado = recetaData.buscarRecetas(null, "Tradicional", null, null, null);
        for (Receta r : resultado) {
            assertEquals("Tradicional", r.getTipoCocina());
        }
    }
}
