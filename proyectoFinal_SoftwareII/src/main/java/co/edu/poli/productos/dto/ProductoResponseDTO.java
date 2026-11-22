package co.edu.poli.productos.dto;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

import co.edu.poli.productos.model.Categoria;
import io.swagger.v3.oas.annotations.media.Schema;

/**
 * Datos del producto que la API devuelve al cliente.
 *
 * Además de los atributos persistidos, expone tres indicadores calculados
 * por el dominio: vencido, por vencer y stock bajo.
 */
@Schema(description = "Producto devuelto por la API")
public class ProductoResponseDTO {

    private Long id;
    private String nombre;
    private String principioActivo;
    private String presentacion;
    private String lote;
    private LocalDate fechaVencimiento;
    private BigDecimal precio;
    private Integer stock;
    private Integer stockMinimo;
    private Boolean requiereFormula;
    private Categoria categoria;
    private Boolean activo;
    private LocalDateTime fechaCreacion;
    private boolean vencido;
    private boolean porVencer;
    private boolean stockBajo;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

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

    public LocalDateTime getFechaCreacion() {
        return fechaCreacion;
    }

    public void setFechaCreacion(LocalDateTime fechaCreacion) {
        this.fechaCreacion = fechaCreacion;
    }

    public boolean isVencido() {
        return vencido;
    }

    public void setVencido(boolean vencido) {
        this.vencido = vencido;
    }

    public boolean isPorVencer() {
        return porVencer;
    }

    public void setPorVencer(boolean porVencer) {
        this.porVencer = porVencer;
    }

    public boolean isStockBajo() {
        return stockBajo;
    }

    public void setStockBajo(boolean stockBajo) {
        this.stockBajo = stockBajo;
    }
}
