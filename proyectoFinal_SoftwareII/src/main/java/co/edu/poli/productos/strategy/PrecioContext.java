package co.edu.poli.productos.strategy;

import java.math.BigDecimal;
import java.util.Collection;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.TreeMap;

import org.springframework.stereotype.Component;

import co.edu.poli.productos.exception.ReglaNegocioException;
import co.edu.poli.productos.model.Producto;

/**
 * Contexto del patrón Strategy.
 *
 * Spring inyecta todas las implementaciones de {@link PrecioStrategy}; el
 * contexto las indexa por nombre y delega el cálculo en la elegida. Para
 * agregar una política nueva basta con crear otra clase {@code @Component}
 * que implemente la interfaz, sin modificar este contexto ni el servicio
 * (principio abierto/cerrado).
 */
@Component
public class PrecioContext {

    private final Map<String, PrecioStrategy> estrategias = new TreeMap<>();

    public PrecioContext(List<PrecioStrategy> implementaciones) {
        for (PrecioStrategy estrategia : implementaciones) {
            estrategias.put(estrategia.getNombre(), estrategia);
        }
    }

    /**
     * Calcula el precio final con la estrategia indicada. Si el nombre es nulo
     * o vacío se usa {@link SinDescuentoStrategy}.
     *
     * @throws ReglaNegocioException si la estrategia no existe
     */
    public BigDecimal calcular(String nombreEstrategia, Producto producto) {
        return obtener(nombreEstrategia).calcularPrecioFinal(producto);
    }

    public PrecioStrategy obtener(String nombreEstrategia) {
        String clave = (nombreEstrategia == null || nombreEstrategia.isBlank())
                ? SinDescuentoStrategy.NOMBRE
                : nombreEstrategia.trim().toUpperCase(Locale.ROOT);

        PrecioStrategy estrategia = estrategias.get(clave);

        if (estrategia == null) {
            throw new ReglaNegocioException("Estrategia de precio no válida: '" + nombreEstrategia
                    + "'. Disponibles: " + estrategias.keySet());
        }

        return estrategia;
    }

    public Collection<PrecioStrategy> disponibles() {
        return estrategias.values();
    }
}
