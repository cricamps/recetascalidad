# RecetasChef - ISY2202
**Seguridad y Calidad en el Desarrollo de Software**

## Descripción
Aplicación web para compartir recetas de cocina, desarrollada con Spring Boot + Spring Security + Thymeleaf.

## Tecnologías
- Java 17
- Spring Boot 3.2.5
- Spring Security 6
- Thymeleaf + thymeleaf-extras-springsecurity6
- Maven
- CSS3 (diseño responsivo)

## Cómo ejecutar

### Opción 1 - Doble clic
Ejecutar `iniciar.bat`

### Opción 2 - Terminal
```bash
cd C:\recetascalidad
mvnw.cmd spring-boot:run
```

La aplicación estará disponible en: **http://localhost:8080/recetas**

## Usuarios de prueba
| Usuario  | Contraseña  | Rol         |
|----------|-------------|-------------|
| usuario  | usuario123  | USER        |
| chef     | chef123     | USER, CHEF  |
| admin    | admin123    | USER, ADMIN |

## Páginas de la aplicación
| URL                      | Acceso   | Descripción                             |
|--------------------------|----------|-----------------------------------------|
| `/recetas/`              | Pública  | Página de inicio con recetas destacadas |
| `/recetas/home`          | Pública  | Página de inicio                        |
| `/recetas/buscar`        | Pública  | Búsqueda de recetas con filtros         |
| `/recetas/login`         | Pública  | Formulario de autenticación             |
| `/recetas/receta/{id}`   | Privada  | Detalle completo de una receta          |
| `/recetas/logout`        | Privada  | Cierre de sesión                        |

## Estructura del proyecto
```
src/
├── main/
│   ├── java/com/duoc/recetas/
│   │   ├── RecetasApplication.java       ← Clase principal
│   │   ├── config/
│   │   │   └── WebSecurityConfig.java    ← Configuración Spring Security
│   │   ├── controller/
│   │   │   ├── HomeController.java       ← Rutas públicas: home, login
│   │   │   └── RecetaController.java     ← Rutas: buscar (pública), detalle (privada)
│   │   └── model/
│   │       ├── Receta.java               ← Modelo de datos
│   │       └── RecetaData.java           ← Datos estáticos (6 recetas)
│   └── resources/
│       ├── templates/
│       │   ├── home.html                 ← Página de inicio
│       │   ├── login.html                ← Formulario de login
│       │   ├── buscar.html               ← Búsqueda con filtros
│       │   └── detalle.html              ← Detalle de receta (privada)
│       └── static/css/
│           └── style.css                 ← Estilos CSS completos
```
"# recetascalidad" 
"#RecetasCalidad" 
