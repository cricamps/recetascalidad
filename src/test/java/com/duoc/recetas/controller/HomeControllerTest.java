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

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("Tests unitarios - HomeController")
class HomeControllerTest {

    @Mock
    private RecetaService recetaService;

    @Mock
    private Model model;

    @InjectMocks
    private HomeController homeController;

    @Test
    @DisplayName("home retorna vista 'home' con recetas recientes y populares")
    void home_retornaVistaHomeConRecetas() {
        List<RecetaEntity> recientes = List.of(crearReceta(1L, "Cazuela"));
        List<RecetaEntity> populares = List.of(crearReceta(2L, "Empanadas"));
        when(recetaService.getRecientes()).thenReturn(recientes);
        when(recetaService.getPopulares()).thenReturn(populares);

        String vista = homeController.home(model);

        assertThat(vista).isEqualTo("home");
        verify(model).addAttribute("recetasRecientes", recientes);
        verify(model).addAttribute("recetasPopulares", populares);
    }

    @Test
    @DisplayName("login retorna vista 'login'")
    void login_retornaVistaLogin() {
        String vista = homeController.login();
        assertThat(vista).isEqualTo("login");
    }

    @Test
    @DisplayName("home llama a getRecientes y getPopulares del servicio")
    void home_llamaAmbosMetodosDelServicio() {
        when(recetaService.getRecientes()).thenReturn(List.of());
        when(recetaService.getPopulares()).thenReturn(List.of());

        homeController.home(model);

        verify(recetaService, times(1)).getRecientes();
        verify(recetaService, times(1)).getPopulares();
    }

    // ── Helper ────────────────────────────────────────────────────────────────
    private RecetaEntity crearReceta(Long id, String nombre) {
        RecetaEntity r = new RecetaEntity();
        r.setId(id);
        r.setNombre(nombre);
        return r;
    }
}
