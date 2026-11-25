# proyectoFinal_SoftwareII

# Inventario de Farmacia — CRUD de Productos

Proyecto final de Ingeniería de Software II (Politécnico Grancolombiano). Aplicación web CRUD para la entidad **Producto**, con arquitectura en capas, patrón de diseño GoF **Strategy**, Docker, ORM, Swagger, Postman, SonarQube y GitHub.

**Integrantes:** Juan Esteban Rosero Jiménez ([@Juanes04af](https://github.com/Juanes04af)) · Jhomar Alejandra Cano Ramírez ([@jhomarcano](https://github.com/jhomarcano))

**Video explicativo:**[Video Expicativo](https://youtu.be/hzqVQ0FwENs)

## Tecnologías

| Componente | Tecnología |
|---|---|
| Lenguaje / framework | Java 25, Spring Boot 4.1.3 |
| ORM | Spring Data JPA (Hibernate) |
| Base de datos | PostgreSQL 17 (Docker) |
| Documentación API | springdoc-openapi 3.1.1 (Swagger UI) |
| Pruebas de API | Postman (`postman/`) |
| Calidad de código | SonarQube Community (Docker) |

## Arquitectura en capas

```
controller  →  service  →  repository  →  PostgreSQL
     │            │
     dto       strategy (patrón Strategy)
```

| Paquete | Responsabilidad |
|---|---|
| `controller` | API REST (`ProductoController`) |
| `service` | Lógica de negocio y validaciones (`ProductoService`, `ProductoServiceImpl`) |
| `repository` | Acceso a datos (`ProductoRepository`) |
| `model` | Entidad `Producto` y enumeración `Categoria` |
| `dto` | `ProductoRequestDTO`, `ProductoResponseDTO`, `PrecioFinalResponseDTO` |
| `exception` | `ProductoNoEncontradoException`, `ReglaNegocioException`, `GlobalExceptionHandler` |
| `strategy` | Patrón Strategy para el cálculo del precio final |
| `config` | `OpenApiConfig` |

## Patrón Strategy

El precio final de un producto se calcula con una estrategia intercambiable. `PrecioContext` recibe todas las implementaciones de `PrecioStrategy` y elige la pedida por nombre; agregar una política nueva no requiere modificar el servicio ni el controlador.

| Estrategia | Regla |
|---|---|
| `SIN_DESCUENTO` (por defecto) | Precio base |
| `PROXIMO_VENCIMIENTO` | 40 % si vence en 7 días o menos; 20 % si vence en 30 días o menos |
| `CATEGORIA` | Cuidado personal 15 %, suplemento 10 %, bebida 5 % |

## Cómo ejecutar

Requisitos: Docker Desktop y, para correr sin Docker, JDK 25.

**Opción A — Todo en Docker**

```bash
cd proyectoFinal_SoftwareII
docker compose --profile app up --build
```

**Opción B — App local (desde Eclipse o Maven) y base de datos en Docker**

```bash
cd proyectoFinal_SoftwareII
docker compose up -d postgres
./mvnw spring-boot:run        # en Windows: mvnw.cmd spring-boot:run
```

Una vez arriba:

| Qué | URL |
|---|---|
| Interfaz web (CRUD visual) | http://localhost:8080/ |
| Swagger UI | http://localhost:8080/swagger-ui.html |
| API | http://localhost:8080/api/productos |

## Endpoints

| Método | Ruta | Descripción | Respuestas |
|---|---|---|---|
| GET | `/api/productos` | Lista los productos | 200 |
| GET | `/api/productos/{id}` | Obtiene un producto | 200, 404 |
| POST | `/api/productos` | Crea un producto | 201, 400 |
| PUT | `/api/productos/{id}` | Actualiza un producto | 200, 400, 404 |
| DELETE | `/api/productos/{id}` | Elimina un producto | 204, 404 |
| GET | `/api/productos/{id}/precio-final?estrategia=` | Precio final con la estrategia indicada | 200, 400, 404 |

## Pruebas

- **Unitarias y de integración:** `./mvnw test`
- **Postman:** importar `postman/Productos_Farmacia.postman_collection.json` y ejecutarla en orden con el Collection Runner (la app debe estar en el puerto 8080).
- **SonarQube:** `docker compose up -d sonarqube`, abrir http://localhost:9000, crear un token y ejecutar
  `./mvnw verify sonar:sonar -Dsonar.token=<TU_TOKEN>`. No subas el token al repositorio.

## Flujo de trabajo

GitHub Flow: `main` estable y una rama `feature/<tarea>` por tarea, integrada con Pull Request. Las decisiones técnicas están en [`docs/contrato-tecnico.md`](docs/contrato-tecnico.md).
>>>>>>> eca5a35 (ft: interfaz web, Dockerfile, colección Postman y README actualizado)
