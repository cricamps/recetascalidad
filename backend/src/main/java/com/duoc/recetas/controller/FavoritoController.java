package com.duoc.recetas.controller;

import com.duoc.recetas.service.FavoritoService;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/favoritos")
public class FavoritoController {

    private final FavoritoService favoritoService;

    public FavoritoController(FavoritoService favoritoService) {
        this.favoritoService = favoritoService;
    }

    /** Lista de favoritos del usuario autenticado. */
    @GetMapping
    public String listar(Authentication auth, Model model) {
        model.addAttribute("favoritos",
                favoritoService.getFavoritos(auth.getName()));
        return "favoritos";
    }

    /** Agrega una receta a favoritos. */
    @PostMapping("/agregar/{recetaId}")
    public String agregar(@PathVariable Long recetaId,
                          Authentication auth,
                          RedirectAttributes ra) {
        try {
            favoritoService.agregar(auth.getName(), recetaId);
            ra.addFlashAttribute("exito", "Receta añadida a tus favoritos.");
        } catch (IllegalArgumentException e) {
            ra.addFlashAttribute("error", e.getMessage());
        }
        return "redirect:/receta/" + recetaId;
    }

    /** Elimina una receta de favoritos. */
    @PostMapping("/eliminar/{recetaId}")
    public String eliminar(@PathVariable Long recetaId,
                           Authentication auth,
                           RedirectAttributes ra) {
        favoritoService.eliminar(auth.getName(), recetaId);
        ra.addFlashAttribute("exito", "Receta eliminada de tus favoritos.");
        return "redirect:/favoritos";
    }

    /** Toggle (agrega o quita) desde la página de detalle. */
    @PostMapping("/toggle/{recetaId}")
    public String toggle(@PathVariable Long recetaId,
                         Authentication auth,
                         RedirectAttributes ra) {
        boolean ahora = favoritoService.toggle(auth.getName(), recetaId);
        ra.addFlashAttribute("exito",
                ahora ? "Receta añadida a tus favoritos." : "Receta eliminada de tus favoritos.");
        return "redirect:/receta/" + recetaId;
    }
}
