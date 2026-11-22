package co.edu.poli.productos.dto;

import java.math.BigDecimal;
import java.time.LocalDate;

import co.edu.poli.productos.model.Categoria;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Digits;
import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;
import jakarta.validation.constraints.Size;

/**
 * Datos que envía el cliente para crear o actualizar un producto.
 *
 * No incluye {@code id} ni {@code fechaCreacion}: los genera el sistema.
 * Si {@code stockMinimo}, {@code requiereFormula} o {@code activo} no se
 * envían, se usan los valores predeterminados (5, false y true).
 */
@Schema(description = "Datos para crear o actualizar un producto")
public class ProductoRequestDTO {

    @Schema(example = "Acetaminofén 500 mg")
    @NotBlank(message = "El nombre es obligatorio")
    @Size(max = 150, message = "El nombre no puede superar 150 caracteres")
    private String nombre;

    @Schema(example = "Paracetamol")
    @Size(max = 150, message = "El principio activo no puede superar 150 caracteres")
    private String principioActivo;

    @Schema(example = "Caja x 20 tabletas")
    @Size(max = 100, message = "La presentación no puede superar 100 caracteres")
    private String presentacion;

    @Schema(example = "L2026-045")
    @Size(max = 100, message = "El lote no puede superar 100 caracteres")
    private String lote;

    @Schema(example = "2027-06-30")
    @FutureOrPresent(message = "La fecha de vencimiento no puede ser anterior a hoy")
    private LocalDate fechaVencimiento;

    @Schema(example = "8500.00")
    @NotNull(message = "El precio es obligatorio")
    @DecimalMin(value = "0.00", message = "El precio no puede ser negativo")
    @Digits(integer = 10, fraction = 2, message = "El precio admite hasta 10 enteros y 2 decimales")
    private BigDecimal precio;

    @Schema(example = "120")
    @NotNull(message = "El stock es obligatorio")
    @PositiveOrZero(message = "El stock no puede ser negativo")
    private Integer stock;

    @Schema(example = "5", description = "Opcional. Por defecto 5")
    @PositiveOrZero(message = "El stock mínimo no puede ser negativo")
    private Integer stockMinimo;

    @Schema(example = "false", description = "Opcional. Por defecto false")
    private Boolean requiereFormula;

    @Schema(example = "MEDICAMENTO")
    @NotNull(message = "La categoría es obligatoria")
    private Categoria categoria;

    @Schema(example = "true", description = "Opcional. Por defecto true")
    private Boolean activo;

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getPrincipioActivo() {
        return principioActivo;
    }

    public void setPrincipioActivo(String principioActivo) {
        this.principioActivo = principioActivo;
    }

    public String getPresentacion() {
        return presentacion;
    }

    public void setPresentacion(String presentacion) {
        this.presentacion = presentacion;
    }

    public String getLote() {
        return lote;
    }

    public void setLote(String lote) {
        this.lote = lote;
    }

    public LocalDate getFechaVencimiento() {
        return fechaVencimiento;
    }

    public void setFechaVencimiento(LocalDate fechaVencimiento) {
        this.fechaVencimiento = fechaVencimiento;
    }

    public BigDecimal getPrecio() {
        return precio;
    }

    public void setPrecio(BigDecimal precio) {
        this.precio = precio;
    }

    public Integer getStock() {
        return stock;
    }

    public void setStock(Integer stock) {
        this.stock = stock;
    }

    public Integer getStockMinimo() {
        return stockMinimo;
    }

    public void setStockMinimo(Integer stockMinimo) {
        this.stockMinimo = stockMinimo;
    }

    public Boolean getRequiereFormula() {
        return requiereFormula;
    }

    public void setRequiereFormula(Boolean requiereFormula) {
        this.requiereFormula = requiereFormula;
    }

    public Categoria getCategoria() {
        return categoria;
    }

    public void setCategoria(Categoria categoria) {
        this.categoria = categoria;
    }

    public Boolean getActivo() {
        return activo;
    }

    public void setActivo(Boolean activo) {
        this.activo = activo;
    }
}
