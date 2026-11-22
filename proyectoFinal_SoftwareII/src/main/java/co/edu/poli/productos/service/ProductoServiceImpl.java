package co.edu.poli.productos.service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import co.edu.poli.productos.dto.PrecioFinalResponseDTO;
import co.edu.poli.productos.dto.ProductoRequestDTO;
import co.edu.poli.productos.dto.ProductoResponseDTO;
import co.edu.poli.productos.exception.ProductoNoEncontradoException;
import co.edu.poli.productos.exception.ReglaNegocioException;
import co.edu.poli.productos.model.Producto;
import co.edu.poli.productos.repository.ProductoRepository;
import co.edu.poli.productos.strategy.PrecioContext;
import co.edu.poli.productos.strategy.PrecioStrategy;

/**
 * Implementa la lógica de negocio de los productos de la farmacia.
 */
@Service
@Transactional(readOnly = true)
public class ProductoServiceImpl implements ProductoService {

    private final ProductoRepository productoRepository;
    private final PrecioContext precioContext;

    public ProductoServiceImpl(ProductoRepository productoRepository, PrecioContext precioContext) {
        this.productoRepository = productoRepository;
        this.precioContext = precioContext;
    }

    @Override
    public List<ProductoResponseDTO> listar() {
        return productoRepository.findAll().stream()
                .map(this::aRespuesta)
                .toList();
    }

    @Override
    public ProductoResponseDTO obtenerPorId(Long id) {
        return aRespuesta(buscar(id));
    }

    @Override
    @Transactional
    public ProductoResponseDTO crear(ProductoRequestDTO solicitud) {
        validar(solicitud);

        Producto producto = new Producto();
        copiarDatos(solicitud, producto);

        return aRespuesta(productoRepository.save(producto));
    }

    @Override
    @Transactional
    public ProductoResponseDTO actualizar(Long id, ProductoRequestDTO solicitud) {
        Producto producto = buscar(id);
        validar(solicitud);
        copiarDatos(solicitud, producto);

        return aRespuesta(productoRepository.save(producto));
    }

    @Override
    @Transactional
    public void eliminar(Long id) {
        if (!productoRepository.existsById(id)) {
            throw new ProductoNoEncontradoException(id);
        }
        productoRepository.deleteById(id);
    }

    @Override
    public PrecioFinalResponseDTO calcularPrecioFinal(Long id, String estrategia) {
        Producto producto = buscar(id);

        if (producto.estaVencido()) {
            throw new ReglaNegocioException(
                    "No se puede calcular el precio de venta de un producto vencido (id " + id + ")");
        }

        PrecioStrategy politica = precioContext.obtener(estrategia);
        BigDecimal precioFinal = politica.calcularPrecioFinal(producto);

        return new PrecioFinalResponseDTO(producto.getId(), producto.getNombre(), politica.getNombre(),
                politica.getDescripcion(), producto.getPrecio(), precioFinal);
    }

    private Producto buscar(Long id) {
        return productoRepository.findById(id)
                .orElseThrow(() -> new ProductoNoEncontradoException(id));
    }

    /**
     * Reglas de negocio: se verifican también aquí porque el servicio puede
     * ser invocado sin pasar por la validación de los DTO.
     */
    private void validar(ProductoRequestDTO solicitud) {
        if (solicitud.getPrecio() == null || solicitud.getPrecio().signum() < 0) {
            throw new ReglaNegocioException("El precio es obligatorio y no puede ser negativo");
        }
        if (solicitud.getStock() == null || solicitud.getStock() < 0) {
            throw new ReglaNegocioException("El stock es obligatorio y no puede ser negativo");
        }
        if (solicitud.getStockMinimo() != null && solicitud.getStockMinimo() < 0) {
            throw new ReglaNegocioException("El stock mínimo no puede ser negativo");
        }
        if (solicitud.getFechaVencimiento() != null
                && solicitud.getFechaVencimiento().isBefore(LocalDate.now())) {
            throw new ReglaNegocioException("La fecha de vencimiento no puede ser anterior a la fecha actual");
        }
    }

    /**
     * Copia los datos del DTO a la entidad. Los campos opcionales con valor
     * predeterminado ({@code stockMinimo}, {@code requiereFormula}, {@code activo})
     * conservan el valor actual de la entidad cuando no se envían.
     */
    private void copiarDatos(ProductoRequestDTO origen, Producto destino) {
        destino.setNombre(origen.getNombre().trim());
        destino.setPrincipioActivo(origen.getPrincipioActivo());
        destino.setPresentacion(origen.getPresentacion());
        destino.setLote(origen.getLote());
        destino.setFechaVencimiento(origen.getFechaVencimiento());
        destino.setPrecio(origen.getPrecio());
        destino.setStock(origen.getStock());
        destino.setCategoria(origen.getCategoria());

        if (origen.getStockMinimo() != null) {
            destino.setStockMinimo(origen.getStockMinimo());
        }
        if (origen.getRequiereFormula() != null) {
            destino.setRequiereFormula(origen.getRequiereFormula());
        }
        if (origen.getActivo() != null) {
            destino.setActivo(origen.getActivo());
        }
    }

    private ProductoResponseDTO aRespuesta(Producto producto) {
        ProductoResponseDTO dto = new ProductoResponseDTO();
        dto.setId(producto.getId());
        dto.setNombre(producto.getNombre());
        dto.setPrincipioActivo(producto.getPrincipioActivo());
        dto.setPresentacion(producto.getPresentacion());
        dto.setLote(producto.getLote());
        dto.setFechaVencimiento(producto.getFechaVencimiento());
        dto.setPrecio(producto.getPrecio());
        dto.setStock(producto.getStock());
        dto.setStockMinimo(producto.getStockMinimo());
        dto.setRequiereFormula(producto.getRequiereFormula());
        dto.setCategoria(producto.getCategoria());
        dto.setActivo(producto.getActivo());
        dto.setFechaCreacion(producto.getFechaCreacion());
        dto.setVencido(producto.estaVencido());
        dto.setPorVencer(producto.estaPorVencer());
        dto.setStockBajo(producto.tieneStockBajo());
        return dto;
    }
}
