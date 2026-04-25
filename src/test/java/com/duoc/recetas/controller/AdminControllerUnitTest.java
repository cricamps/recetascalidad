package com.duoc.recetas.controller;

import com.duoc.recetas.service.AdminService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.duoc.recetas.entity.Usuario;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("Tests unitarios - AdminController (lógica)")
@SuppressWarnings("null")
class AdminControllerUnitTest {

    @Mock
    private AdminService adminService;

    @Mock
    private RedirectAttributes ra;

    @Mock
    private org.springframework.ui.Model model;

    @InjectMocks
    private AdminController adminController;

    // ── LISTAR USUARIOS ───────────────────────────────────────────────────────

    @Test
    @DisplayName("listarUsuarios agrega lista al modelo y retorna vista")
    void listarUsuarios_retornaVista() {
        Usuario admin = new Usuario("admin", "pass", "ROLE_USER,ROLE_ADMIN");
        Usuario chef  = new Usuario("chef",  "pass", "ROLE_USER,ROLE_CHEF");
        Usuario user  = new Usuario("juan",  "pass", "ROLE_USER");
        when(adminService.listarUsuarios()).thenReturn(List.of(admin, chef, user));

        String vista = adminController.listarUsuarios(model);

        assertThat(vista).isEqualTo("admin/usuarios");
        verify(model).addAttribute(eq("usuarios"), anyList());
        verify(model).addAttribute(eq("totalAdmins"),   eq(1L));
        verify(model).addAttribute(eq("totalChefs"),    eq(1L));
        verify(model).addAttribute(eq("totalUsuarios"), eq(1L));
    }

    @Test
    @DisplayName("listarUsuarios con lista vacía retorna ceros en contadores")
    void listarUsuarios_listaVacia_ceroContadores() {
        when(adminService.listarUsuarios()).thenReturn(List.of());

        String vista = adminController.listarUsuarios(model);

        assertThat(vista).isEqualTo("admin/usuarios");
        verify(model).addAttribute(eq("totalAdmins"),   eq(0L));
        verify(model).addAttribute(eq("totalChefs"),    eq(0L));
        verify(model).addAttribute(eq("totalUsuarios"), eq(0L));
    }

    // ── CAMBIAR ROL ───────────────────────────────────────────────────────────

    @Test
    @DisplayName("cambiarRol llama al servicio y redirige a /admin/usuarios")
    void cambiarRol_llamaServicioYRedirige() {
        String vista = adminController.cambiarRol(1L, "ROLE_USER,ROLE_ADMIN", ra);

        assertThat(vista).isEqualTo("redirect:/admin/usuarios");
        verify(adminService).cambiarRol(1L, "ROLE_USER,ROLE_ADMIN");
        verify(ra).addFlashAttribute(eq("exito"), anyString());
    }

    // ── RESETEAR CONTRASEÑA ───────────────────────────────────────────────────

    @Test
    @DisplayName("resetearPassword con contraseña válida llama al servicio y redirige")
    void resetearPassword_contrasennaValida_redirige() {
        String vista = adminController.resetearPassword(1L, "nuevaPass123", ra);

        assertThat(vista).isEqualTo("redirect:/admin/usuarios");
        verify(adminService).resetearPassword(1L, "nuevaPass123");
        verify(ra).addFlashAttribute(eq("exito"), anyString());
    }

    @Test
    @DisplayName("resetearPassword con contraseña vacía no llama al servicio y retorna error")
    void resetearPassword_contrasennaVacia_noLlamaServicio() {
        String vista = adminController.resetearPassword(1L, "   ", ra);

        assertThat(vista).isEqualTo("redirect:/admin/usuarios");
        verify(adminService, never()).resetearPassword(anyLong(), anyString());
        verify(ra).addFlashAttribute(eq("error"), anyString());
    }

    @Test
    @DisplayName("resetearPassword con contraseña null no llama al servicio")
    void resetearPassword_contrasennaNull_noLlamaServicio() {
        String vista = adminController.resetearPassword(1L, null, ra);

        assertThat(vista).isEqualTo("redirect:/admin/usuarios");
        verify(adminService, never()).resetearPassword(anyLong(), anyString());
    }

    // ── ELIMINAR USUARIO ──────────────────────────────────────────────────────

    @Test
    @DisplayName("eliminarUsuario llama al servicio y redirige")
    void eliminarUsuario_llamaServicioYRedirige() {
        String vista = adminController.eliminarUsuario(1L, ra);

        assertThat(vista).isEqualTo("redirect:/admin/usuarios");
        verify(adminService).eliminarUsuario(1L);
        verify(ra).addFlashAttribute(eq("exito"), anyString());
    }

    // ── CREAR USUARIO ─────────────────────────────────────────────────────────

    @Test
    @DisplayName("crearUsuario exitoso llama al servicio y agrega flash de exito")
    void crearUsuario_exitoso_flashExito() {
        String vista = adminController.crearUsuario(
                "nuevo", "pass123", "ROLE_USER", "Nuevo User", "nuevo@test.cl", ra);

        assertThat(vista).isEqualTo("redirect:/admin/usuarios");
        verify(adminService).crearUsuario("nuevo", "pass123", "ROLE_USER", "Nuevo User", "nuevo@test.cl");
        verify(ra).addFlashAttribute(eq("exito"), anyString());
    }

    @Test
    @DisplayName("crearUsuario duplicado captura excepcion y agrega flash de error")
    void crearUsuario_duplicado_flashError() {
        doThrow(new IllegalArgumentException("El usuario 'admin' ya existe."))
                .when(adminService).crearUsuario(eq("admin"), any(), any(), any(), any());

        String vista = adminController.crearUsuario(
                "admin", "pass", "ROLE_USER", null, null, ra);

        assertThat(vista).isEqualTo("redirect:/admin/usuarios");
        verify(ra).addFlashAttribute(eq("error"), anyString());
    }
}
