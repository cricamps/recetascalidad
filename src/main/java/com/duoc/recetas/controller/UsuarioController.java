package com.duoc.recetas.controller;

import com.duoc.recetas.dto.RegistroRequest;
import com.duoc.recetas.entity.RecetaEntity;
import com.duoc.recetas.service.RecetaService;
import com.duoc.recetas.service.UsuarioService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class UsuarioController {

    private final UsuarioService usuarioService;
    private final RecetaService recetaService;

    public UsuarioController(UsuarioService usuarioService, RecetaService recetaService) {
        this.usuarioService = usuarioService;
        this.recetaService = recetaService;
    }

    @GetMapping("/registro")
    public String mostrarRegistro(Model model) {
        model.addAttribute("registroRequest", new RegistroRequest());
        return "registro";
    }

    @PostMapping("/registro")
    public String procesarRegistro(
            @ModelAttribute RegistroRequest registroRequest,
            Model model) {

        if (esBlanco(registroRequest.getUsername())
                || esBlanco(registroRequest.getPassword())
                || esBlanco(registroRequest.getCorreo())
                || esBlanco(registroRequest.getNombreCompleto())) {
            model.addAttribute("error", "Todos los campos son obligatorios.");
            return "registro";
        }

        boolean ok = usuarioService.registrar(registroRequest);
        if (!ok) {
            model.addAttribute("error", "El nombre de usuario ya está en uso. Elige otro.");
            return "registro";
        }

        return "redirect:/login?registrado=true";
    }

    @GetMapping("/nueva-receta")
    public String mostrarFormularioReceta(Model model) {
        model.addAttribute("receta", new RecetaEntity());
        return "nueva-receta";
    }

    @PostMapping("/nueva-receta")
    public String publicarReceta(
            @RequestParam String nombre,
            @RequestParam String ingredientes,
            @RequestParam String instrucciones,
            @RequestParam String tipoCocina,
            @RequestParam String paisOrigen,
            @RequestParam int tiempoCoccion,
            @RequestParam String dificultad,
            Model model) {

        if (esBlanco(nombre) || esBlanco(ingredientes) || esBlanco(instrucciones)) {
            model.addAttribute("error", "Nombre, ingredientes e instrucciones son obligatorios.");
            model.addAttribute("receta", new RecetaEntity());
            return "nueva-receta";
        }

        RecetaEntity nueva = new RecetaEntity();
        nueva.setNombre(nombre.trim());
        nueva.setIngredientes(ingredientes.trim());
        nueva.setInstrucciones(instrucciones.trim());
        nueva.setTipoCocina(tipoCocina != null ? tipoCocina.trim() : "Tradicional");
        nueva.setPaisOrigen(paisOrigen != null ? paisOrigen.trim() : "Chile");
        nueva.setTiempoCoccion(tiempoCoccion);
        nueva.setDificultad(dificultad != null ? dificultad.trim() : "Media");
        nueva.setDescripcionCorta(nombre.trim() + " — receta publicada por un usuario.");
        nueva.setDescripcionLarga("");
        nueva.setImagen("default.jpg");
        nueva.setPopular(false);

        recetaService.guardar(nueva);

        return "redirect:/buscar?publicada=true";
    }

    private boolean esBlanco(String valor) {
        return valor == null || valor.isBlank();
    }
}
