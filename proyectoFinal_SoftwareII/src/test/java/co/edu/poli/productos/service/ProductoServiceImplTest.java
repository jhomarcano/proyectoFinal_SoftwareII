package co.edu.poli.productos.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import co.edu.poli.productos.dto.PrecioFinalResponseDTO;
import co.edu.poli.productos.dto.ProductoRequestDTO;
import co.edu.poli.productos.dto.ProductoResponseDTO;
import co.edu.poli.productos.exception.ProductoNoEncontradoException;
import co.edu.poli.productos.exception.ReglaNegocioException;
import co.edu.poli.productos.model.Categoria;
import co.edu.poli.productos.model.Producto;
import co.edu.poli.productos.repository.ProductoRepository;
import co.edu.poli.productos.strategy.DescuentoPorCategoriaStrategy;
import co.edu.poli.productos.strategy.DescuentoPorVencimientoStrategy;
import co.edu.poli.productos.strategy.PrecioContext;
import co.edu.poli.productos.strategy.SinDescuentoStrategy;

@ExtendWith(MockitoExtension.class)
class ProductoServiceImplTest {

    @Mock
    private ProductoRepository repository;

    private ProductoServiceImpl service;

    @BeforeEach
    void setUp() {
        PrecioContext contexto = new PrecioContext(List.of(new SinDescuentoStrategy(),
                new DescuentoPorVencimientoStrategy(), new DescuentoPorCategoriaStrategy()));
        service = new ProductoServiceImpl(repository, contexto);
    }

    private ProductoRequestDTO solicitudValida() {
        ProductoRequestDTO dto = new ProductoRequestDTO();
        dto.setNombre("  Ibuprofeno 400 mg ");
        dto.setPrecio(new BigDecimal("12000"));
        dto.setStock(30);
        dto.setCategoria(Categoria.MEDICAMENTO);
        return dto;
    }

    private Producto entidad(Long id) {
        Producto p = new Producto();
        p.setId(id);
        p.setNombre("Vitamina C");
        p.setPrecio(new BigDecimal("10000"));
        p.setStock(10);
        p.setCategoria(Categoria.SUPLEMENTO);
        return p;
    }

    @Test
    void crearAplicaValoresPredeterminadosYLimpiaNombre() {
        when(repository.save(any(Producto.class))).thenAnswer(inv -> {
            Producto p = inv.getArgument(0);
            p.setId(1L);
            return p;
        });

        ProductoResponseDTO r = service.crear(solicitudValida());

        assertEquals(1L, r.getId());
        assertEquals("Ibuprofeno 400 mg", r.getNombre());
        assertEquals(5, r.getStockMinimo());
        assertFalse(r.getRequiereFormula());
        assertTrue(r.getActivo());
    }

    @Test
    void crearRechazaPrecioNegativo() {
        ProductoRequestDTO dto = solicitudValida();
        dto.setPrecio(new BigDecimal("-1"));

        assertThrows(ReglaNegocioException.class, () -> service.crear(dto));
        verify(repository, never()).save(any());
    }

    @Test
    void crearRechazaStockNegativoYStockMinimoNegativo() {
        ProductoRequestDTO a = solicitudValida();
        a.setStock(-1);
        assertThrows(ReglaNegocioException.class, () -> service.crear(a));

        ProductoRequestDTO b = solicitudValida();
        b.setStockMinimo(-2);
        assertThrows(ReglaNegocioException.class, () -> service.crear(b));
    }

    @Test
    void crearRechazaFechaDeVencimientoPasada() {
        ProductoRequestDTO dto = solicitudValida();
        dto.setFechaVencimiento(LocalDate.now().minusDays(1));

        assertThrows(ReglaNegocioException.class, () -> service.crear(dto));
    }

    @Test
    void obtenerPorIdInexistenteLanzaNoEncontrado() {
        when(repository.findById(99L)).thenReturn(Optional.empty());
        assertThrows(ProductoNoEncontradoException.class, () -> service.obtenerPorId(99L));
    }

    @Test
    void actualizarConservaOpcionalesNoEnviados() {
        Producto existente = entidad(3L);
        existente.setStockMinimo(20);
        existente.setActivo(false);
        when(repository.findById(3L)).thenReturn(Optional.of(existente));
        when(repository.save(any(Producto.class))).thenAnswer(inv -> inv.getArgument(0));

        ProductoResponseDTO r = service.actualizar(3L, solicitudValida());

        assertEquals("Ibuprofeno 400 mg", r.getNombre());
        assertEquals(20, r.getStockMinimo());
        assertFalse(r.getActivo());
    }

    @Test
    void actualizarInexistenteLanzaNoEncontrado() {
        when(repository.findById(7L)).thenReturn(Optional.empty());
        assertThrows(ProductoNoEncontradoException.class, () -> service.actualizar(7L, solicitudValida()));
    }

    @Test
    void eliminarInexistenteLanzaNoEncontrado() {
        when(repository.existsById(5L)).thenReturn(false);
        assertThrows(ProductoNoEncontradoException.class, () -> service.eliminar(5L));
        verify(repository, never()).deleteById(any());
    }

    @Test
    void eliminarExistenteBorra() {
        when(repository.existsById(5L)).thenReturn(true);
        service.eliminar(5L);
        verify(repository).deleteById(5L);
    }

    @Test
    void precioFinalUsaLaEstrategiaElegida() {
        when(repository.findById(3L)).thenReturn(Optional.of(entidad(3L)));

        PrecioFinalResponseDTO r = service.calcularPrecioFinal(3L, "CATEGORIA");

        assertEquals("CATEGORIA", r.getEstrategia());
        assertEquals(new BigDecimal("9000.00"), r.getPrecioFinal());
        assertEquals(new BigDecimal("1000.00"), r.getDescuento());
    }

    @Test
    void precioFinalSinEstrategiaUsaPorDefecto() {
        when(repository.findById(3L)).thenReturn(Optional.of(entidad(3L)));

        PrecioFinalResponseDTO r = service.calcularPrecioFinal(3L, null);

        assertEquals("SIN_DESCUENTO", r.getEstrategia());
        assertEquals(new BigDecimal("10000.00"), r.getPrecioFinal());
    }

    @Test
    void precioFinalRechazaEstrategiaInvalidaYProductoVencido() {
        Producto vencido = entidad(4L);
        vencido.setFechaVencimiento(LocalDate.now().minusDays(2));
        when(repository.findById(4L)).thenReturn(Optional.of(vencido));
        assertThrows(ReglaNegocioException.class, () -> service.calcularPrecioFinal(4L, "CATEGORIA"));

        when(repository.findById(3L)).thenReturn(Optional.of(entidad(3L)));
        assertThrows(ReglaNegocioException.class, () -> service.calcularPrecioFinal(3L, "INVENTADA"));
    }
}
