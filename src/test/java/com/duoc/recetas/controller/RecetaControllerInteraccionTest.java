package com.duoc.recetas.controller;

import com.duoc.recetas.entity.RecetaEntity;
import com.duoc.recetas.service.InteraccionService;
import com.duoc.recetas.service.RecetaService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.security.core.Authentication;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

/**
 * Tests unitarios para los endpoints de interacción de RecetaController:
 * comentar, subirMedio y compartir.
 */
@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
@DisplayName("Tests unitarios - RecetaController (comentar, subirMedio, compartir)")
@SuppressWarnings("null")
class RecetaControllerInteraccionTest {

    @Mock
    private RecetaService recetaService;

    @Mock
    private InteraccionService interaccionService;

    @Mock
    private Authentication auth;

    @Mock
    private RedirectAttributes ra;

    @InjectMocks
    private RecetaController recetaController;

    // ══════════════════════════════════════════════════════════════════════════
    // COMENTAR
    // ══════════════════════════════════════════════════════════════════════════

    @Test
    @DisplayName("comentar exitoso agrega flash de éxito y redirige a receta")
    void comentar_exitoso_flashExitoYRedirige() {
        when(auth.getName()).thenReturn("usuario_test");
        doNothing().when(interaccionService)
                .agregarComentario(eq(1L), anyString(), anyString(), anyInt());

        String vista = recetaController.comentar(1L, "¡Excelente receta!", 5, auth, ra);

        assertThat(vista).isEqualTo("redirect:/receta/1");
        verify(interaccionService).agregarComentario(1L, "usuario_test", "¡Excelente receta!", 5);
        verify(ra).addFlashAttribute(eq("exito"), anyString());
    }

    @Test
    @DisplayName("comentar con datos inválidos captura excepción y agrega flash de error")
    void comentar_datosInvalidos_flashError() {
        when(auth.getName()).thenReturn("usuario_test");
        doThrow(new IllegalArgumentException("El comentario no puede estar vacío"))
                .when(interaccionService)
                .agregarComentario(eq(1L), anyString(), anyString(), anyInt());

        String vista = recetaController.comentar(1L, "", 3, auth, ra);

        assertThat(vista).isEqualTo("redirect:/receta/1");
        verify(ra).addFlashAttribute(eq("error"), anyString());
    }

    @Test
    @DisplayName("comentar con valoración fuera de rango agrega flash de error")
    void comentar_valoracionFueraDeRango_flashError() {
        when(auth.getName()).thenReturn("usuario_test");
        doThrow(new IllegalArgumentException("La valoración debe estar entre 1 y 5"))
                .when(interaccionService)
                .agregarComentario(eq(2L), anyString(), anyString(), anyInt());

        String vista = recetaController.comentar(2L, "Buena", 10, auth, ra);

        assertThat(vista).isEqualTo("redirect:/receta/2");
        verify(ra).addFlashAttribute(eq("error"), anyString());
    }

    // ══════════════════════════════════════════════════════════════════════════
    // SUBIR MEDIO
    // ══════════════════════════════════════════════════════════════════════════

    @Test
    @DisplayName("subirMedio exitoso agrega flash de éxito y redirige a receta")
    void subirMedio_exitoso_flashExitoYRedirige() throws Exception {
        when(auth.getName()).thenReturn("usuario_test");
        MockMultipartFile archivo = new MockMultipartFile(
                "archivo", "foto.jpg", "image/jpeg", "contenido".getBytes());
        doNothing().when(interaccionService).subirMedio(eq(1L), any(), anyString());

        String vista = recetaController.subirMedio(1L, archivo, auth, ra);

        assertThat(vista).isEqualTo("redirect:/receta/1");
        verify(interaccionService).subirMedio(eq(1L), eq(archivo), eq("usuario_test"));
        verify(ra).addFlashAttribute(eq("exito"), anyString());
    }

