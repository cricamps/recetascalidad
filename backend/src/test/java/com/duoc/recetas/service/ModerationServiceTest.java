package com.duoc.recetas.service;

import com.duoc.recetas.entity.ComentarioEntity;
import com.duoc.recetas.entity.RecetaEntity;
import com.duoc.recetas.repository.ComentarioRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ModerationServiceTest {

    @Mock ComentarioRepository comentarioRepository;

    @InjectMocks ModerationService moderationService;

    private ComentarioEntity c1;
    private ComentarioEntity c2;

    @BeforeEach
    void setUp() {
        RecetaEntity receta = new RecetaEntity();
        receta.setId(1L);
        receta.setNombre("Cazuela");

        c1 = new ComentarioEntity();
        c1.setId(1L);
        c1.setAutor("user1");
        c1.setContenido("Muy buena");
        c1.setValoracion(5);
        c1.setReceta(receta);
        c1.setFechaCreacion(LocalDateTime.now().minusDays(1));

        c2 = new ComentarioEntity();
        c2.setId(2L);
        c2.setAutor("user2");
        c2.setContenido("Regular");
        c2.setValoracion(3);
        c2.setReceta(receta);
        c2.setFechaCreacion(LocalDateTime.now());
    }

    @Test
    void listarTodos_returnsSortedByFechaDesc() {
        when(comentarioRepository.findAll()).thenReturn(List.of(c1, c2));

        List<ComentarioEntity> result = moderationService.listarTodos();

        assertThat(result).hasSize(2);
        assertThat(result.get(0).getId()).isEqualTo(2L);
    }

    @Test
    void eliminar_callsDeleteById() {
        moderationService.eliminar(1L);
        verify(comentarioRepository).deleteById(1L);
    }

    @Test
    void buscarPorId_returnsComentario() {
        when(comentarioRepository.findById(1L)).thenReturn(Optional.of(c1));

        Optional<ComentarioEntity> result = moderationService.buscarPorId(1L);

        assertThat(result).isPresent();
        assertThat(result.get().getAutor()).isEqualTo("user1");
    }

    @Test
    void buscarPorId_returnsEmptyWhenNotFound() {
        when(comentarioRepository.findById(99L)).thenReturn(Optional.empty());

        Optional<ComentarioEntity> result = moderationService.buscarPorId(99L);

        assertThat(result).isEmpty();
    }
}
