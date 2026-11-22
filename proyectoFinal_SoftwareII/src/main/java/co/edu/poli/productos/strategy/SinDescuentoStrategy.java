package co.edu.poli.productos.strategy;

import java.math.BigDecimal;
import java.math.RoundingMode;

import org.springframework.stereotype.Component;

import co.edu.poli.productos.model.Producto;

/**
 * Estrategia por defecto: el precio final es el precio base del producto.
 */
@Component
public class SinDescuentoStrategy implements PrecioStrategy {

    public static final String NOMBRE = "SIN_DESCUENTO";

    @Override
    public String getNombre() {
        return NOMBRE;
    }

    @Override
    public String getDescripcion() {
        return "Precio base, sin descuento.";
    }

    @Override
    public BigDecimal calcularPrecioFinal(Producto producto) {
        return producto.getPrecio().setScale(2, RoundingMode.HALF_UP);
    }
}