    @Test
    @DisplayName("subirMedio con extensión no permitida agrega flash de error")
    void subirMedio_extensionNoPermitida_flashError() throws Exception {
        when(auth.getName()).thenReturn("usuario_test");
        MockMultipartFile archivo = new MockMultipartFile(
                "archivo", "virus.exe", "application/octet-stream", "datos".getBytes());
        doThrow(new IllegalArgumentException("Tipo de archivo no permitido: .exe"))
                .when(interaccionService).subirMedio(anyLong(), any(), anyString());

        String vista = recetaController.subirMedio(1L, archivo, auth, ra);

        assertThat(vista).isEqualTo("redirect:/receta/1");
        verify(ra).addFlashAttribute(eq("error"), anyString());
    }

    @Test
    @DisplayName("subirMedio con error de runtime agrega flash de error genérico")
    void subirMedio_errorRuntime_flashError() throws Exception {
        when(auth.getName()).thenReturn("usuario_test");
        MockMultipartFile archivo = new MockMultipartFile(
                "archivo", "foto.jpg", "image/jpeg", "contenido".getBytes());
        doThrow(new RuntimeException("Error de disco"))
                .when(interaccionService).subirMedio(anyLong(), any(), anyString());

        String vista = recetaController.subirMedio(3L, archivo, auth, ra);

        assertThat(vista).isEqualTo("redirect:/receta/3");
        verify(ra).addFlashAttribute(eq("error"), anyString());
    }

    // ══════════════════════════════════════════════════════════════════════════
    // COMPARTIR
    // ══════════════════════════════════════════════════════════════════════════

    @Test
    @DisplayName("compartir con destino 'web' redirige con query compartido=true")
    void compartir_destinoWeb_redirigePaginaReceta() {
        RecetaEntity receta = crearReceta(1L, "Cazuela Chilena");
        when(recetaService.getPorId(1L)).thenReturn(Optional.of(receta));

        String vista = recetaController.compartir(1L, "web", ra);

        assertThat(vista).startsWith("redirect:/receta/1");
    }

    @Test
    @DisplayName("compartir con destino 'twitter' genera URL de Twitter")
    void compartir_destinoTwitter_generaUrlTwitter() {
        RecetaEntity receta = crearReceta(1L, "Empanadas");
        when(recetaService.getPorId(1L)).thenReturn(Optional.of(receta));

        String vista = recetaController.compartir(1L, "twitter", ra);

        assertThat(vista).startsWith("redirect:https://twitter.com");
    }

    @Test
    @DisplayName("compartir con destino 'facebook' genera URL de Facebook")
    void compartir_destinoFacebook_generaUrlFacebook() {
        RecetaEntity receta = crearReceta(1L, "Sopaipillas");
        when(recetaService.getPorId(1L)).thenReturn(Optional.of(receta));

        String vista = recetaController.compartir(1L, "facebook", ra);

        assertThat(vista).startsWith("redirect:https://www.facebook.com");
    }

    @Test
    @DisplayName("compartir con destino 'whatsapp' genera URL de WhatsApp")
    void compartir_destinoWhatsapp_generaUrlWhatsapp() {
        RecetaEntity receta = crearReceta(1L, "Pastel de Choclo");
        when(recetaService.getPorId(1L)).thenReturn(Optional.of(receta));

        String vista = recetaController.compartir(1L, "whatsapp", ra);

        assertThat(vista).startsWith("redirect:https://wa.me");
    }

    @Test
    @DisplayName("compartir con receta inexistente redirige a buscar")
    void compartir_recetaNoExiste_redirigeBuscar() {
        when(recetaService.getPorId(999L)).thenReturn(Optional.empty());

        String vista = recetaController.compartir(999L, "web", ra);

        assertThat(vista).isEqualTo("redirect:/buscar");
    }

    // ── Helper ────────────────────────────────────────────────────────────────
    private RecetaEntity crearReceta(Long id, String nombre) {
        RecetaEntity r = new RecetaEntity();
        r.setId(id);
        r.setNombre(nombre);
        r.setIngredientes("Ingrediente1|Ingrediente2");
        r.setInstrucciones("Paso 1|Paso 2");
        r.setTipoCocina("Tradicional");
        r.setPaisOrigen("Chile");
        r.setDificultad("Media");
        r.setTiempoCoccion(30);
        return r;
    }
}
