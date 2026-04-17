# RecetasChef — ISY2202

Aplicación web de recetas de cocina desarrollada en la asignatura **ISY2202 Seguridad y Calidad en el Desarrollo de Software** — Duoc UC.

## Arquitectura de capas (Separación de Responsabilidades)

```
frontend/          ← Capa de Presentación (HTML + CSS + JS puro)
│  index.html      │  Consume la API REST del backend via fetch() + JWT
│  login.html      │  NO tiene acceso directo a la BD
│  buscar.html     │  Se puede servir desde cualquier servidor estático
│  css/style.css
│  js/api.js       ← Único punto de contacto con el backend

src/main/java/     ← Capa de Negocio y Seguridad (Spring Boot)
│  controller/     │  Expone API REST en /api/** y vistas SSR en /**
│  service/        │  Lógica de negocio, validaciones, sanitización
│  security/       │  JWT, Spring Security, CSRF, headers de seguridad
│  repository/     │  Acceso a datos via Spring Data JPA
│  entity/         │  Modelos de datos

base_de_datos/     ← Capa de Datos (MySQL / H2)
   init.sql        │  Estructura de tablas y datos iniciales
```

## Seguridad implementada

### Spring Security — Dos cadenas de filtros

| Cadena | Ruta | Tipo | Autenticación |
|--------|------|------|---------------|
| `apiFilterChain` | `/api/**` | Stateless | JWT en header `Authorization: Bearer` |
| `webFilterChain` | `/**` | Con sesión | Form login + CSRF token |

### Rutas públicas vs privadas

**API REST (`/api/**`):**
- Pública: `POST /api/auth/login`
- Privada: todo lo demás (requiere JWT válido)

**Web Thymeleaf (`/**`):**
- Pública: `/`, `/home`, `/buscar`, `/receta/{id}`, `/login`, `/registro`
- Privada: `/nueva-receta`, `/receta/*/comentar`, `/receta/*/subir-medio`, `/receta/*/compartir`

### Otras medidas de seguridad
- CSRF habilitado en capa web (deshabilitado en API REST — correcto por diseño)
- Headers HTTP: CSP, HSTS, X-Content-Type-Options, Referrer-Policy
- Contraseñas con BCrypt (factor 12)
- Sanitización de inputs en comentarios
- Validación de extensiones en subida de archivos
- UUID para nombres de archivos (previene path traversal)
- CORS configurado para la API REST

## Stack tecnológico

| Capa | Tecnología |
|------|-----------|
| Frontend | HTML5 + CSS3 + JavaScript (ES Modules) |
| Backend | Spring Boot 3.5.13 + Spring Security 6.x |
| API | REST/JSON con JWT (JJWT 0.12.6) |
| Vistas SSR | Thymeleaf + thymeleaf-extras-springsecurity6 |
| Persistencia | Spring Data JPA + Hibernate |
| BD desarrollo | H2 en memoria |
| BD producción | MySQL (Render.com) |
| Análisis SCA | OWASP Dependency Check 12.1.0 |
| Análisis SAST | SonarQube Community Edition |

## Cómo ejecutar

```bash
# Backend
mvn spring-boot:run

# Frontend desacoplado (opcional, requiere backend corriendo)
cd frontend
python -m http.server 3000
# Abrir http://localhost:3000
```

## Despliegue

- **App**: https://recetascalidad.onrender.com/recetas
- **Repositorio**: https://github.com/cricamps/recetascalidad
