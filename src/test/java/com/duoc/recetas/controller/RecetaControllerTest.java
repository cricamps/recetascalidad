package com.duoc.recetas.controller;

import com.duoc.recetas.entity.RecetaEntity;
import com.duoc.recetas.service.RecetaService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.ui.Model;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("Tests unitarios - RecetaController")
class RecetaControllerTest {

    @Mock
    private RecetaService recetaService;

    @Mock
    private Model model;

    @InjectMocks
    private RecetaController recetaController;

    @Test
    @DisplayName("buscar sin filtros retorna todas las recetas")
    void buscar_sinFiltros_retornaTodasLasRecetas() {
        List<RecetaEntity> recetas = List.of(crearReceta(1L, "Cazuela"));
        when(recetaService.getTodas()).thenReturn(recetas);

        String vista = recetaController.buscar(null, null, null, null, null, model);

        assertThat(vista).isEqualTo("buscar");
        verify(recetaService).getTodas();
        verify(model).addAttribute("resultados", recetas);
    }

    @Test
    @DisplayName("buscar con filtro de nombre usa servicio buscar")
    void buscar_conNombre_usaServicioBuscar() {
        when(recetaService.buscar(anyString(), any(), any(), any(), any()))
            .thenReturn(List.of());

        String vista = recetaController.buscar("cazuela", null, null, null, null, model);

        assertThat(vista).isEqualTo("buscar");
        verify(recetaService).buscar("cazuela", null, null, null, null);
    }

    @Test
    @DisplayName("buscar con filtros en blanco usa getTodas")
    void buscar_conFiltrosEnBlanco_usaGetTodas() {
        when(recetaService.getTodas()).thenReturn(List.of());

        String vista = recetaController.buscar("  ", "  ", "  ", "  ", "  ", model);

        assertThat(vista).isEqualTo("buscar");
        verify(recetaService).getTodas();
    }

    @Test
    @DisplayName("detalleReceta retorna vista detalle cuando la receta existe")
    void detalleReceta_cuandoExiste_retornaDetalle() {
        RecetaEntity receta = crearReceta(1L, "Empanadas");
        receta.setIngredientes("Harina|Mantequilla|Carne");
        receta.setInstrucciones("Paso 1|Paso 2|Paso 3");
        when(recetaService.getPorId(1L)).thenReturn(Optional.of(receta));

        String vista = recetaController.detalleReceta(1L, model);

        assertThat(vista).isEqualTo("detalle");
        verify(model).addAttribute("receta", receta);
    }

    @Test
    @DisplayName("detalleReceta redirige a buscar cuando la receta no existe")
    void detalleReceta_cuandoNoExiste_redirigeBuscar() {
        when(recetaService.getPorId(99L)).thenReturn(Optional.empty());

        String vista = recetaController.detalleReceta(99L, model);

        assertThat(vista).isEqualTo("redirect:/buscar");
    }

    // ── Helper ────────────────────────────────────────────────────────────────
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
