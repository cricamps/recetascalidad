package com.duoc.recetas.service;

import com.duoc.recetas.entity.FavoritoEntity;
import com.duoc.recetas.entity.RecetaEntity;
import com.duoc.recetas.repository.FavoritoRepository;
import com.duoc.recetas.repository.RecetaRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class FavoritoServiceTest {

    @Mock FavoritoRepository favoritoRepository;
    @Mock RecetaRepository   recetaRepository;
    @InjectMocks FavoritoService favoritoService;

    private RecetaEntity receta;

    @BeforeEach
    void setUp() {
        receta = new RecetaEntity();
        receta.setId(1L);
        receta.setNombre("Cazuela");
    }

    @Test
    void getFavoritos_returnsListFromRepository() {
        FavoritoEntity fav = new FavoritoEntity("user1", receta);
        when(favoritoRepository.findByUsernameOrderByFechaAgregadoDesc("user1")).thenReturn(List.of(fav));
        assertThat(favoritoService.getFavoritos("user1")).hasSize(1);
    }

    @Test
    void esFavorito_true_whenExists() {
        when(favoritoRepository.existsByUsernameAndRecetaId("user1", 1L)).thenReturn(true);
        assertThat(favoritoService.esFavorito("user1", 1L)).isTrue();
    }

    @Test
    void esFavorito_false_whenNotExists() {
        when(favoritoRepository.existsByUsernameAndRecetaId("user1", 99L)).thenReturn(false);
        assertThat(favoritoService.esFavorito("user1", 99L)).isFalse();
    }

    @Test
    void agregar_savesWhenNotExists() {
        when(favoritoRepository.existsByUsernameAndRecetaId("user1", 1L)).thenReturn(false);
        when(recetaRepository.findById(1L)).thenReturn(Optional.of(receta));
        favoritoService.agregar("user1", 1L);
        verify(favoritoRepository).save(any(FavoritoEntity.class));
    }

    @Test
    void agregar_idempotente_noSaveIfAlreadyExists() {
        when(favoritoRepository.existsByUsernameAndRecetaId("user1", 1L)).thenReturn(true);
        favoritoService.agregar("user1", 1L);
        verify(favoritoRepository, never()).save(any());
    }

    @Test
    void agregar_throwsWhenRecetaNotFound() {
        when(favoritoRepository.existsByUsernameAndRecetaId("user1", 99L)).thenReturn(false);
        when(recetaRepository.findById(99L)).thenReturn(Optional.empty());
        assertThatThrownBy(() -> favoritoService.agregar("user1", 99L))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void eliminar_callsDelete() {
        favoritoService.eliminar("user1", 1L);
        verify(favoritoRepository).deleteByUsernameAndRecetaId("user1", 1L);
    }

    @Test
    void toggle_addsWhenNotFavorite_returnsTrue() {
        when(favoritoRepository.existsByUsernameAndRecetaId("user1", 1L)).thenReturn(false);
        when(recetaRepository.findById(1L)).thenReturn(Optional.of(receta));
        assertThat(favoritoService.toggle("user1", 1L)).isTrue();
        verify(favoritoRepository).save(any(FavoritoEntity.class));
    }

    @Test
    void toggle_removesWhenFavorite_returnsFalse() {
        when(favoritoRepository.existsByUsernameAndRecetaId("user1", 1L)).thenReturn(true);
        assertThat(favoritoService.toggle("user1", 1L)).isFalse();
        verify(favoritoRepository).deleteByUsernameAndRecetaId("user1", 1L);
    }
}
