package com.duoc.recetas.controller;

import com.duoc.recetas.entity.RecetaEntity;
import com.duoc.recetas.service.RecetaService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/recetas")
public class RecetaApiController {

    private final RecetaService recetaService;

    public RecetaApiController(RecetaService recetaService) {
        this.recetaService = recetaService;
    }

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
    public ResponseEntity<Object> getPorId(@PathVariable Long id) {
        return recetaService.getPorId(id)
                .map(r -> (Object) r)
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
        return ResponseEntity.ok(
            recetaService.buscar(nombre, tipoCocina, paisOrigen, dificultad, ingrediente));
    }
}
