package com.duoc.recetas.controller;

import com.duoc.recetas.service.AdminService;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/admin")
@PreAuthorize("hasRole('ADMIN')")
public class AdminController {

    private final AdminService adminService;

    public AdminController(AdminService adminService) {
        this.adminService = adminService;
    }

    // ── LISTAR USUARIOS ───────────────────────────────────────────────

    @GetMapping("/usuarios")
    public String listarUsuarios(Model model) {
        var usuarios = adminService.listarUsuarios();
        model.addAttribute("usuarios", usuarios);
        model.addAttribute("totalAdmins",   usuarios.stream().filter(u -> u.getRoles().contains("ROLE_ADMIN")).count());
        model.addAttribute("totalChefs",    usuarios.stream().filter(u -> u.getRoles().contains("ROLE_CHEF") && !u.getRoles().contains("ROLE_ADMIN")).count());
        model.addAttribute("totalUsuarios", usuarios.stream().filter(u -> !u.getRoles().contains("ROLE_ADMIN") && !u.getRoles().contains("ROLE_CHEF")).count());
        return "admin/usuarios";
    }

    // ── CAMBIAR ROL ───────────────────────────────────────────────────

    @PostMapping("/usuarios/{id}/rol")
    public String cambiarRol(@PathVariable Long id,
                              @RequestParam String rol,
                              RedirectAttributes ra) {
        adminService.cambiarRol(id, rol);
        ra.addFlashAttribute("exito", "Rol actualizado correctamente.");
        return "redirect:/admin/usuarios";
    }

    // ── RESETEAR CONTRASEÑA ───────────────────────────────────────────

    @PostMapping("/usuarios/{id}/reset-password")
    public String resetearPassword(@PathVariable Long id,
                                    @RequestParam String nuevaPassword,
                                    RedirectAttributes ra) {
        if (nuevaPassword == null || nuevaPassword.isBlank()) {
            ra.addFlashAttribute("error", "La nueva contraseña no puede estar vacía.");
            return "redirect:/admin/usuarios";
        }
        adminService.resetearPassword(id, nuevaPassword);
        ra.addFlashAttribute("exito", "Contraseña reseteada correctamente.");
        return "redirect:/admin/usuarios";
    }

    // ── ELIMINAR USUARIO ──────────────────────────────────────────────

    @PostMapping("/usuarios/{id}/eliminar")
    public String eliminarUsuario(@PathVariable Long id,
                                   RedirectAttributes ra) {
        adminService.eliminarUsuario(id);
        ra.addFlashAttribute("exito", "Usuario eliminado correctamente.");
        return "redirect:/admin/usuarios";
    }

    // ── CREAR USUARIO ─────────────────────────────────────────────────

    @PostMapping("/usuarios/crear")
    public String crearUsuario(@RequestParam String username,
                                @RequestParam String password,
                                @RequestParam String roles,
                                @RequestParam(required = false) String nombreCompleto,
                                @RequestParam(required = false) String correo,
                                RedirectAttributes ra) {
        try {
            adminService.crearUsuario(username, password, roles,
                                      nombreCompleto, correo);
            ra.addFlashAttribute("exito", "Usuario '" + username + "' creado correctamente.");
        } catch (IllegalArgumentException e) {
            ra.addFlashAttribute("error", e.getMessage());
        }
        return "redirect:/admin/usuarios";
    }
}
