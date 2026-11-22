package co.edu.poli.productos.strategy;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

import org.junit.jupiter.api.Test;

import co.edu.poli.productos.exception.ReglaNegocioException;
import co.edu.poli.productos.model.Categoria;
import co.edu.poli.productos.model.Producto;

class PrecioStrategyTest {

    private Producto producto(String precio, Categoria categoria, LocalDate vence) {
        Producto p = new Producto();
        p.setPrecio(new BigDecimal(precio));
        p.setCategoria(categoria);
        p.setFechaVencimiento(vence);
        return p;
    }

    @Test
    void sinDescuentoDevuelvePrecioBase() {
        Producto p = producto("10000", Categoria.MEDICAMENTO, null);
        assertEquals(new BigDecimal("10000.00"), new SinDescuentoStrategy().calcularPrecioFinal(p));
    }

    @Test
    void vencimientoCritico_40PorCiento() {
        Producto p = producto("10000", Categoria.MEDICAMENTO, LocalDate.now().plusDays(5));
        assertEquals(new BigDecimal("6000.00"), new DescuentoPorVencimientoStrategy().calcularPrecioFinal(p));
    }

    @Test
    void vencimientoEnUnMes_20PorCiento() {
        Producto p = producto("10000", Categoria.MEDICAMENTO, LocalDate.now().plusDays(20));
        assertEquals(new BigDecimal("8000.00"), new DescuentoPorVencimientoStrategy().calcularPrecioFinal(p));
    }

    @Test
    void vencimientoLejanoOSinFecha_sinDescuento() {
        DescuentoPorVencimientoStrategy s = new DescuentoPorVencimientoStrategy();
        assertEquals(new BigDecimal("10000.00"),
                s.calcularPrecioFinal(producto("10000", Categoria.OTROS, LocalDate.now().plusDays(200))));
        assertEquals(new BigDecimal("10000.00"),
                s.calcularPrecioFinal(producto("10000", Categoria.OTROS, null)));
    }

    @Test
    void categoriaAplicaPorcentajeCorrespondiente() {
        DescuentoPorCategoriaStrategy s = new DescuentoPorCategoriaStrategy();
        assertEquals(new BigDecimal("8500.00"), s.calcularPrecioFinal(producto("10000", Categoria.CUIDADO_PERSONAL, null)));
        assertEquals(new BigDecimal("9000.00"), s.calcularPrecioFinal(producto("10000", Categoria.SUPLEMENTO, null)));
        assertEquals(new BigDecimal("9500.00"), s.calcularPrecioFinal(producto("10000", Categoria.BEBIDA, null)));
        assertEquals(new BigDecimal("10000.00"), s.calcularPrecioFinal(producto("10000", Categoria.MEDICAMENTO, null)));
    }

    @Test
    void redondeaADosDecimales() {
        Producto p = producto("9.99", Categoria.SUPLEMENTO, null);
        assertEquals(new BigDecimal("8.99"), new DescuentoPorCategoriaStrategy().calcularPrecioFinal(p));
    }

    @Test
    void contextoSeleccionaPorNombreYUsaDefecto() {
        PrecioContext ctx = new PrecioContext(List.of(new SinDescuentoStrategy(),
                new DescuentoPorVencimientoStrategy(), new DescuentoPorCategoriaStrategy()));
        Producto p = producto("10000", Categoria.BEBIDA, null);

        assertEquals(new BigDecimal("9500.00"), ctx.calcular("categoria", p));
        assertEquals(new BigDecimal("10000.00"), ctx.calcular(null, p));
        assertEquals(new BigDecimal("10000.00"), ctx.calcular("  ", p));
        assertEquals(3, ctx.disponibles().size());
    }

    @Test
    void contextoRechazaEstrategiaDesconocida() {
        PrecioContext ctx = new PrecioContext(List.of(new SinDescuentoStrategy()));
        assertThrows(ReglaNegocioException.class, () -> ctx.obtener("NO_EXISTE"));
    }
}
