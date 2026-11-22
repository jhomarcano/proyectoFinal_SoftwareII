package co.edu.poli.productos.controller;

import java.net.URI;
import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import co.edu.poli.productos.dto.PrecioFinalResponseDTO;
import co.edu.poli.productos.dto.ProductoRequestDTO;
import co.edu.poli.productos.dto.ProductoResponseDTO;
import co.edu.poli.productos.service.ProductoService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;

/**
 * Capa de presentación: expone el CRUD de productos y el cálculo del precio
 * final. Recibe y devuelve DTO, nunca la entidad, y delega en el servicio.
 */
@RestController
@RequestMapping("/api/productos")
@Tag(name = "Productos", description = "Gestión de productos de la farmacia")
public class ProductoController {

    private final ProductoService productoService;

    public ProductoController(ProductoService productoService) {
        this.productoService = productoService;
    }

    @GetMapping
    @Operation(summary = "Listar todos los productos")
    @ApiResponse(responseCode = "200", description = "Listado de productos")
    public List<ProductoResponseDTO> listar() {
        return productoService.listar();
    }

    @GetMapping("/{id}")
    @Operation(summary = "Consultar un producto por id")
    @ApiResponse(responseCode = "200", description = "Producto encontrado")
    @ApiResponse(responseCode = "404", description = "El producto no existe")
    public ProductoResponseDTO obtenerPorId(@PathVariable Long id) {
        return productoService.obtenerPorId(id);
    }

    @PostMapping
    @Operation(summary = "Crear un producto")
    @ApiResponse(responseCode = "201", description = "Producto creado")
    @ApiResponse(responseCode = "400", description = "Datos inválidos")
    public ResponseEntity<ProductoResponseDTO> crear(@Valid @RequestBody ProductoRequestDTO solicitud) {
        ProductoResponseDTO creado = productoService.crear(solicitud);
        return ResponseEntity.created(URI.create("/api/productos/" + creado.getId())).body(creado);
    }

    @PutMapping("/{id}")
    @Operation(summary = "Actualizar un producto")
    @ApiResponse(responseCode = "200", description = "Producto actualizado")
    @ApiResponse(responseCode = "400", description = "Datos inválidos")
    @ApiResponse(responseCode = "404", description = "El producto no existe")
    public ProductoResponseDTO actualizar(@PathVariable Long id,
            @Valid @RequestBody ProductoRequestDTO solicitud) {
        return productoService.actualizar(id, solicitud);
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Eliminar un producto")
    @ApiResponse(responseCode = "204", description = "Producto eliminado")
    @ApiResponse(responseCode = "404", description = "El producto no existe")
    public ResponseEntity<Void> eliminar(@PathVariable Long id) {
        productoService.eliminar(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/{id}/precio-final")
    @Operation(summary = "Calcular el precio final con una estrategia de precio (patrón Strategy)",
            description = "Estrategias: SIN_DESCUENTO (por defecto), PROXIMO_VENCIMIENTO y CATEGORIA.")
    @ApiResponse(responseCode = "200", description = "Precio calculado")
    @ApiResponse(responseCode = "400", description = "Estrategia inválida o producto vencido")
    @ApiResponse(responseCode = "404", description = "El producto no existe")
    public PrecioFinalResponseDTO calcularPrecioFinal(@PathVariable Long id,
            @Parameter(description = "Nombre de la estrategia", example = "PROXIMO_VENCIMIENTO")
            @RequestParam(required = false) String estrategia) {
        return productoService.calcularPrecioFinal(id, estrategia);
    }
}
