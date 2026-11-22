package co.edu.poli.productos.strategy;

import java.math.BigDecimal;
import java.math.RoundingMode;

import co.edu.poli.productos.model.Producto;

/**
 * Patrón GoF Strategy: define la familia de algoritmos para calcular
 * el precio final de venta de un {@link Producto}.
 *
 * Cada estrategia concreta encapsula una política de precio distinta.
 * El servicio no conoce los detalles de ninguna: solo depende de esta
 * interfaz a través de {@link PrecioContext}.
 */
public interface PrecioStrategy {

    BigDecimal CIEN = BigDecimal.valueOf(100);

    /**
     * Nombre único con el que el cliente elige la estrategia
     * (por ejemplo, {@code SIN_DESCUENTO}).
     */
    String getNombre();

    /**
     * Descripción breve de la política de precio, útil para documentación.
     */
    String getDescripcion();

    /**
     * Calcula el precio final del producto según la política de la estrategia.
     *
     * @param producto producto sobre el que se calcula el precio
     * @return precio final con dos decimales
     */
    BigDecimal calcularPrecioFinal(Producto producto);

    /**
     * Aplica un descuento porcentual a un precio, redondeando a dos decimales.
     */
    default BigDecimal aplicarDescuento(BigDecimal precio, BigDecimal porcentaje) {
        BigDecimal descuento = precio.multiply(porcentaje).divide(CIEN, 2, RoundingMode.HALF_UP);
        return precio.subtract(descuento).setScale(2, RoundingMode.HALF_UP);
    }
}
