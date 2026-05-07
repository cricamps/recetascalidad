package com.duoc.recetas.service;

import com.duoc.recetas.entity.RecetaEntity;
import com.duoc.recetas.repository.RecetaRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("Tests unitarios - RecetaService")
class RecetaServiceTest {

    @Mock private RecetaRepository recetaRepository;
    @InjectMocks private RecetaService recetaService;

    @Test
    @DisplayName("getTodas retorna todas las recetas del repositorio")
    void getTodas_retornaListaCompleta() {
        when(recetaRepository.findAll()).thenReturn(List.of(crearReceta(1L, "Cazuela"), crearReceta(2L, "Empanadas")));
        assertThat(recetaService.getTodas()).hasSize(2);
    }

    @Test
    @DisplayName("getPopulares retorna solo recetas marcadas como populares")
    void getPopulares_retornaRecetasPopulares() {
        RecetaEntity popular = crearReceta(1L, "Plateada");
        popular.setPopular(true);
        when(recetaRepository.findByPopularTrue()).thenReturn(List.of(popular));
        assertThat(recetaService.getPopulares().get(0).isPopular()).isTrue();
    }

    @Test
    @DisplayName("getRecientes retorna máximo 6 recetas")
    void getRecientes_retornaMaximo6() {
        List<RecetaEntity> diez = List.of(
            crearReceta(1L,"R1"), crearReceta(2L,"R2"), crearReceta(3L,"R3"),
            crearReceta(4L,"R4"), crearReceta(5L,"R5"), crearReceta(6L,"R6"),
            crearReceta(7L,"R7"), crearReceta(8L,"R8"), crearReceta(9L,"R9"),
            crearReceta(10L,"R10")
        );
        when(recetaRepository.findRecientes()).thenReturn(diez);
        assertThat(recetaService.getRecientes()).hasSize(6);
    }

    @Test
    @DisplayName("getPorId retorna receta cuando existe el ID")
    void getPorId_cuandoExiste_retornaReceta() {
        when(recetaRepository.findById(1L)).thenReturn(Optional.of(crearReceta(1L, "Arroz con Leche")));
        assertThat(recetaService.getPorId(1L)).isPresent();
    }

    @Test
    @DisplayName("getPorId retorna vacío cuando no existe el ID")
    void getPorId_cuandoNoExiste_retornaVacio() {
        when(recetaRepository.findById(99L)).thenReturn(Optional.empty());
        assertThat(recetaService.getPorId(99L)).isEmpty();
    }

    @Test
    @DisplayName("guardar persiste la receta y la retorna")
    void guardar_persisteReceta() {
        RecetaEntity nueva    = crearReceta(null, "Sopa de Tomate");
        RecetaEntity guardada = crearReceta(5L,   "Sopa de Tomate");
        when(recetaRepository.save(nueva)).thenAnswer(inv -> guardada);

        RecetaEntity resultado = recetaService.guardar(nueva);
        Objects.requireNonNull(resultado, "El resultado no debe ser null");
        assertThat(resultado.getId()).isEqualTo(5L);
        verify(recetaRepository, times(1)).save(nueva);
    }

    @Test
    @DisplayName("buscar con filtros vacíos llama al repositorio con nulls")
    void buscar_conFiltrosVacios_pasaNullsAlRepositorio() {
        when(recetaRepository.buscar(null, null, null, null, null)).thenReturn(List.of());
        recetaService.buscar("", "  ", null, "", null);
        verify(recetaRepository).buscar(null, null, null, null, null);
    }

    @Test
    @DisplayName("buscar con filtros válidos los pasa al repositorio")
    void buscar_conFiltrosValidos_losPasaAlRepositorio() {
        when(recetaRepository.buscar("cazuela", "Tradicional", "Chile", "Media", null)).thenReturn(List.of());
        recetaService.buscar("cazuela", "Tradicional", "Chile", "Media", "");
        verify(recetaRepository).buscar("cazuela", "Tradicional", "Chile", "Media", null);
    }

    private RecetaEntity crearReceta(Long id, String nombre) {
        RecetaEntity r = new RecetaEntity();
        r.setId(id);
        r.setNombre(nombre);
        r.setTipoCocina("Tradicional");
        r.setPaisOrigen("Chile");
        r.setDificultad("Fácil");
        r.setTiempoCoccion(30);
        r.setPopular(false);
        return r;
    }
}
