# RecetasChef — Backend

Capa de lógica de negocio y API REST de la plataforma RecetasChef.
Proyecto **completamente independiente** del frontend.

## Tecnologías

| Tecnología | Versión | Uso |
|---|---|---|
| Java | 17 | Lenguaje principal |
| Spring Boot | 3.5 | Framework principal |
| Spring Security | 6.x | Autenticación y autorización |
| Spring Data JPA | 3.5 | Persistencia ORM |
| H2 | runtime | Base de datos embebida (desarrollo y tests) |
| MySQL | runtime | Base de datos (producción) |
| jjwt | 0.12.6 | Generación y validación de JWT |
| JaCoCo | 0.8.12 | Cobertura de código |
| SonarQube Plugin | 4.0.0 | Análisis estático |
| OWASP Dependency Check | 12.1.0 | Análisis de vulnerabilidades |

## Requisitos

- Java 17+
- Maven 3.9+

## Instalación y Ejecución

```bash
cd backend

# Ejecutar la aplicación
mvn spring-boot:run

# Ejecutar tests
mvn test

# Análisis SonarQube
mvn test sonar:sonar "-Dsonar.host.url=http://localhost:9000" "-Dsonar.token=<TOKEN>"
```

La aplicación estará disponible en: `http://localhost:8080/recetas`

## Usuarios por Defecto

| Usuario | Contraseña | Rol(es) |
|---|---|---|
| `usuario` | `usuario123` | ROLE_USER |
| `chef` | `chef123` | ROLE_USER, ROLE_CHEF |
| `admin` | `admin123` | ROLE_USER, ROLE_ADMIN |

## Arquitectura de Seguridad

Dos filter chains diferenciadas:

- **Cadena 1** (`/api/**`): Stateless, sin sesión, sin CSRF. Autenticación por JWT.
- **Cadena 2** (Web/Thymeleaf): Con sesión HTTP, CSRF habilitado, login por formulario.

## API REST

| Endpoint | Método | Auth | Descripción |
|---|---|---|---|
| `/api/auth/login` | POST | ❌ | Obtener JWT (accessToken + refreshToken) |
| `/api/auth/refresh-token` | POST | ❌ | Renovar JWT |
| `/api/recetas` | GET | ✅ | Listar todas las recetas |
| `/api/recetas/{id}` | GET | ✅ | Detalle de receta |
| `/api/recetas/populares` | GET | ✅ | Recetas populares |
| `/api/recetas/recientes` | GET | ✅ | Recetas recientes |
| `/api/recetas/buscar` | GET | ✅ | Búsqueda por filtros |
| `/api/me` | GET | ✅ | Datos del usuario autenticado |

## Resultados de Calidad (EFT)

| Métrica | Resultado |
|---|---|
| Quality Gate SonarQube | ✅ PASSED |
| Security Rating | A (0 issues) |
| Reliability Rating | A (0 issues) |
| Maintainability Rating | A |
| Cobertura JaCoCo | 93.3% |
| Duplicaciones | 1.3% |
| Security Hotspots | 0 |
| Tests JUnit | 222 tests / 0 fallos |

## Despliegue

Producción en Render.com: https://recetascalidad.onrender.com/recetas

## Asignatura

**ISY2202** — Seguridad y Calidad en el Desarrollo de Software · Duoc UC
