# Sistema de Gestión de Productos - Farmacia

## 1. Descripción del proyecto

El proyecto **Sistema de Gestión de Productos - Farmacia** tiene como objetivo desarrollar una aplicación para administrar el inventario de una droguería mediante una API REST construida con Java y Spring Boot, conectada a una base de datos relacional PostgreSQL.

El sistema permitirá gestionar medicamentos y otros productos de droguería, como artículos de cuidado personal, suplementos y dispositivos médicos.

Cada producto contará con información como su nombre, categoría, lote, fecha de vencimiento, precio, stock disponible y si requiere fórmula médica.

El proyecto utiliza una arquitectura en capas que separa las responsabilidades de presentación, lógica de negocio, acceso a datos, dominio, transferencia de información, manejo de errores y configuración.

## 2. Objetivo general

Desarrollar una aplicación para gestionar los productos de una farmacia, utilizando una API REST, persistencia en PostgreSQL y una arquitectura en capas que facilite el mantenimiento, las pruebas y la ampliación del sistema.

## 3. Funcionalidades del sistema

El alcance funcional contempla las siguientes operaciones:

- **Crear productos:** registrar productos nuevos en el inventario.
- **Consultar productos:** obtener el listado de productos y consultar un producto por su identificador.
- **Actualizar productos:** modificar la información de un producto existente.
- **Eliminar productos:** eliminar un producto registrado.
- **Validar información:** comprobar los datos recibidos y aplicar las reglas de negocio correspondientes.
- **Controlar el inventario:** almacenar el stock, el stock mínimo, el lote y la fecha de vencimiento.
- **Documentar la API:** facilitar la exploración de los endpoints mediante Swagger/OpenAPI.

> Estado del desarrollo: la estructura preliminar de paquetes y clases se establece antes de implementar el CRUD completo. Las funcionalidades se consideran implementadas únicamente cuando se encuentren desarrolladas y verificadas en el código.

## 4. Tecnologías utilizadas

<table>
  <thead>
    <tr>
      <th>Tecnología</th>
      <th>Uso</th>
    </tr>
  </thead>
  <tbody>
    <tr>
      <td>Java 25 (Eclipse Temurin)</td>
      <td>Lenguaje de programación</td>
    </tr>
    <tr>
      <td>Spring Boot 4.1.1</td>
      <td>Desarrollo de la aplicación y API REST</td>
    </tr>
    <tr>
      <td>Spring Web</td>
      <td>Controladores y endpoints HTTP</td>
    </tr>
    <tr>
      <td>Spring Data JPA</td>
      <td>Persistencia y operaciones sobre la base de datos</td>
    </tr>
    <tr>
      <td>Hibernate</td>
      <td>Mapeo objeto-relacional</td>
    </tr>
    <tr>
      <td>PostgreSQL 17</td>
      <td>Base de datos relacional</td>
    </tr>
    <tr>
      <td>Docker Compose</td>
      <td>Ejecución de PostgreSQL y SonarQube</td>
    </tr>
    <tr>
      <td>Maven</td>
      <td>Gestión de dependencias y construcción</td>
    </tr>
    <tr>
      <td>Spring Tools for Eclipse 5.4.0</td>
      <td>Entorno de desarrollo</td>
    </tr>
    <tr>
      <td>Swagger/OpenAPI</td>
      <td>Documentación de la API</td>
    </tr>
    <tr>
      <td>SonarQube</td>
      <td>Análisis de calidad del código</td>
    </tr>
    <tr>
      <td>Git y GitHub</td>
      <td>Control de versiones y colaboración</td>
    </tr>
    <tr>
      <td>Postman</td>
      <td>Pruebas de los endpoints REST</td>
    </tr>
  </tbody>
</table>

## 5. Arquitectura del proyecto

El proyecto utiliza una arquitectura en capas. Cada paquete tiene una responsabilidad definida y se comunica con las capas correspondientes.

### 5.1. Capa de presentación: `controller`

Paquete: `co.edu.poli.productos.controller`

Clase principal:

- `ProductoController`

Responsabilidades:

- Exponer los endpoints REST.
- Recibir las solicitudes HTTP.
- Validar y procesar los datos de entrada mediante los DTO.
- Delegar las operaciones a `ProductoService`.
- Devolver respuestas HTTP apropiadas.

### 5.2. Capa de lógica de negocio: `service`

Paquete: `co.edu.poli.productos.service`

Clases:

- `ProductoService`
- `ProductoServiceImpl`

