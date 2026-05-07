# RecetasChef — Frontend Desacoplado

Capa de presentación de la plataforma **RecetasChef**, implementada como una SPA (Single Page Application) estática en HTML5, CSS3 y Vanilla JavaScript con ES Modules.

## Separación de Capas (Arquitectura)

```
┌─────────────────────────────────┐     HTTP/REST + JWT      ┌──────────────────────────────────┐
│   FRONTEND (este proyecto)      │ ◄────────────────────── ► │   BACKEND (recetascalidad/)      │
│                                 │                            │                                  │
│  HTML + CSS + Vanilla JS        │   GET  /api/recetas        │  Spring Boot 3.5                 │
│  Sin acceso a BD                │   POST /api/auth/login     │  Spring Security + JWT           │
│  Sin lógica de negocio          │   POST /api/auth/refresh   │  Spring Data JPA + H2/MySQL      │
│  Solo presentación y UX         │   GET  /api/me             │  Expone API REST en /api/**      │
└─────────────────────────────────┘                            └──────────────────────────────────┘
         Puerto 3000                                                    Puerto 8080
```

**Principio central:** este proyecto no importa ninguna clase Java, no accede directamente a la base de datos y no contiene lógica de negocio. Toda la data proviene del backend a través de llamadas HTTP autenticadas con JWT.

## Estructura del Proyecto

```
frontend/
├── index.html          # Página principal — recetas recientes y populares
├── login.html          # Formulario de autenticación
├── buscar.html         # Búsqueda de recetas con filtros
├── css/
│   └── style.css       # Estilos globales (variables CSS, componentes)
├── js/
│   ├── api.js          # Capa de comunicación con el backend (único punto de acceso a la API)
│   └── __tests__/      # Pruebas unitarias del módulo api.js
│       └── api.test.js
├── package.json        # Dependencias y scripts del proyecto frontend
├── .eslintrc.json      # Configuración de calidad de código (ESLint)
├── .env.example        # Variables de entorno de ejemplo
└── README.md           # Este archivo
```

## Requisitos Previos

- Node.js 18+
- El backend RecetasChef ejecutándose en `http://localhost:8080`

## Instalación y Ejecución

```bash
# 1. Instalar dependencias de desarrollo
cd frontend
npm install

# 2. Iniciar servidor de desarrollo (con live-reload)
npm run dev

# 3. O usar un servidor estático simple
npm start
```

Abrir en el navegador: `http://localhost:3000`

## Configuración del Backend

El archivo `js/api.js` apunta por defecto a `http://localhost:8080/recetas/api`.

Para cambiar la URL del backend, editar la constante `API_BASE` en `js/api.js`:

```javascript
const API_BASE = 'http://localhost:8080/recetas/api'; // desarrollo local
// const API_BASE = 'https://recetascalidad.onrender.com/recetas/api'; // producción
```

## Autenticación JWT

El flujo de autenticación implementado:

1. El usuario ingresa credenciales en `login.html`
2. El frontend llama `POST /api/auth/login` con `{ username, password }`
3. El backend valida con Spring Security + BCrypt y devuelve `{ accessToken, refreshToken }`
4. El frontend almacena el token en `sessionStorage` (no en `localStorage` por seguridad)
5. Cada petición subsiguiente incluye el header `Authorization: Bearer <token>`
6. Al expirar el accessToken, `api.js` llama `POST /api/auth/refresh-token` automáticamente

## Usuarios de Prueba

| Usuario   | Contraseña   | Rol(es)                    |
|-----------|-------------|----------------------------|
| `usuario` | `usuario123` | ROLE_USER                  |
| `chef`    | `chef123`    | ROLE_USER, ROLE_CHEF       |
| `admin`   | `admin123`   | ROLE_USER, ROLE_ADMIN      |

## Pruebas Unitarias

```bash
# Ejecutar tests del módulo api.js
npm test

# Con reporte de cobertura
npm run test:coverage
```

## Tecnologías

| Tecnología | Versión | Uso |
|---|---|---|
| HTML5 | — | Estructura de vistas |
| CSS3 | — | Estilos, variables CSS, Grid/Flexbox |
| Vanilla JS | ES2022 | Lógica de frontend (ES Modules) |
| Fetch API | — | Comunicación HTTP con el backend |
| sessionStorage | — | Almacenamiento temporal del JWT |
| Jest + jsdom | 29.x | Pruebas unitarias del módulo api.js |
| ESLint | 8.x | Calidad y estilo de código |
| live-server | — | Servidor de desarrollo con hot-reload |

## Relación con el Backend

Este frontend consume exclusivamente los endpoints públicos y autenticados del backend:

| Endpoint | Método | Auth | Descripción |
|---|---|---|---|
| `/api/auth/login` | POST | ❌ | Obtener JWT |
| `/api/auth/refresh-token` | POST | ❌ | Renovar JWT |
| `/api/recetas` | GET | ✅ | Listar todas |
| `/api/recetas/{id}` | GET | ✅ | Detalle de receta |
| `/api/recetas/populares` | GET | ✅ | Recetas populares |
| `/api/recetas/recientes` | GET | ✅ | Recetas recientes |
| `/api/recetas/buscar` | GET | ✅ | Búsqueda por filtros |
| `/api/me` | GET | ✅ | Datos del usuario autenticado |

## Asignatura

Proyecto desarrollado para la asignatura **ISY2202 — Seguridad y Calidad en el Desarrollo de Software**
Institución: **Duoc UC** | Carrera: Analista Programador
