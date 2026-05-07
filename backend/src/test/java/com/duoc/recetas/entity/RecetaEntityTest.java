package com.duoc.recetas.entity;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("Tests unitarios - Entidad RecetaEntity")
class RecetaEntityTest {

    @Test
    @DisplayName("Constructor vacío crea receta sin datos")
    void constructorVacio_creaRecetaSinDatos() {
        RecetaEntity receta = new RecetaEntity();
        assertThat(receta.getNombre()).isNull();
        assertThat(receta.getId()).isNull();
        assertThat(receta.isPopular()).isFalse();
    }

    @Test
    @DisplayName("Setters asignan todos los campos correctamente")
    void setters_asignanTodosLosCampos() {
        RecetaEntity receta = new RecetaEntity();
        receta.setId(1L);
        receta.setNombre("Cazuela de Vacuno");
        receta.setTipoCocina("Tradicional");
        receta.setPaisOrigen("Chile");
        receta.setDificultad("Media");
        receta.setTiempoCoccion(60);
        receta.setPopular(true);
        receta.setDescripcionCorta("Cazuela clásica chilena");
        receta.setDescripcionLarga("Descripción larga de la cazuela");
        receta.setIngredientes("Carne|Papas|Zapallo");
        receta.setInstrucciones("Paso 1|Paso 2|Paso 3");
        receta.setImagen("cazuela.jpg");

        assertThat(receta.getId()).isEqualTo(1L);
        assertThat(receta.getNombre()).isEqualTo("Cazuela de Vacuno");
        assertThat(receta.getTipoCocina()).isEqualTo("Tradicional");
        assertThat(receta.getPaisOrigen()).isEqualTo("Chile");
        assertThat(receta.getDificultad()).isEqualTo("Media");
        assertThat(receta.getTiempoCoccion()).isEqualTo(60);
        assertThat(receta.isPopular()).isTrue();
        assertThat(receta.getDescripcionCorta()).isEqualTo("Cazuela clásica chilena");
        assertThat(receta.getIngredientes()).contains("Papas");
        assertThat(receta.getInstrucciones()).contains("Paso 2");
        assertThat(receta.getImagen()).isEqualTo("cazuela.jpg");
    }

    @Test
    @DisplayName("isPopular retorna false por defecto")
    void isPopular_retornaFalsePorDefecto() {
        RecetaEntity receta = new RecetaEntity();
        assertThat(receta.isPopular()).isFalse();
    }

    @Test
    @DisplayName("setPopular cambia el estado correctamente")
    void setPopular_cambiaEstado() {
        RecetaEntity receta = new RecetaEntity();
        receta.setPopular(true);
        assertThat(receta.isPopular()).isTrue();
        receta.setPopular(false);
        assertThat(receta.isPopular()).isFalse();
    }
}
