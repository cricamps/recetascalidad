package com.duoc.recetas.service;

import com.duoc.recetas.entity.MedioRecetaEntity;
import com.duoc.recetas.entity.RecetaEntity;
import com.duoc.recetas.repository.ComentarioRepository;
import com.duoc.recetas.repository.MedioRecetaRepository;
import com.duoc.recetas.repository.RecetaRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.api.io.TempDir;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.util.ReflectionTestUtils;

import java.io.IOException;
import java.nio.file.Path;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("Tests unitarios - InteraccionService.subirMedio")
@SuppressWarnings("null")
class InteraccionServiceSubirMedioTest {

    @Mock
    private ComentarioRepository comentarioRepository;

    @Mock
    private MedioRecetaRepository medioRepository;

    @Mock
    private RecetaRepository recetaRepository;

    @InjectMocks
    private InteraccionService interaccionService;

    @TempDir
    Path tempDir;

    private RecetaEntity recetaMock;

    @BeforeEach
    void setUp() {
        recetaMock = new RecetaEntity();
        recetaMock.setId(1L);
        recetaMock.setNombre("Cazuela");
        // Apuntar el uploadDir al directorio temporal para no escribir en disco real
        ReflectionTestUtils.setField(interaccionService, "uploadDir", tempDir);
    }

    // ── CASOS FELICES ──────────────────────────────────────────────────────────

    @Test
    @DisplayName("subirMedio con imagen JPG guarda medio de tipo FOTO")
    void subirMedio_conImagenJpg_guardaFoto() throws IOException {
        MockMultipartFile archivo = new MockMultipartFile(
                "file", "foto.jpg", "image/jpeg", "contenido".getBytes());
        when(recetaRepository.findById(1L)).thenReturn(Optional.of(recetaMock));

        interaccionService.subirMedio(1L, archivo, "usuario");

        verify(medioRepository).save(argThat(m ->
                "FOTO".equals(m.getTipo()) && m.getNombreArchivo().endsWith(".jpg")));
    }

    @Test
    @DisplayName("subirMedio con imagen PNG guarda medio de tipo FOTO")
    void subirMedio_conImagenPng_guardaFoto() throws IOException {
        MockMultipartFile archivo = new MockMultipartFile(
                "file", "imagen.png", "image/png", "datos".getBytes());
        when(recetaRepository.findById(1L)).thenReturn(Optional.of(recetaMock));

        interaccionService.subirMedio(1L, archivo, "chef");

        verify(medioRepository).save(argThat(m -> "FOTO".equals(m.getTipo())));
    }

    @Test
    @DisplayName("subirMedio con video MP4 guarda medio de tipo VIDEO")
    void subirMedio_conVideoMp4_guardaVideo() throws IOException {
        MockMultipartFile archivo = new MockMultipartFile(
                "file", "video.mp4", "video/mp4", "datos".getBytes());
        when(recetaRepository.findById(1L)).thenReturn(Optional.of(recetaMock));

        interaccionService.subirMedio(1L, archivo, "chef");

        verify(medioRepository).save(argThat(m ->
                "VIDEO".equals(m.getTipo()) && m.getNombreArchivo().endsWith(".mp4")));
    }

    @Test
    @DisplayName("subirMedio con video MOV guarda medio de tipo VIDEO")
    void subirMedio_conVideoMov_guardaVideo() throws IOException {
        MockMultipartFile archivo = new MockMultipartFile(
                "file", "clip.mov", "video/quicktime", "datos".getBytes());
        when(recetaRepository.findById(1L)).thenReturn(Optional.of(recetaMock));

        interaccionService.subirMedio(1L, archivo, "chef");

        verify(medioRepository).save(argThat(m -> "VIDEO".equals(m.getTipo())));
    }

