# GestionaPro — API Backend

Una API REST para gestionar productos, categorías, usuarios y ventas, construida con **Spring Boot** y asegurada con **autenticación JWT** y **control de acceso por roles**.

🔗 **API en producción:** [https://gestionapro.onrender.com](https://gestionapro.onrender.com)
📄 **Documentación (Swagger):** [https://gestionapro.onrender.com/swagger-ui/index.html](https://gestionapro.onrender.com/swagger-ui/index.html)
🎨 **Repositorio del frontend:** [gestionapro-frontend](https://github.com/FavianRamos/gestionapro-frontend)

> ⚠️ La API está alojada en un servicio gratuito y puede tardar entre 30 y 60 segundos en responder en la primera petición tras un período de inactividad.

---

## Descripción

GestionaPro es un sistema de gestión para pequeños negocios que permite a un **Administrador** gestionar productos, categorías, usuarios y ventas, mientras que un **Usuario** regular puede explorar el catálogo, realizar compras y ver su propio historial de compras. El acceso está controlado de principio a fin mediante autenticación JWT y autorización basada en roles.

## Funcionalidades

- **Autenticación y autorización**
  - Registro e inicio de sesión de usuarios con JWT
  - Control de acceso por roles (`ADMIN` / `USER`)
  - Contraseñas encriptadas con BCrypt

- **Gestión de productos y categorías**
  - CRUD completo de productos y categorías
  - Búsqueda de productos por nombre
  - Control de stock

- **Ventas**
  - Registro de ventas con validación automática de stock (rechaza la venta si no hay stock suficiente)
  - Cálculo automático de subtotal / IGV (18%) / total, persistido por cada venta
  - Historial de ventas paginado (admin: todas las ventas con filtros por usuario y rango de fechas; usuario: solo su propio historial)

- **Reportes** (solo admin)
  - Ventas por período (agrupadas por mes)
  - Ingresos totales por rango de fechas
  - Productos más vendidos
  - Ventas por categoría
  - Productos con stock bajo

- **Calidad y documentación**
  - Tests unitarios con JUnit 5, Mockito y AssertJ
  - Manejo global de excepciones con códigos de estado HTTP adecuados
  - Documentación interactiva de la API con Swagger / OpenAPI
  - Paginación en los endpoints de listado

## Tecnologías utilizadas

| Capa | Tecnología |
|---|---|
| Lenguaje | Java 21 |
| Framework | Spring Boot |
| Seguridad | Spring Security, JWT |
| Persistencia | Spring Data JPA / Hibernate |
| Base de datos | MySQL (alojada en Aiven) |
| Testing | JUnit 5, Mockito, AssertJ |
| Documentación | springdoc-openapi (Swagger UI) |
| Contenedores | Docker (build multi-etapa) |
| Despliegue | Render |

## Arquitectura

El proyecto está organizado **por funcionalidad/entidad** en vez de por capa técnica — cada dominio (`product`, `category`, `user`, `sale`, `auth`, `report`, `security`, `config`, `exception`) contiene su propia entidad, DTOs, repositorio, servicio (interfaz + implementación) y controlador.

com.example.projectProduct
├── auth # Login/registro, generación de JWT
├── category # CRUD de categorías
├── config # Configuración de seguridad y Swagger
├── exception # Manejo global de excepciones
├── product # CRUD de productos, búsqueda, stock, reporte de stock bajo
├── report # Reportes de ventas, ingresos, top productos, categorías
├── sale # Venta/DetalleVenta, lógica de checkout
├── security # Filtro JWT, reglas de acceso por rol
└── user # Gestión de usuarios, control de rol y estado


Decisiones de diseño clave:
- **DTOs para cada entidad** — las entidades nunca se exponen directamente a través de la API.
- **Interfaz + implementación** para los servicios (ej. `ProductService` / `ProductServiceImpl`), manteniendo la lógica de negocio flexible y fácil de testear.
- **Java records** para los DTOs de request/response del módulo de ventas.

## Cómo ejecutarlo localmente

### Requisitos previos
- Java 21
- Maven
- Una instancia de MySQL (local o remota)

### Variables de entorno
Crea las siguientes variables de entorno (o un archivo `application-local.properties` / `.env` local, según tu configuración):

DB_URL=jdbc:mysql://localhost:3306/tu_base_de_datos
DB_USERNAME=tu_usuario
DB_PASSWORD=tu_contraseña
JWT_SECRET_KEY=tu_clave_secreta
FRONTEND_URL=http://localhost:5173


### Ejecutar
```bash
./mvnw spring-boot:run
```

La API estará disponible en `http://localhost:8080`, y Swagger UI en `http://localhost:8080/swagger-ui/index.html`.

### Ejecutar con Docker
```bash
docker build -t gestionapro-backend .
docker run -p 8080:8080 --env-file .env gestionapro-backend
```

### Ejecutar tests
```bash
./mvnw test
```

## Endpoints principales

| Método | Endpoint | Descripción | Acceso |
|---|---|---|---|
| POST | `/api/auth/register` | Registrar un nuevo usuario | Público |
| POST | `/api/auth/login` | Autenticarse y obtener un JWT | Público |
| GET/POST/PUT/DELETE | `/api/products` | Gestionar productos | Admin |
| GET | `/api/products?name=` | Buscar productos por nombre | Autenticado |
| GET/POST/PUT/DELETE | `/api/categories` | Gestionar categorías | Admin |
| GET/PATCH | `/api/users` | Listar usuarios, cambiar rol/estado | Admin |
| POST | `/api/sales` | Registrar una venta (checkout) | Autenticado |
| GET | `/api/sales` | Todas las ventas, con filtros | Admin |
| GET | `/api/sales/my` | Historial de compras propio | Autenticado |
| GET | `/api/reports/*` | Reportes de ventas, ingresos, top productos, categorías, stock bajo | Admin |

Los detalles completos de request/response están disponibles en [Swagger UI](https://gestionapro.onrender.com/swagger-ui/index.html).

## Autor

**Favian Ramos Garay** — Desarrollador Backend (Java / Spring Boot)
