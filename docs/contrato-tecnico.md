# Contrato técnico

Proyecto de aula de Ingeniería de Software II: aplicación web CRUD para la entidad **Producto** (contexto: farmacia).

**Integrantes:** Juan Esteban Rosero Jiménez y Jhomar Alejandra Cano Ramírez
**Versión:** 1.0 (octubre de 2026)

Este archivo es la versión oficial de las decisiones técnicas que usan los dos integrantes. Los diagramas y el código deben coincidir con él. Si algo cambia, se edita este archivo primero (rama + Pull Request) y se avisa al otro integrante.

## 1. Decisiones técnicas

| Decisión | Valor acordado |
|---|---|
| Lenguaje | Java 25 (LTS) |
| Framework | Spring Boot 4.1.x (versión estable generada por Spring Initializr) |
| Gestor de dependencias | Maven, mediante el Maven Wrapper del proyecto (`mvnw.cmd`) |
| IDE | Spring Tools for Eclipse 5.4.0 |
| Control de versiones | Git (EGit, integrado en Eclipse) y GitHub |
| Group / Artifact | co.edu.poli / productos |
| Paquete base | co.edu.poli.productos |
| Base de datos | PostgreSQL 17 en Docker (imagen `postgres:17`). BD `productos_db`, usuario `productos_user`, clave `productos_pass` (solo para desarrollo) |
| Análisis de calidad | SonarQube Community Build en Docker (imagen `sonarqube:community`) |
| Puertos | API: 8080. PostgreSQL: 5433 en el equipo (5432 dentro del contenedor). SonarQube: 9000 |
| ORM | Spring Data JPA (Hibernate) |
| Documentación de la API | springdoc-openapi 3.1.1. Swagger UI en `http://localhost:8080/swagger-ui.html` |
| Pruebas de la API | Postman. La colección se exporta a la carpeta `postman/` del repositorio |
| Ramas | GitHub Flow: `main` estable y una rama `feature/<tarea>` por tarea, integrada con Pull Request |
| Mensajes de commit | `feat:` código nuevo, `docs:` documentación, `chore:` configuración |

**Notas**

- Lo que compila el código o guarda datos (Java, Spring Boot, PostgreSQL) va en una versión fija e igual en los dos equipos. Por eso se usa `postgres:17` y no `latest`.
- Eclipse, Docker Desktop y Postman pueden ir en su última versión, porque no cambian el resultado de la aplicación.
- El puerto 5433 evita conflicto con una instalación local de PostgreSQL, que usa el 5432. `application.properties` se comparte en el repositorio, por lo que ambos usan los mismos puertos.
- Cada integrante tiene su propia base de datos y su propio SonarQube en Docker. El repositorio comparte la receta (`docker-compose.yml`, `application.properties`), no los datos.

## 2. Paquetes y clases

| Paquete | Capa | Clases |
|---|---|---|
| controller | Presentación (API REST) | ProductoController |
| service | Lógica de negocio | ProductoService (interfaz), ProductoServiceImpl |
| repository | Acceso a datos | ProductoRepository (extiende `JpaRepository<Producto, Long>`) |
| model | Dominio | Producto (entidad), Categoria (enumeración) |
| dto | Transporte de datos | ProductoRequestDTO, ProductoResponseDTO, PrecioFinalResponseDTO |
| exception | Manejo de errores | ProductoNoEncontradoException, ReglaNegocioException, GlobalExceptionHandler |
| config | Configuración | OpenApiConfig |
| strategy | Patrón GoF Strategy (precio final) | PrecioStrategy (interfaz), SinDescuentoStrategy, DescuentoPorVencimientoStrategy, DescuentoPorCategoriaStrategy, PrecioContext |

El controlador no recibe ni devuelve la entidad directamente: usa DTO. Así el cliente no puede enviar campos que genera el sistema (`id`, `fechaCreacion`) y la estructura de la base de datos no queda expuesta.

## 3. Modelo de negocio (entidad Producto)

| Atributo | Tipo | Obligatorio | Valor por defecto |
|---|---|---|---|
| id | Long | Sí (generado) | Autogenerado por la base de datos |
| nombre | String | Sí | — |
| principioActivo | String | No | — |
| presentacion | String | No | — |
| lote | String | No | — |
| fechaVencimiento | LocalDate | No | — |
| precio | BigDecimal | Sí | — |
| stock | Integer | Sí | — |
| stockMinimo | Integer | Sí | 5 |
| requiereFormula | Boolean | Sí | false |
| categoria | Categoria | Sí | — |
| activo | Boolean | Sí | true |
| fechaCreacion | LocalDateTime | Sí (generado) | Asignada por `alCrear()` |

**Categoria (enum):** MEDICAMENTO, CUIDADO_PERSONAL, SUPLEMENTO, DISPOSITIVO_MEDICO, BEBIDA, OTROS. Se guarda como texto (`@Enumerated(EnumType.STRING)`), sin tabla propia.

**Métodos de Producto**

| Método | Descripción |
|---|---|
| `Producto()` | Constructor sin parámetros, exigido por JPA |
| `alCrear()` | Asigna `fechaCreacion` antes de insertar (`@PrePersist`) |
| `estaVencido()` | `true` si hay fecha de vencimiento y es anterior a hoy |
| `estaPorVencer()` | `true` si no está vencido y vence dentro de los próximos 30 días |
| `tieneStockBajo()` | `true` si `stock <= stockMinimo` |

Cada fila de la tabla `productos` representa un producto con su lote. Los métodos `get` y `set` no se listan.


## 4. Endpoints (tercera entrega)

| Método | Ruta | Descripción | Respuestas |
|---|---|---|---|
| GET | /api/productos | Lista los productos | 200 |
| GET | /api/productos/{id} | Consulta un producto | 200, 404 |
| POST | /api/productos | Crea un producto | 201 (+ Location), 400 |
| PUT | /api/productos/{id} | Actualiza un producto | 200, 400, 404 |
| DELETE | /api/productos/{id} | Elimina un producto | 204, 404 |
| GET | /api/productos/{id}/precio-final?estrategia=... | Precio final con una estrategia | 200, 400, 404 |

Los errores se devuelven con el cuerpo `{timestamp, status, error, mensaje, detalles}`.

## 5. Patrón Strategy: precio final

| Estrategia | Regla |
|---|---|
| SIN_DESCUENTO (por defecto) | Precio base |
| PROXIMO_VENCIMIENTO | 40 % si vence en 7 días o menos; 20 % si vence en los próximos 30 días |
| CATEGORIA | Cuidado personal 15 %, suplementos 10 %, bebidas 5 %; resto sin descuento |

No se calcula precio de un producto vencido (400). Para una política nueva se crea otra clase `@Component` que implemente `PrecioStrategy`.
