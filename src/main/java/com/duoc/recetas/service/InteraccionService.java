package com.duoc.recetas.service;

import com.duoc.recetas.entity.ComentarioEntity;
import com.duoc.recetas.entity.MedioRecetaEntity;
import com.duoc.recetas.entity.RecetaEntity;
import com.duoc.recetas.repository.ComentarioRepository;
import com.duoc.recetas.repository.MedioRecetaRepository;
import com.duoc.recetas.repository.RecetaRepository;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

@Service
public class InteraccionService {

    private final ComentarioRepository comentarioRepository;
    private final MedioRecetaRepository medioRepository;
    private final RecetaRepository recetaRepository;

    private final Path uploadDir = Paths.get("uploads");

    public InteraccionService(ComentarioRepository comentarioRepository,
                              MedioRecetaRepository medioRepository,
                              RecetaRepository recetaRepository) {
        this.comentarioRepository = comentarioRepository;
        this.medioRepository      = medioRepository;
        this.recetaRepository     = recetaRepository;
    }

    // ── COMENTARIOS ──────────────────────────────────────────────────

    public List<ComentarioEntity> getComentariosPorReceta(Long recetaId) {
        // Línea 42-43: recetaId viene de @PathVariable Long, nunca es null en la práctica,
        // pero el compilador JDT no lo sabe. Objects.requireNonNull elimina el warning.
        Objects.requireNonNull(recetaId, "recetaId no puede ser null");
        return comentarioRepository.findByRecetaIdOrderByFechaCreacionDesc(recetaId);
    }

    public void agregarComentario(Long recetaId, String autor,
                                  String contenido, int valoracion) {
        Objects.requireNonNull(recetaId, "recetaId no puede ser null");

        if (contenido == null || contenido.isBlank()) {
            throw new IllegalArgumentException("El comentario no puede estar vacío");
        }
        if (valoracion < 1 || valoracion > 5) {
            throw new IllegalArgumentException("La valoración debe estar entre 1 y 5");
        }

        RecetaEntity receta = recetaRepository.findById(recetaId)
                .orElseThrow(() -> new IllegalArgumentException("Receta no encontrada"));

        ComentarioEntity comentario = new ComentarioEntity();
        comentario.setReceta(receta);
        comentario.setAutor(autor);
        comentario.setContenido(sanitizar(contenido));
        comentario.setValoracion(valoracion);
        comentarioRepository.save(comentario);
    }

    // ── MEDIOS (FOTOS / VIDEOS) ───────────────────────────────────────

    public List<MedioRecetaEntity> getMediosPorReceta(Long recetaId) {
        Objects.requireNonNull(recetaId, "recetaId no puede ser null");
        return medioRepository.findByRecetaIdOrderByFechaSubidaDesc(recetaId);
    }

    public void subirMedio(Long recetaId, MultipartFile archivo, String usuario)
            throws IOException {

        Objects.requireNonNull(recetaId, "recetaId no puede ser null");

        if (archivo == null || archivo.isEmpty()) {
            throw new IllegalArgumentException("El archivo no puede estar vacío");
        }

        // Línea 88: getOriginalFilename() puede retornar null según la API de MultipartFile.
        // Usamos un String vacío como fallback seguro.
        String rawFilename = archivo.getOriginalFilename();
        String nombreOriginal = (rawFilename != null) ? rawFilename.trim() : "";

        // Eliminar componentes de ruta para evitar path traversal
        int lastSep = Math.max(nombreOriginal.lastIndexOf('/'), nombreOriginal.lastIndexOf('\\'));
        if (lastSep >= 0) {
            nombreOriginal = nombreOriginal.substring(lastSep + 1);
        }

        String extension = nombreOriginal.contains(".")
                ? nombreOriginal.substring(nombreOriginal.lastIndexOf('.')).toLowerCase()
                : "";

        List<String> extensionesPermitidas =
                List.of(".jpg", ".jpeg", ".png", ".gif", ".webp", ".mp4", ".mov", ".avi");
        if (!extensionesPermitidas.contains(extension)) {
            throw new IllegalArgumentException("Tipo de archivo no permitido: " + extension);
        }

        if (archivo.getSize() > 50L * 1024 * 1024) {
            throw new IllegalArgumentException("El archivo supera el límite de 50 MB");
        }

        String tipo = extension.matches("\\.(mp4|mov|avi)") ? "VIDEO" : "FOTO";

        // UUID evita colisiones y path traversal en el nombre final
        String nombreGuardado = UUID.randomUUID().toString() + extension;

        Files.createDirectories(uploadDir);
        Path destino = uploadDir.resolve(nombreGuardado).normalize();
        if (!destino.startsWith(uploadDir.toAbsolutePath())) {
            throw new IllegalArgumentException("Ruta de archivo inválida");
        }
        Files.copy(archivo.getInputStream(), destino, StandardCopyOption.REPLACE_EXISTING);

        // Línea 119: mismo caso que línea 58 — requireNonNull ya fue llamado arriba.
        RecetaEntity receta = recetaRepository.findById(recetaId)
                .orElseThrow(() -> new IllegalArgumentException("Receta no encontrada"));

        MedioRecetaEntity medio = new MedioRecetaEntity();
        medio.setReceta(receta);
        medio.setTipo(tipo);
        medio.setNombreArchivo(nombreGuardado);
        medio.setSubidoPor(usuario);
        medioRepository.save(medio);
    }

    // ── UTILIDADES ───────────────────────────────────────────────────

    private String sanitizar(String texto) {
        return texto.replace("&",  "&amp;")
                    .replace("<",  "&lt;")
                    .replace(">",  "&gt;")
                    .replace("\"", "&quot;")
                    .replace("'",  "&#x27;");
    }
}
