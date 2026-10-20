package co.edu.poli.productos.model;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.PrePersist;
import jakarta.persistence.Table;

/**
 * Representa un producto registrado en la farmacia.
 */
@Entity
@Table(name = "productos")

public class Producto {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 150)
    private String nombre;

    @Column(length = 150)
    private String principioActivo;

    @Column(length = 100)
    private String presentacion;

    @Column(nullable = false, length = 100)
    private String lote;

    @Column(nullable = false)
    private LocalDate fechaVencimiento;

    @Column(nullable = false, precision = 12, scale = 2)
    private BigDecimal precio;

    @Column(nullable = false)
    private Integer stock;

    @Column(nullable = false)
    private Integer stockMinimo = 5;

    @Column(nullable = false)
    private Boolean requiereFormula = false;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 30)
    private Categoria categoria;

    @Column(nullable = false)
    private Boolean activo = true;

    @Column(nullable = false, updatable = false)
    private LocalDateTime fechaCreacion;

    /**
     * Constructor vacío requerido por JPA.
     */
    public Producto() {
    }

    /**
     * Inicializa los valores predeterminados antes de persistir
     * una nueva entidad.
     */
    @PrePersist
    protected void alCrear() {
        if (stockMinimo == null) {
            stockMinimo = 5;
        }

        if (requiereFormula == null) {
            requiereFormula = false;
        }

        if (activo == null) {
            activo = true;
        }

        if (fechaCreacion == null) {
            fechaCreacion = LocalDateTime.now();
        }
    }

    /**
     * Indica si el producto ya ha vencido.
     */
    public boolean estaVencido() {
        return fechaVencimiento != null
                && fechaVencimiento.isBefore(LocalDate.now());
    }

    /**
     * Indica si el producto tiene existencias iguales o inferiores
     * al stock mínimo.
     */
    public boolean tieneStockBajo() {
        return stock != null
                && stockMinimo != null
                && stock <= stockMinimo;
    }

    /**
     * Indica si el producto está próximo a vencer.
     * Se consideran los siguientes 30 días, incluido el día actual.
     */
    public boolean estaPorVencer() {
        if (fechaVencimiento == null) {
            return false;
        }

        LocalDate hoy = LocalDate.now();
        LocalDate limite = hoy.plusDays(30);

        return !estaVencido()
                && !fechaVencimiento.isAfter(limite);
    }

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
}