Responsabilidades:

- Definir las operaciones disponibles para gestionar productos.
- Implementar las reglas de negocio.
- Coordinar las operaciones de consulta, creación, actualización y eliminación.
- Solicitar las operaciones de persistencia al repositorio.
- Comprobar condiciones relacionadas con el inventario y los datos del producto.

`ProductoService` define el contrato de las operaciones y `ProductoServiceImpl` contiene su implementación.

### 5.3. Capa de acceso a datos: `repository`

Paquete: `co.edu.poli.productos.repository`

Clase:

- `ProductoRepository`

Responsabilidades:

- Acceder a los productos almacenados en PostgreSQL.
- Utilizar Spring Data JPA para las operaciones de persistencia.
- Consultar productos por su identificador.
- Guardar, actualizar y eliminar registros.

El repositorio extenderá `JpaRepository<Producto, Long>`.

### 5.4. Capa de dominio: `model`

Paquete: `co.edu.poli.productos.model`

Clases y tipos:

- `Producto`
- `Categoria`

Responsabilidades:

- Representar los productos de la farmacia.
- Definir los atributos y las reglas propias del dominio.
- Representar las categorías válidas mediante un tipo enumerado.
- Permitir que JPA relacione la entidad con la base de datos.

### 5.5. Capa de transferencia de datos: `dto`

Paquete: `co.edu.poli.productos.dto`

Clases:

- `ProductoRequestDTO`
- `ProductoResponseDTO`

Responsabilidades:

- Recibir los datos enviados por los clientes de la API.
- Definir los datos que se devuelven en las respuestas.
- Evitar que los controladores expongan directamente la entidad de persistencia.
- Facilitar la validación de las solicitudes.

### 5.6. Capa de manejo de errores: `exception`

Paquete: `co.edu.poli.productos.exception`

Clases:

- `ProductoNoEncontradoException`
- `GlobalExceptionHandler`

Responsabilidades:

- Representar los errores de negocio relacionados con productos inexistentes.
- Centralizar el manejo de excepciones.
- Devolver respuestas HTTP adecuadas, por ejemplo, 404 para un producto inexistente y 400 para solicitudes inválidas.

### 5.7. Capa de configuración: `config`

Paquete: `co.edu.poli.productos.config`

Clase:

- `OpenApiConfig`

Responsabilidades:

- Configurar la información de la API.
- Definir el título, la versión y la descripción de la documentación OpenAPI.
- Facilitar la exploración de los endpoints mediante Swagger UI.

## 6. Modelo de datos: Producto

La entidad `Producto` representa los medicamentos y demás artículos de la farmacia.


La entidad <code>Producto</code> representa los medicamentos y demás artículos de la farmacia.

<table>
  <thead>
    <tr>
      <th>Atributo</th>
      <th>Tipo Java</th>
      <th>Descripción</th>
    </tr>
  </thead>
  <tbody>
    <tr>
      <td><code>id</code></td>
      <td><code>Long</code></td>
      <td>Identificador único generado por la base de datos</td>
    </tr>
    <tr>
      <td><code>nombre</code></td>
      <td><code>String</code></td>
      <td>Nombre comercial del producto</td>
    </tr>
    <tr>
      <td><code>principioActivo</code></td>
      <td><code>String</code></td>
      <td>Principio activo, cuando corresponda</td>
    </tr>
    <tr>
      <td><code>presentacion</code></td>
      <td><code>String</code></td>
      <td>Presentación del producto</td>
    </tr>
    <tr>
      <td><code>lote</code></td>
      <td><code>String</code></td>
      <td>Identificador del lote</td>
    </tr>
    <tr>
      <td><code>fechaVencimiento</code></td>
      <td><code>LocalDate</code></td>
      <td>Fecha de vencimiento</td>
    </tr>
    <tr>
      <td><code>precio</code></td>
      <td><code>BigDecimal</code></td>
      <td>Precio del producto</td>
    </tr>
    <tr>
      <td><code>stock</code></td>
      <td><code>Integer</code></td>
      <td>Cantidad disponible</td>
    </tr>
    <tr>
      <td><code>stockMinimo</code></td>
      <td><code>Integer</code></td>
      <td>Cantidad mínima para alertar sobre existencias bajas</td>
    </tr>
    <tr>
      <td><code>requiereFormula</code></td>
      <td><code>Boolean</code></td>
      <td>Indica si requiere fórmula médica</td>
    </tr>
    <tr>
      <td><code>categoria</code></td>
      <td><code>Categoria</code></td>
      <td>Categoría del producto</td>
    </tr>
    <tr>
      <td><code>activo</code></td>
      <td><code>Boolean</code></td>
      <td>Indica si el producto está activo</td>
    </tr>
    <tr>
      <td><code>fechaCreacion</code></td>
      <td><code>LocalDateTime</code></td>
      <td>Fecha y hora de creación del registro</td>
    </tr>
  </tbody>
