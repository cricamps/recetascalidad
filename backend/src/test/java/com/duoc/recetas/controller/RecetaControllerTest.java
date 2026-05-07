package com.duoc.recetas.controller;

import com.duoc.recetas.entity.RecetaEntity;
import com.duoc.recetas.service.FavoritoService;
import com.duoc.recetas.service.InteraccionService;
import com.duoc.recetas.service.RecetaService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.Authentication;
import org.springframework.ui.Model;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("Tests unitarios - RecetaController")
class RecetaControllerTest {

    @Mock private RecetaService      recetaService;
    @Mock private InteraccionService interaccionService;
    @Mock private FavoritoService    favoritoService;
    @Mock private Model              model;
    @Mock private Authentication     auth;

    @InjectMocks private RecetaController recetaController;

    @Test
    @DisplayName("buscar sin filtros retorna todas las recetas")
    void buscar_sinFiltros_retornaTodasLasRecetas() {
        List<RecetaEntity> recetas = List.of(crearReceta(1L, "Cazuela"));
        when(recetaService.getTodas()).thenReturn(recetas);
        String vista = recetaController.buscar(null, null, null, null, null, model);
        assertThat(vista).isEqualTo("buscar");
        verify(model).addAttribute(eq("resultados"), eq(recetas));
    }

    @Test
    @DisplayName("buscar con filtro de nombre usa servicio buscar")
    void buscar_conNombre_usaServicioBuscar() {
        when(recetaService.buscar(anyString(), any(), any(), any(), any())).thenReturn(List.of());
        assertThat(recetaController.buscar("cazuela", null, null, null, null, model)).isEqualTo("buscar");
        verify(recetaService).buscar("cazuela", null, null, null, null);
    }

    @Test
    @DisplayName("buscar con filtros en blanco usa getTodas")
    void buscar_conFiltrosEnBlanco_usaGetTodas() {
        when(recetaService.getTodas()).thenReturn(List.of());
        assertThat(recetaController.buscar("  ", "  ", "  ", "  ", "  ", model)).isEqualTo("buscar");
        verify(recetaService).getTodas();
    }

    @Test
    @DisplayName("detalleReceta retorna vista detalle cuando la receta existe (sin auth)")
    void detalleReceta_cuandoExiste_sinAuth_retornaDetalle() {
        RecetaEntity receta = crearReceta(1L, "Empanadas");
        receta.setIngredientes("Harina|Mantequilla");
        receta.setInstrucciones("Paso 1|Paso 2");
        when(recetaService.getPorId(1L)).thenReturn(Optional.of(receta));
        when(interaccionService.getComentariosPorReceta(1L)).thenReturn(List.of());
        when(interaccionService.getMediosPorReceta(1L)).thenReturn(List.of());

        String vista = recetaController.detalleReceta(1L, null, model);

        assertThat(vista).isEqualTo("detalle");
        verify(model).addAttribute(eq("receta"), eq(receta));
        verify(model).addAttribute(eq("esFavorito"), eq(false));
    }

    @Test
    @DisplayName("detalleReceta retorna vista detalle cuando la receta existe (con auth)")
    void detalleReceta_cuandoExiste_conAuth_retornaDetalle() {
        RecetaEntity receta = crearReceta(2L, "Cazuela");
        receta.setIngredientes("Pollo|Papas");
        receta.setInstrucciones("Hervir|Servir");
        when(auth.getName()).thenReturn("usuario");
        when(recetaService.getPorId(2L)).thenReturn(Optional.of(receta));
        when(interaccionService.getComentariosPorReceta(2L)).thenReturn(List.of());
        when(interaccionService.getMediosPorReceta(2L)).thenReturn(List.of());
        when(favoritoService.esFavorito("usuario", 2L)).thenReturn(true);

        String vista = recetaController.detalleReceta(2L, auth, model);

        assertThat(vista).isEqualTo("detalle");
        verify(model).addAttribute(eq("esFavorito"), eq(true));
    }

    @Test
    @DisplayName("detalleReceta redirige a buscar cuando la receta no existe")
    void detalleReceta_cuandoNoExiste_redirigeBuscar() {
        when(recetaService.getPorId(99L)).thenReturn(Optional.empty());
        assertThat(recetaController.detalleReceta(99L, null, model)).isEqualTo("redirect:/buscar");
    }

    private RecetaEntity crearReceta(Long id, String nombre) {
        RecetaEntity r = new RecetaEntity();
        r.setId(id);
        r.setNombre(nombre);
        r.setTipoCocina("Tradicional");
        r.setPaisOrigen("Chile");
        r.setDificultad("Media");
        r.setTiempoCoccion(30);
        return r;
    }
}
