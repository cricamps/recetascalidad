package com.duoc.recetas.service;

import com.duoc.recetas.entity.ComentarioEntity;
import com.duoc.recetas.entity.RecetaEntity;
import com.duoc.recetas.repository.ComentarioRepository;
import com.duoc.recetas.repository.MedioRecetaRepository;
import com.duoc.recetas.repository.RecetaRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class InteraccionServiceTest {

    @Mock
    private ComentarioRepository comentarioRepository;

    @Mock
    private MedioRecetaRepository medioRepository;

    @Mock
    private RecetaRepository recetaRepository;

    @InjectMocks
    private InteraccionService interaccionService;

    private RecetaEntity recetaMock;

    @BeforeEach
    void setUp() {
        recetaMock = new RecetaEntity();
        recetaMock.setNombre("Arroz");
    }

    @Test
    void getComentariosPorRecetaRetornaLista() {
        ComentarioEntity comentario = new ComentarioEntity();
        comentario.setAutor("Juan");
        comentario.setContenido("Rico");
        comentario.setValoracion(5);

        when(comentarioRepository.findByRecetaIdOrderByFechaCreacionDesc(1L))
                .thenReturn(List.of(comentario));

        List<ComentarioEntity> result = interaccionService.getComentariosPorReceta(1L);

        assertEquals(1, result.size());
        assertEquals("Juan", result.get(0).getAutor());
    }

    @Test
    void agregarComentarioGuardaCorrectamente() {
        when(recetaRepository.findById(1L)).thenReturn(Optional.of(recetaMock));

        interaccionService.agregarComentario(1L, "Ana", "Excelente receta", 4);

        verify(comentarioRepository, times(1)).save(any(ComentarioEntity.class));
    }

    @Test
    void agregarComentarioConContenidoVacioLanzaExcepcion() {
        assertThrows(IllegalArgumentException.class, () ->
                interaccionService.agregarComentario(1L, "Ana", "", 3));
    }

    @Test
    void agregarComentarioConValoracionFueraDeRangoLanzaExcepcion() {
        assertThrows(IllegalArgumentException.class, () ->
                interaccionService.agregarComentario(1L, "Ana", "Buena receta", 6));
    }

    @Test
    void agregarComentarioConValoracionMenorA1LanzaExcepcion() {
        assertThrows(IllegalArgumentException.class, () ->
                interaccionService.agregarComentario(1L, "Ana", "Buena receta", 0));
    }

    @Test
    void agregarComentarioRecetaNoEncontradaLanzaExcepcion() {
        when(recetaRepository.findById(999L)).thenReturn(Optional.empty());

        assertThrows(IllegalArgumentException.class, () ->
                interaccionService.agregarComentario(999L, "Ana", "Buena receta", 4));
    }

    @Test
    void getComentariosPorRecetaConIdNullLanzaExcepcion() {
        assertThrows(NullPointerException.class, () ->
                interaccionService.getComentariosPorReceta(null));
    }

    @Test
    void getMediosPorRecetaRetornaLista() {
        when(medioRepository.findByRecetaIdOrderByFechaSubidaDesc(1L))
                .thenReturn(List.of());

        List<?> result = interaccionService.getMediosPorReceta(1L);

        assertNotNull(result);
        verify(medioRepository).findByRecetaIdOrderByFechaSubidaDesc(1L);
    }
}
