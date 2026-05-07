# RecetasChef — Monorepo

Plataforma web para compartir recetas de cocina chilena.
Proyecto desarrollado para **ISY2202 — Seguridad y Calidad en el Desarrollo de Software · Duoc UC**.

## Estructura del Repositorio

Este repositorio contiene **dos proyectos completamente independientes**:

```
recetascalidad/
├── backend/      # Proyecto Spring Boot (Java 17, Maven)
└── frontend/     # Proyecto SPA (HTML/CSS/Vanilla JS, Node.js)
```

Cada proyecto tiene su propio ecosistema de dependencias, herramientas y pruebas, sin dependencias cruzadas entre sí.

## Backend

- **Tecnología:** Java 17 + Spring Boot 3.5 + Spring Security + JPA
- **Puerto:** 8080
- **Documentación:** [backend/README.md](backend/README.md)

```bash
cd backend
mvn spring-boot:run
```

## Frontend

- **Tecnología:** HTML5 + CSS3 + Vanilla JS (ES Modules)
- **Puerto:** 3000
- **Documentación:** [frontend/README.md](frontend/README.md)

```bash
cd frontend
npm install
npm start
```

## Comunicación entre capas

```
Frontend (puerto 3000)  ──── HTTP/REST + JWT ────►  Backend (puerto 8080)
                                                          │
                                                          ▼
                                                    Base de datos
                                                    (H2 / MySQL)
```

El frontend **nunca** accede directamente a la base de datos.
Toda la comunicación ocurre mediante la API REST del backend, autenticada con JWT.

## Integrantes

- Cristobal Andres Camps De La Maza
- Cynthia Alejandra Torres Leal

**Asignatura:** ISY2202 — Seguridad y Calidad en el Desarrollo de Software  
**Institución:** Duoc UC | Carrera: Analista Programador  
**Profesor:** Christian Álvarez
