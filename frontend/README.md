# Frontend — RecetasChef

Este directorio contiene la capa de **presentación desacoplada** del backend.

## Arquitectura de capas

```
┌─────────────────────────────────────────────────────┐
│  FRONTEND (esta carpeta)                            │
│  HTML + CSS + JavaScript puro                       │
│  Se comunica con el backend ÚNICAMENTE via API REST │
│  No tiene acceso a la base de datos ni servicios    │
└───────────────────┬─────────────────────────────────┘
                    │  HTTP/JSON  (JWT en Authorization header)
                    ▼
┌─────────────────────────────────────────────────────┐
│  BACKEND  (src/main/java)                           │
│  Spring Boot + Spring Security + Spring Data JPA    │
│  Expone API REST en  /api/**                        │
│  Gestiona autenticación JWT, lógica de negocio y BD │
└───────────────────┬─────────────────────────────────┘
                    │  JPA/SQL
                    ▼
┌─────────────────────────────────────────────────────┐
│  BASE DE DATOS  (MySQL / H2)                        │
│  Tablas: recetas, usuarios, comentarios, medios     │
└─────────────────────────────────────────────────────┘
```

## Separación de responsabilidades (SoC)

| Capa | Responsabilidad | Tecnología |
|------|----------------|------------|
| Frontend | Presentación, interacción usuario | HTML/CSS/JS |
| Backend | Lógica de negocio, seguridad, API | Spring Boot |
| Base de datos | Persistencia de datos | MySQL/H2 |

## Endpoints API disponibles

### Públicos (sin autenticación)
- `POST /api/auth/login` — obtener token JWT
- `GET  /api/recetas` — listar todas las recetas
- `GET  /api/recetas/{id}` — detalle de una receta
- `GET  /api/recetas/buscar?nombre=...` — buscar recetas

### Privados (requieren `Authorization: Bearer <token>`)
- `GET  /api/me` — datos del usuario autenticado
- `GET  /api/recetas/populares` — recetas populares
- `GET  /api/recetas/recientes` — recetas recientes

## Cómo ejecutar el frontend

Abrir `index.html` directamente en el navegador, o servir con cualquier servidor estático:

```bash
# Python (simple)
cd frontend
python -m http.server 3000

# Node.js (npx)
npx serve frontend
```

El frontend apunta al backend en `http://localhost:8080/recetas` por defecto.
Para cambiar la URL del backend, editar la constante `API_BASE` en `js/api.js`.