</table>


### Categorías

El tipo enumerado `Categoria` contempla las siguientes opciones:

- `MEDICAMENTO`
- `CUIDADO_PERSONAL`
- `SUPLEMENTO`
- `DISPOSITIVO_MEDICO`
- `BEBIDA`
- `OTROS`

### Reglas de negocio propuestas

- El nombre del producto es obligatorio.
- El lote es obligatorio.
- La fecha de vencimiento es obligatoria y no debe ser anterior a la fecha actual al registrar un producto.
- El precio debe ser mayor que cero.
- El stock debe ser mayor o igual a cero.
- El stock mínimo tendrá un valor predeterminado de 5.
- `requiereFormula` tendrá un valor predeterminado de `false`.
- `activo` tendrá un valor predeterminado de `true`.
- El principio activo será obligatorio cuando corresponda a la categoría de medicamento.
- La fecha de creación se generará automáticamente.

Estas reglas deben validarse e implementarse en el código; su inclusión en este documento no significa que ya estén activas.

## 7. Estructura del proyecto

La estructura prevista para el código fuente es la siguiente:

```text
proyectoFinal_SoftwareII/
├── docker-compose.yml
├── pom.xml
├── mvnw
├── mvnw.cmd
├── README.md
├── postman/
└── src/
    ├── main/
    │   ├── java/
    │   │   └── co/
    │   │       └── edu/
    │   │           └── poli/
    │   │               └── productos/
    │   │                   ├── ProyectoFinalSoftwareIiApplication.java
    │   │                   ├── controller/
    │   │                   │   └── ProductoController.java
    │   │                   ├── service/
    │   │                   │   ├── ProductoService.java
    │   │                   │   └── ProductoServiceImpl.java
    │   │                   ├── repository/
    │   │                   │   └── ProductoRepository.java
    │   │                   ├── model/
    │   │                   │   ├── Producto.java
    │   │                   │   └── Categoria.java
    │   │                   ├── dto/
    │   │                   │   ├── ProductoRequestDTO.java
    │   │                   │   └── ProductoResponseDTO.java
    │   │                   ├── exception/
    │   │                   │   ├── ProductoNoEncontradoException.java
    │   │                   │   └── GlobalExceptionHandler.java
    │   │                   └── config/
    │   │                       └── OpenApiConfig.java
    │   └── resources/
    │       └── application.properties
    └── test/
        └── java/
            └── co/
                └── edu/
                    └── poli/
                        └── productos/
                             └── ProyectoFinalSoftwareliApplicationTests.java
```

La estructura muestra la organización objetivo. Los nombres de archivos, recursos y pruebas deben ajustarse a los que existan realmente en el repositorio.

## 8. Requisitos previos

Antes de ejecutar el proyecto se necesita:

- Java JDK 25.
- Spring Tools for Eclipse 5.4.0 o un IDE compatible con Maven y Java 25.
- Git.
- Docker Desktop con WSL 2 habilitado.
- Postman para probar los endpoints, cuando se requiera.
- Acceso al repositorio de GitHub.

## 9. Configuración del entorno

### 9.1. Clonar el repositorio

```bash
git clone https://github.com/jhomarcano/proyectoFinal_SoftwareII.git
cd proyectoFinal_SoftwareII
```

El repositorio contiene una subcarpeta adicional con el mismo nombre. Los comandos de ejecución deben realizarse dentro de la carpeta que contiene `pom.xml` y `docker-compose.yml`.

Si el proyecto se clonó desde la raíz del repositorio:

```bash
cd proyectoFinal_SoftwareII
```

### 9.2. Verificar Java y Docker

```bash
java -version
javac -version
docker --version
docker compose version
```

Java debe corresponder a la versión 25 y Docker debe estar ejecutándose.

### 9.3. Levantar PostgreSQL y SonarQube

Desde la carpeta que contiene `docker-compose.yml`, ejecutar:

```bash
docker compose up -d
docker compose ps
```

