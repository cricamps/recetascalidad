package com.duoc.recetas.controller;

import com.duoc.recetas.entity.RecetaEntity;
import com.duoc.recetas.service.FavoritoService;
import com.duoc.recetas.service.InteraccionService;
import com.duoc.recetas.service.RecetaService;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.Arrays;
import java.util.List;

@Controller
public class RecetaController {

    private final RecetaService      recetaService;
    private final InteraccionService interaccionService;
    private final FavoritoService    favoritoService;

    public RecetaController(RecetaService recetaService,
                            InteraccionService interaccionService,
                            FavoritoService favoritoService) {
        this.recetaService       = recetaService;
        this.interaccionService  = interaccionService;
        this.favoritoService     = favoritoService;
    }

    // ── BÚSQUEDA ─────────────────────────────────────────────────────

    @GetMapping("/buscar")
    public String buscar(
            @RequestParam(required = false) String nombre,
            @RequestParam(required = false) String tipoCocina,
            @RequestParam(required = false) String ingrediente,
            @RequestParam(required = false) String paisOrigen,
            @RequestParam(required = false) String dificultad,
            Model model) {

        List<RecetaEntity> resultados;

        boolean sinFiltros = esBlanco(nombre) && esBlanco(tipoCocina) &&
                             esBlanco(ingrediente) && esBlanco(paisOrigen) &&
                             esBlanco(dificultad);

        if (sinFiltros) {
            resultados = recetaService.getTodas();
        } else {
            resultados = recetaService.buscar(nombre, tipoCocina, paisOrigen,
                                              dificultad, ingrediente);
        }

        model.addAttribute("resultados", resultados);
        model.addAttribute("nombre", nombre);
        model.addAttribute("tipoCocina", tipoCocina);
        model.addAttribute("ingrediente", ingrediente);
        model.addAttribute("paisOrigen", paisOrigen);
        model.addAttribute("dificultad", dificultad);
        model.addAttribute("totalResultados", resultados.size());

        return "buscar";
    }

    // ── DETALLE DE RECETA ─────────────────────────────────────────────

    @GetMapping("/receta/{id}")
    public String detalleReceta(@PathVariable Long id,
                                 Authentication auth,
                                 Model model) {
        return recetaService.getPorId(id)
                .map(receta -> {
                    List<String> ingredientes =
                        Arrays.asList(receta.getIngredientes().split("\\|"));
                    List<String> instrucciones =
                        Arrays.asList(receta.getInstrucciones().split("\\|"));
                    model.addAttribute("receta", receta);
                    model.addAttribute("ingredientesList", ingredientes);
                    model.addAttribute("instruccionesList", instrucciones);
                    model.addAttribute("comentarios",
                        interaccionService.getComentariosPorReceta(id));
                    model.addAttribute("medios",
                        interaccionService.getMediosPorReceta(id));
                    // ── Favorito: false si no está autenticado ────────
                    boolean esFav = (auth != null)
                            && favoritoService.esFavorito(auth.getName(), id);
                    model.addAttribute("esFavorito", esFav);
                    return "detalle";
                })
                .orElse("redirect:/buscar");
    }

    // ── COMENTAR Y VALORAR (PRIVADO) ──────────────────────────────────

    @PostMapping("/receta/{id}/comentar")
    public String comentar(@PathVariable Long id,
                           @RequestParam String contenido,
                           @RequestParam int valoracion,
                           Authentication auth,
                           RedirectAttributes ra) {
        try {
            interaccionService.agregarComentario(id, auth.getName(),
                                                 contenido, valoracion);
            ra.addFlashAttribute("exito", "¡Comentario agregado correctamente!");
        } catch (IllegalArgumentException e) {
            ra.addFlashAttribute("error", e.getMessage());
        }
        return "redirect:/receta/" + id;
    }

    // ── SUBIR FOTO O VIDEO (PRIVADO) ──────────────────────────────────

    @PostMapping("/receta/{id}/subir-medio")
    public String subirMedio(@PathVariable Long id,
                             @RequestParam("archivo") MultipartFile archivo,
                             Authentication auth,
                             RedirectAttributes ra) {
        try {
            interaccionService.subirMedio(id, archivo, auth.getName());
            ra.addFlashAttribute("exito", "¡Archivo subido correctamente!");
        } catch (Exception e) {
            ra.addFlashAttribute("error", "Error al subir: " + e.getMessage());
        }
        return "redirect:/receta/" + id;
    }

    // ── COMPARTIR (PRIVADO) ──────────────────────────────────────────

    @GetMapping("/receta/{id}/compartir")
    public String compartir(@PathVariable Long id,
                            @RequestParam(defaultValue = "web") String destino,
                            RedirectAttributes ra) {
        return recetaService.getPorId(id)
                .map(receta -> {
                    String titulo = receta.getNombre();
                    String url    = "https://recetascalidad.onrender.com/recetas/receta/" + id;

                    String redirectUrl = switch (destino) {
                        case "twitter" ->
                            "https://twitter.com/intent/tweet?text=" +
                            encode("¡Mira esta receta: " + titulo) + "&url=" + encode(url);
                        case "facebook" ->
                            "https://www.facebook.com/sharer/sharer.php?u=" + encode(url);
                        case "whatsapp" ->
                            "https://wa.me/?text=" + encode(titulo + " " + url);
                        default -> "/receta/" + id + "?compartido=true";
                    };
                    return "redirect:" + redirectUrl;
                })
                .orElse("redirect:/buscar");
    }

    // ── UTILIDADES ────────────────────────────────────────────────────

    private boolean esBlanco(String valor) {
        return valor == null || valor.isBlank();
    }

    private String encode(String texto) {
        try {
            return java.net.URLEncoder.encode(texto, "UTF-8");
        } catch (Exception e) {
            return texto;
        }
    }
}
