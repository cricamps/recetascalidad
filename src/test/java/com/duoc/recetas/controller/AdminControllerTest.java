package com.duoc.recetas.controller;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
class AdminControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    void listarUsuariosSinAuthRedirige() throws Exception {
        mockMvc.perform(get("/admin/usuarios"))
                .andExpect(status().is3xxRedirection());
    }

    @Test
    @WithMockUser(username = "usuario", roles = {"USER"})
    void listarUsuariosSinRolAdminRetorna403() throws Exception {
        mockMvc.perform(get("/admin/usuarios"))
                .andExpect(status().isForbidden());
    }

    @Test
    @WithMockUser(username = "admin", roles = {"USER", "ADMIN"})
    void listarUsuariosConRolAdminRetornaVista() throws Exception {
        mockMvc.perform(get("/admin/usuarios"))
                .andExpect(status().isOk())
                .andExpect(view().name("admin/usuarios"));
    }
}