    @Test
    @DisplayName("subirMedio asigna el usuario correcto al medio")
    void subirMedio_asignaUsuarioCorrecto() throws IOException {
        MockMultipartFile archivo = new MockMultipartFile(
                "file", "foto.jpg", "image/jpeg", "datos".getBytes());
        when(recetaRepository.findById(1L)).thenReturn(Optional.of(recetaMock));

        interaccionService.subirMedio(1L, archivo, "mi_usuario");

        verify(medioRepository).save(argThat(m -> "mi_usuario".equals(m.getSubidoPor())));
    }

    @Test
    @DisplayName("subirMedio genera nombre UUID (no usa el nombre original)")
    void subirMedio_generaNombreUUID() throws IOException {
        MockMultipartFile archivo = new MockMultipartFile(
                "file", "foto_original.jpg", "image/jpeg", "datos".getBytes());
        when(recetaRepository.findById(1L)).thenReturn(Optional.of(recetaMock));

        interaccionService.subirMedio(1L, archivo, "usuario");

        verify(medioRepository).save(argThat(m ->
                !m.getNombreArchivo().contains("foto_original")));
    }

    // ── CASOS DE ERROR ─────────────────────────────────────────────────────────

    @Test
    @DisplayName("subirMedio con archivo vacío lanza IllegalArgumentException")
    void subirMedio_archivoVacio_lanzaExcepcion() {
        MockMultipartFile archivo = new MockMultipartFile(
                "file", "foto.jpg", "image/jpeg", new byte[0]);

        assertThrows(IllegalArgumentException.class, () ->
                interaccionService.subirMedio(1L, archivo, "usuario"));
        verify(medioRepository, never()).save(any());
    }

    @Test
    @DisplayName("subirMedio con extensión no permitida lanza IllegalArgumentException")
    void subirMedio_extensionNoPermitida_lanzaExcepcion() {
        MockMultipartFile archivo = new MockMultipartFile(
                "file", "script.exe", "application/octet-stream", "datos".getBytes());

        assertThrows(IllegalArgumentException.class, () ->
                interaccionService.subirMedio(1L, archivo, "usuario"));
        verify(medioRepository, never()).save(any());
    }

    @Test
    @DisplayName("subirMedio con receta inexistente lanza IllegalArgumentException")
    void subirMedio_recetaNoExiste_lanzaExcepcion() {
        MockMultipartFile archivo = new MockMultipartFile(
                "file", "foto.jpg", "image/jpeg", "datos".getBytes());
        when(recetaRepository.findById(999L)).thenReturn(Optional.empty());

        assertThrows(IllegalArgumentException.class, () ->
                interaccionService.subirMedio(999L, archivo, "usuario"));
    }

    @Test
    @DisplayName("subirMedio con recetaId null lanza NullPointerException")
    void subirMedio_recetaIdNull_lanzaExcepcion() {
        MockMultipartFile archivo = new MockMultipartFile(
                "file", "foto.jpg", "image/jpeg", "datos".getBytes());

        assertThrows(NullPointerException.class, () ->
                interaccionService.subirMedio(null, archivo, "usuario"));
    }

    @Test
    @DisplayName("subirMedio con archivo WebP guarda como FOTO")
    void subirMedio_conWebp_guardaFoto() throws IOException {
        MockMultipartFile archivo = new MockMultipartFile(
                "file", "imagen.webp", "image/webp", "datos".getBytes());
        when(recetaRepository.findById(1L)).thenReturn(Optional.of(recetaMock));

        interaccionService.subirMedio(1L, archivo, "usuario");

        verify(medioRepository).save(argThat((MedioRecetaEntity m) -> "FOTO".equals(m.getTipo())));
    }

    @Test
    @DisplayName("subirMedio con video AVI guarda como VIDEO")
    void subirMedio_conAvi_guardaVideo() throws IOException {
        MockMultipartFile archivo = new MockMultipartFile(
                "file", "clip.avi", "video/x-msvideo", "datos".getBytes());
        when(recetaRepository.findById(1L)).thenReturn(Optional.of(recetaMock));

        interaccionService.subirMedio(1L, archivo, "usuario");

        verify(medioRepository).save(argThat((MedioRecetaEntity m) -> "VIDEO".equals(m.getTipo())));
    }
}
