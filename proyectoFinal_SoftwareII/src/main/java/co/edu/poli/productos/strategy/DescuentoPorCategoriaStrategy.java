package co.edu.poli.productos.strategy;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.EnumMap;
import java.util.Map;

import org.springframework.stereotype.Component;

import co.edu.poli.productos.model.Categoria;
import co.edu.poli.productos.model.Producto;

/**
 * Descuento promocional según la categoría del producto.
 *
 * Los medicamentos y dispositivos médicos no tienen descuento por categoría.
 */
@Component
public class DescuentoPorCategoriaStrategy implements PrecioStrategy {

    public static final String NOMBRE = "CATEGORIA";

    private static final Map<Categoria, BigDecimal> DESCUENTOS = new EnumMap<>(Categoria.class);

    static {
        DESCUENTOS.put(Categoria.CUIDADO_PERSONAL, BigDecimal.valueOf(15));
        DESCUENTOS.put(Categoria.SUPLEMENTO, BigDecimal.valueOf(10));
        DESCUENTOS.put(Categoria.BEBIDA, BigDecimal.valueOf(5));
    }

    @Override
    public String getNombre() {
        return NOMBRE;
    }

    @Override
    public String getDescripcion() {
        return "Cuidado personal 15 %, suplementos 10 %, bebidas 5 %; las demás categorías sin descuento.";
    }

    @Override
    public BigDecimal calcularPrecioFinal(Producto producto) {
        BigDecimal porcentaje = DESCUENTOS.get(producto.getCategoria());

        if (porcentaje == null) {
            return producto.getPrecio().setScale(2, RoundingMode.HALF_UP);
        }

        return aplicarDescuento(producto.getPrecio(), porcentaje);
    }
}
