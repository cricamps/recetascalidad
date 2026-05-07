package com.duoc.recetas.controller;

import com.duoc.recetas.service.AdminService;
import com.duoc.recetas.service.ModerationService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.duoc.recetas.entity.ComentarioEntity;
import com.duoc.recetas.entity.RecetaEntity;
import com.duoc.recetas.entity.Usuario;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("Tests unitarios - AdminController (lógica)")
class AdminControllerUnitTest {

    @Mock private AdminService      adminService;
    @Mock private ModerationService moderationService;
    @Mock private RedirectAttributes ra;
    @Mock private org.springframework.ui.Model model;

    @InjectMocks private AdminController adminController;

    @Test
    @DisplayName("listarUsuarios agrega lista al modelo y retorna vista")
    void listarUsuarios_retornaVista() {
        Usuario admin = new Usuario("admin", "pass", "ROLE_USER,ROLE_ADMIN");
        Usuario chef  = new Usuario("chef",  "pass", "ROLE_USER,ROLE_CHEF");
        Usuario user  = new Usuario("juan",  "pass", "ROLE_USER");
        when(adminService.listarUsuarios()).thenReturn(List.of(admin, chef, user));
        String vista = adminController.listarUsuarios(model);
        assertThat(vista).isEqualTo("admin/usuarios");
        verify(model).addAttribute(eq("totalAdmins"), eq(1L));
        verify(model).addAttribute(eq("totalChefs"),  eq(1L));
        verify(model).addAttribute(eq("totalUsuarios"), eq(1L));
    }

    @Test
    @DisplayName("listarUsuarios con lista vacía retorna ceros")
    void listarUsuarios_listaVacia_ceroContadores() {
        when(adminService.listarUsuarios()).thenReturn(List.of());
        adminController.listarUsuarios(model);
        verify(model).addAttribute(eq("totalAdmins"),   eq(0L));
        verify(model).addAttribute(eq("totalChefs"),    eq(0L));
        verify(model).addAttribute(eq("totalUsuarios"), eq(0L));
    }

    @Test
    void cambiarRol_llamaServicioYRedirige() {
        assertThat(adminController.cambiarRol(1L, "ROLE_USER,ROLE_ADMIN", ra))
                .isEqualTo("redirect:/admin/usuarios");
        verify(adminService).cambiarRol(1L, "ROLE_USER,ROLE_ADMIN");
    }

    @Test
    void resetearPassword_contrasennaValida_redirige() {
        assertThat(adminController.resetearPassword(1L, "nuevaPass123", ra))
                .isEqualTo("redirect:/admin/usuarios");
        verify(adminService).resetearPassword(1L, "nuevaPass123");
    }

    @Test
    void resetearPassword_contrasennaVacia_noLlamaServicio() {
        adminController.resetearPassword(1L, "   ", ra);
        verify(adminService, never()).resetearPassword(anyLong(), anyString());
        verify(ra).addFlashAttribute(eq("error"), anyString());
    }

    @Test
    void resetearPassword_contrasennaNull_noLlamaServicio() {
        adminController.resetearPassword(1L, null, ra);
        verify(adminService, never()).resetearPassword(anyLong(), anyString());
    }

    @Test
    void eliminarUsuario_llamaServicioYRedirige() {
        assertThat(adminController.eliminarUsuario(1L, ra))
                .isEqualTo("redirect:/admin/usuarios");
        verify(adminService).eliminarUsuario(1L);
    }

    @Test
    void crearUsuario_exitoso_flashExito() {
        adminController.crearUsuario("nuevo", "pass123", "ROLE_USER", "Nuevo", "n@t.cl", ra);
        verify(adminService).crearUsuario("nuevo", "pass123", "ROLE_USER", "Nuevo", "n@t.cl");
        verify(ra).addFlashAttribute(eq("exito"), anyString());
    }

    @Test
    void crearUsuario_duplicado_flashError() {
        doThrow(new IllegalArgumentException("El usuario 'admin' ya existe."))
                .when(adminService).crearUsuario(eq("admin"), any(), any(), any(), any());
        adminController.crearUsuario("admin", "pass", "ROLE_USER", null, null, ra);
        verify(ra).addFlashAttribute(eq("error"), anyString());
    }

    @Test
    void listarComentarios_retornaVista() {
        ComentarioEntity c = crearComentario(1L, "user1", "Muy buena");
        when(moderationService.listarTodos()).thenReturn(List.of(c));
        assertThat(adminController.listarComentarios(model)).isEqualTo("admin/comentarios");
        verify(model).addAttribute(eq("totalComentarios"), eq(1));
    }

    @Test
    void eliminarComentario_existente_flashExito() {
        ComentarioEntity c = crearComentario(1L, "user1", "Contenido");
        when(moderationService.buscarPorId(1L)).thenReturn(Optional.of(c));
        assertThat(adminController.eliminarComentario(1L, ra))
                .isEqualTo("redirect:/admin/comentarios");
        verify(moderationService).eliminar(1L);
    }

    @Test
    void eliminarComentario_inexistente_flashError() {
        when(moderationService.buscarPorId(99L)).thenReturn(Optional.empty());
        adminController.eliminarComentario(99L, ra);
        verify(moderationService, never()).eliminar(anyLong());
        verify(ra).addFlashAttribute(eq("error"), anyString());
    }

    private ComentarioEntity crearComentario(Long id, String autor, String contenido) {
        RecetaEntity receta = new RecetaEntity();
        receta.setId(1L);
        receta.setNombre("Cazuela");
        ComentarioEntity c = new ComentarioEntity();
        c.setId(id);
        c.setAutor(autor);
        c.setContenido(contenido);
        c.setValoracion(5);
        c.setReceta(receta);
        c.setFechaCreacion(LocalDateTime.now());
        return c;
    }
}
