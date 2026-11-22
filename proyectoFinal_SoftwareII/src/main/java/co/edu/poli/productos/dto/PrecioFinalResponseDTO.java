package co.edu.poli.productos.dto;

import java.math.BigDecimal;

import io.swagger.v3.oas.annotations.media.Schema;

/**
 * Resultado del cálculo del precio final de un producto con una estrategia
 * de precio (patrón Strategy).
 */
@Schema(description = "Precio final de un producto según la estrategia elegida")
public class PrecioFinalResponseDTO {

    private Long productoId;
    private String nombre;
    private String estrategia;
    private String descripcionEstrategia;
    private BigDecimal precioBase;
    private BigDecimal precioFinal;
    private BigDecimal descuento;

    public PrecioFinalResponseDTO(Long productoId, String nombre, String estrategia,
            String descripcionEstrategia, BigDecimal precioBase, BigDecimal precioFinal) {
        this.productoId = productoId;
        this.nombre = nombre;
        this.estrategia = estrategia;
        this.descripcionEstrategia = descripcionEstrategia;
        this.precioBase = precioBase;
        this.precioFinal = precioFinal;
        this.descuento = precioBase.subtract(precioFinal);
    }

    public Long getProductoId() {
        return productoId;
    }

    public String getNombre() {
        return nombre;
    }

    public String getEstrategia() {
        return estrategia;
    }

    public String getDescripcionEstrategia() {
        return descripcionEstrategia;
    }

    public BigDecimal getPrecioBase() {
        return precioBase;
    }

    public BigDecimal getPrecioFinal() {
        return precioFinal;
    }

    public BigDecimal getDescuento() {
        return descuento;
    }
}
