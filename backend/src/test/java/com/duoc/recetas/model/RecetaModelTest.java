package com.duoc.recetas.model;

import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class RecetaTest {

    @Test
    void constructorCompletoYGetters() {
        List<String> ingredientes = Arrays.asList("Arroz", "Agua", "Sal");
        List<String> instrucciones = Arrays.asList("Hervir agua", "Agregar arroz", "Cocinar 20 min");

        Receta receta = new Receta(
                1, "Arroz Graneado", "Tradicional", "Chile",
                "Fácil", 25, "Descripción corta", "Descripción larga",
                ingredientes, instrucciones, "arroz.jpg", true
        );

        assertEquals(1, receta.getId());
        assertEquals("Arroz Graneado", receta.getNombre());
        assertEquals("Tradicional", receta.getTipoCocina());
        assertEquals("Chile", receta.getPaisOrigen());
        assertEquals("Fácil", receta.getDificultad());
        assertEquals(25, receta.getTiempoCoccion());
        assertEquals("Descripción corta", receta.getDescripcionCorta());
        assertEquals("Descripción larga", receta.getDescripcionLarga());
        assertEquals(ingredientes, receta.getIngredientes());
        assertEquals(instrucciones, receta.getInstrucciones());
        assertEquals("arroz.jpg", receta.getImagen());
        assertTrue(receta.isPopular());
    }

    @Test
    void constructorVacioYSetters() {
        Receta receta = new Receta();
        receta.setId(2);
        receta.setNombre("Cazuela");
        receta.setTipoCocina("Tradicional");
        receta.setPaisOrigen("Chile");
        receta.setDificultad("Media");
        receta.setTiempoCoccion(60);
        receta.setDescripcionCorta("Cazuela casera");
        receta.setDescripcionLarga("Descripción detallada");
        receta.setIngredientes(List.of("Pollo", "Papas"));
        receta.setInstrucciones(List.of("Hervir", "Servir"));
        receta.setImagen("cazuela.jpg");
        receta.setPopular(false);

        assertEquals(2, receta.getId());
        assertEquals("Cazuela", receta.getNombre());
        assertFalse(receta.isPopular());
        assertEquals(60, receta.getTiempoCoccion());
    }

    @Test
    void recetaNoPopularPorDefecto() {
        Receta receta = new Receta();
        assertFalse(receta.isPopular());
    }

    @Test
    void ingredientesYInstruccionesSonListas() {
        Receta receta = new Receta();
        receta.setIngredientes(Arrays.asList("A", "B", "C"));
        receta.setInstrucciones(Arrays.asList("Paso 1", "Paso 2"));

        assertEquals(3, receta.getIngredientes().size());
        assertEquals(2, receta.getInstrucciones().size());
    }
}