La configuración acordada utiliza:

| Servicio | Puerto local |
|---|---:|
| API Spring Boot | 8080 |
| PostgreSQL | 5433 |
| SonarQube | 9000 |

La configuración de conexión a PostgreSQL se encuentra en `application.properties` y `docker-compose.yml`.

Base de datos: `productos_db`.

Usuario: `productos_user`.

Contraseña de desarrollo: `productos_pass`.

Estas credenciales son únicamente para el entorno local de desarrollo. No deben utilizarse como credenciales de producción.

### 9.4. Ejecutar la aplicación

En Windows:

```bash
mvnw.cmd spring-boot:run
```

En sistemas Unix:

```bash
./mvnw spring-boot:run
```

La aplicación debe iniciar correctamente y conectarse a PostgreSQL. No debe ejecutarse simultáneamente desde el IDE y desde la terminal utilizando el mismo puerto.

## 10. API REST y operaciones CRUD

La ruta base prevista es:

`/api/productos`

| Operación | Método HTTP | Endpoint | Descripción |
|---|---|---|---|
| Crear | `POST` | `/api/productos` | Registrar un producto |
| Consultar todos | `GET` | `/api/productos` | Obtener el listado |
| Consultar por ID | `GET` | `/api/productos/{id}` | Obtener un producto específico |
| Actualizar | `PUT` | `/api/productos/{id}` | Modificar un producto |
| Eliminar | `DELETE` | `/api/productos/{id}` | Eliminar un producto |

Estos endpoints representan el contrato propuesto para el CRUD. Deben coincidir con las rutas que finalmente se implementen en `ProductoController`.

## 11. Documentación de la API

La API se documentará mediante Swagger/OpenAPI.

Una vez iniciada la aplicación, se puede intentar acceder a:

- Swagger UI: `http://localhost:8080/swagger-ui.html`
- Documentación OpenAPI: `http://localhost:8080/v3/api-docs`

La disponibilidad de estas rutas depende de las dependencias y la configuración incluidas en el proyecto.

## 12. Pruebas

Las pruebas del sistema deben comprobar, como mínimo:

- Creación de productos con información válida.
- Rechazo de datos obligatorios ausentes o inválidos.
- Consulta del listado de productos.
- Consulta por identificador.
- Respuesta cuando un producto no existe.
- Actualización de los datos de un producto.
- Eliminación de un producto.
- Persistencia de la información en PostgreSQL.
- Manejo de errores HTTP.
- Reglas de negocio relacionadas con precios, stock y fechas.

Postman se utilizará para verificar las solicitudes HTTP y sus respuestas. Las pruebas automatizadas se incorporarán según la estructura de pruebas del proyecto.

## 13. Control de versiones y trabajo colaborativo

El proyecto utiliza Git y GitHub para gestionar el trabajo de los integrantes.

La rama `main` se mantiene como rama estable. Cada tarea debe desarrollarse en una rama independiente con un nombre descriptivo, por ejemplo:

```text
feature/estructura-capas
feature/crud-productos
feature/validaciones-productos
```

Los cambios deben integrarse mediante Pull Requests, después de revisar el código y comprobar que la aplicación funciona.

Se seguira esta estructura para los mensajes de commit:

- `feat:` para funcionalidades nuevas.
- `docs:` para documentación.
- `chore:` para configuración y tareas de mantenimiento.
- `test:` para pruebas.
- `fix:` para correcciones de errores.

## 14. Calidad del código

SonarQube se utiliza para analizar la calidad del código y detectar posibles problemas de mantenimiento, errores y vulnerabilidades.

El servicio local se encuentra disponible, si está iniciado, en:

`http://localhost:9000`

El análisis debe ejecutarse con la configuración y el token local de SonarQube de cada integrante.

## 15. Estado del proyecto

El desarrollo se organiza por etapas:

1. Configuración del ambiente y del repositorio.
2. Creación de la estructura de paquetes y clases.
3. Implementación del CRUD de productos.
4. Aplicación de validaciones y manejo centralizado de errores.
5. Pruebas de la API y persistencia.
6. Análisis de calidad y documentación final.

La estructura preliminar constituye la base para implementar las operaciones CRUD. Las funcionalidades deben verificarse antes de presentarse como terminadas.

## 16. Autores

Proyecto académico de Ingeniería de Software II.

Repositorio: [jhomarcano/proyectoFinal_SoftwareII](https://github.com/jhomarcano/proyectoFinal_SoftwareII)

