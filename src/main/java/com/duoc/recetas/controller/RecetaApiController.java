package com.duoc.recetas.controller;

import com.duoc.recetas.entity.RecetaEntity;
import com.duoc.recetas.service.RecetaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * API REST de Recetas - requiere JWT (protegida por apiFilterChain)
 *
 * GET /api/recetas              - todas las recetas
 * GET /api/recetas/populares    - recetas populares
 * GET /api/recetas/recientes    - ultimas 6 recetas
 * GET /api/recetas/{id}         - detalle de una receta
 * GET /api/recetas/buscar       - busqueda con filtros
 */
@RestController
@RequestMapping("/api/recetas")
public class RecetaApiController {

    @Autowired
    private RecetaService recetaService;

    @GetMapping
    public ResponseEntity<List<RecetaEntity>> getTodas() {
        return ResponseEntity.ok(recetaService.getTodas());
    }

    @GetMapping("/populares")
    public ResponseEntity<List<RecetaEntity>> getPopulares() {
        return ResponseEntity.ok(recetaService.getPopulares());
    }

    @GetMapping("/recientes")
    public ResponseEntity<List<RecetaEntity>> getRecientes() {
        return ResponseEntity.ok(recetaService.getRecientes());
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getPorId(@PathVariable Long id) {
        return recetaService.getPorId(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/buscar")
    public ResponseEntity<List<RecetaEntity>> buscar(
            @RequestParam(required = false) String nombre,
            @RequestParam(required = false) String tipoCocina,
            @RequestParam(required = false) String paisOrigen,
            @RequestParam(required = false) String dificultad,
            @RequestParam(required = false) String ingrediente) {
        return ResponseEntity.ok(recetaService.buscar(nombre, tipoCocina, paisOrigen, dificultad, ingrediente));
    }
}
