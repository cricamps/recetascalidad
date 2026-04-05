package com.duoc.recetas.controller;

import com.duoc.recetas.dto.RegistroRequest;
import com.duoc.recetas.entity.RecetaEntity;
import com.duoc.recetas.service.RecetaService;
import com.duoc.recetas.service.UsuarioService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.ui.Model;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("Tests unitarios - UsuarioController")
class UsuarioControllerTest {

    @Mock
    private UsuarioService usuarioService;

    @Mock
    private RecetaService recetaService;

    @Mock
    private Model model;

    @InjectMocks
    private UsuarioController usuarioController;

    // ── REGISTRO ─────────────────────────────────────────────────────────────

    @Test
    @DisplayName("mostrarRegistro retorna vista 'registro' con DTO vacío")
    void mostrarRegistro_retornaVistaRegistro() {
        String vista = usuarioController.mostrarRegistro(model);
        assertThat(vista).isEqualTo("registro");
        verify(model).addAttribute(eq("registroRequest"), any(RegistroRequest.class));
    }

    @Test
    @DisplayName("procesarRegistro con datos válidos redirige a login con ?registrado")
    void procesarRegistro_datosValidos_redirigeLLogin() {
        RegistroRequest req = buildRequest("juan", "pass123", "Juan Test", "juan@test.cl");
        when(usuarioService.registrar(req)).thenReturn(true);

        String vista = usuarioController.procesarRegistro(req, model);

        assertThat(vista).isEqualTo("redirect:/login?registrado=true");
    }

    @Test
    @DisplayName("procesarRegistro con username duplicado vuelve a registro con error")
    void procesarRegistro_usernameDuplicado_retornaRegistroConError() {
        RegistroRequest req = buildRequest("juan", "pass123", "Juan Test", "juan@test.cl");
        when(usuarioService.registrar(req)).thenReturn(false);

        String vista = usuarioController.procesarRegistro(req, model);

        assertThat(vista).isEqualTo("registro");
        verify(model).addAttribute(eq("error"), anyString());
    }

    @Test
    @DisplayName("procesarRegistro con campos vacíos vuelve a registro con error")
    void procesarRegistro_camposVacios_retornaRegistroConError() {
        RegistroRequest req = buildRequest("", "", "", "");

        String vista = usuarioController.procesarRegistro(req, model);

        assertThat(vista).isEqualTo("registro");
        verify(model).addAttribute(eq("error"), anyString());
        verify(usuarioService, never()).registrar(any());
    }

    // ── PUBLICAR RECETA ───────────────────────────────────────────────────────

    @Test
    @DisplayName("mostrarFormularioReceta retorna vista 'nueva-receta'")
    void mostrarFormularioReceta_retornaVistaFormulario() {
        String vista = usuarioController.mostrarFormularioReceta(model);
        assertThat(vista).isEqualTo("nueva-receta");
    }

    @Test
    @DisplayName("publicarReceta con datos válidos guarda y redirige a buscar")
    void publicarReceta_datosValidos_guardaYRedirige() {
        when(recetaService.guardar(any(RecetaEntity.class))).thenReturn(new RecetaEntity());

        String vista = usuarioController.publicarReceta(
            "Sopa de Tomate", "Tomates|Cebolla", "Sofreír.|Licuar.",
            "Tradicional", "Chile", 30, "Fácil", model);

        assertThat(vista).isEqualTo("redirect:/buscar?publicada=true");
        verify(recetaService).guardar(any(RecetaEntity.class));
    }

    @Test
    @DisplayName("publicarReceta con nombre vacío vuelve al formulario con error")
    void publicarReceta_nombreVacio_retornaFormularioConError() {
        String vista = usuarioController.publicarReceta(
            "", "Tomates", "Sofreír.", "Tradicional", "Chile", 30, "Fácil", model);

        assertThat(vista).isEqualTo("nueva-receta");
        verify(model).addAttribute(eq("error"), anyString());
        verify(recetaService, never()).guardar(any());
    }

    @Test
    @DisplayName("publicarReceta con ingredientes vacíos vuelve al formulario con error")
    void publicarReceta_ingredientesVacios_retornaFormularioConError() {
        String vista = usuarioController.publicarReceta(
            "Sopa", "", "Sofreír.", "Tradicional", "Chile", 30, "Fácil", model);

        assertThat(vista).isEqualTo("nueva-receta");
        verify(model).addAttribute(eq("error"), anyString());
    }

    // ── Helper ────────────────────────────────────────────────────────────────
    private RegistroRequest buildRequest(String username, String password,
                                          String nombre, String correo) {
        RegistroRequest req = new RegistroRequest();
        req.setUsername(username);
        req.setPassword(password);
        req.setNombreCompleto(nombre);
        req.setCorreo(correo);
        return req;
    }
}
