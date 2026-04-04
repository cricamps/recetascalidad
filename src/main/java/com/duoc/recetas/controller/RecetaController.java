package com.duoc.recetas.controller;

import com.duoc.recetas.entity.RecetaEntity;
import com.duoc.recetas.service.RecetaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Arrays;
import java.util.List;

@Controller
public class RecetaController {

    @Autowired
    private RecetaService recetaService;

    // PÚBLICA - Búsqueda de recetas (consume servicio BD)
    @GetMapping("/buscar")
    public String buscar(
            @RequestParam(required = false) String nombre,
            @RequestParam(required = false) String tipoCocina,
            @RequestParam(required = false) String ingrediente,
            @RequestParam(required = false) String paisOrigen,
            @RequestParam(required = false) String dificultad,
            Model model) {

        List<RecetaEntity> resultados;

        boolean sinFiltros = (nombre == null || nombre.isBlank()) &&
                             (tipoCocina == null || tipoCocina.isBlank()) &&
                             (ingrediente == null || ingrediente.isBlank()) &&
                             (paisOrigen == null || paisOrigen.isBlank()) &&
                             (dificultad == null || dificultad.isBlank());

        if (sinFiltros) {
            resultados = recetaService.getTodas();
        } else {
            resultados = recetaService.buscar(nombre, tipoCocina, paisOrigen, dificultad, ingrediente);
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

    // PRIVADA - Detalle de receta (requiere autenticación)
    @GetMapping("/receta/{id}")
    public String detalleReceta(@PathVariable Long id, Model model) {
        return recetaService.getPorId(id)
                .map(receta -> {
                    List<String> ingredientes = Arrays.asList(receta.getIngredientes().split("\\|"));
                    List<String> instrucciones = Arrays.asList(receta.getInstrucciones().split("\\|"));
                    model.addAttribute("receta", receta);
                    model.addAttribute("ingredientesList", ingredientes);
                    model.addAttribute("instruccionesList", instrucciones);
                    return "detalle";
                })
                .orElse("redirect:/buscar");
    }
}
