package co.edu.poli.productos.strategy;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

import org.springframework.stereotype.Component;

import co.edu.poli.productos.model.Producto;

/**
 * Descuento para productos próximos a vencer, con el fin de rotar el inventario.
 *
 * Reglas: si faltan 7 días o menos, 40 %; si faltan entre 8 y 30 días
 * (según {@link Producto#estaPorVencer()}), 20 %; en otro caso, sin descuento.
 */
@Component
public class DescuentoPorVencimientoStrategy implements PrecioStrategy {

    public static final String NOMBRE = "PROXIMO_VENCIMIENTO";

    static final BigDecimal DESCUENTO_7_DIAS = BigDecimal.valueOf(40);
    static final BigDecimal DESCUENTO_30_DIAS = BigDecimal.valueOf(20);
    static final long DIAS_CRITICOS = 7;

    @Override
    public String getNombre() {
        return NOMBRE;
    }

    @Override
    public String getDescripcion() {
        return "40 % si vence en 7 días o menos; 20 % si vence en los próximos 30 días.";
    }

    @Override
    public BigDecimal calcularPrecioFinal(Producto producto) {
        if (!producto.estaPorVencer()) {
            return producto.getPrecio().setScale(2, RoundingMode.HALF_UP);
        }

        long diasRestantes = ChronoUnit.DAYS.between(LocalDate.now(), producto.getFechaVencimiento());
        BigDecimal porcentaje = diasRestantes <= DIAS_CRITICOS ? DESCUENTO_7_DIAS : DESCUENTO_30_DIAS;

        return aplicarDescuento(producto.getPrecio(), porcentaje);
    }
}
