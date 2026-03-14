package com.duoc.recetas.controller;

import com.duoc.recetas.model.Receta;
import com.duoc.recetas.model.RecetaData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Controller
public class RecetaController {

    @Autowired
    private RecetaData recetaData;

    // PÚBLICA - Búsqueda de recetas
    @GetMapping("/buscar")
    public String buscar(
            @RequestParam(required = false) String nombre,
            @RequestParam(required = false) String tipoCocina,
            @RequestParam(required = false) String ingrediente,
            @RequestParam(required = false) String paisOrigen,
            @RequestParam(required = false) String dificultad,
            Model model) {

        List<Receta> resultados;

        // Si no hay filtros, mostrar todas
        if ((nombre == null || nombre.isBlank()) &&
            (tipoCocina == null || tipoCocina.isBlank()) &&
            (ingrediente == null || ingrediente.isBlank()) &&
            (paisOrigen == null || paisOrigen.isBlank()) &&
            (dificultad == null || dificultad.isBlank())) {
            resultados = recetaData.getTodasLasRecetas();
        } else {
            resultados = recetaData.buscarRecetas(nombre, tipoCocina, ingrediente, paisOrigen, dificultad);
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
    public String detalleReceta(@PathVariable int id, Model model) {
        Receta receta = recetaData.getRecetaPorId(id);
        if (receta == null) {
            return "redirect:/buscar";
        }
        model.addAttribute("receta", receta);
        return "detalle";
    }
}
