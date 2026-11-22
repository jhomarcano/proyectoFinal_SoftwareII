package co.edu.poli.productos.service;

import java.util.List;

import co.edu.poli.productos.dto.PrecioFinalResponseDTO;
import co.edu.poli.productos.dto.ProductoRequestDTO;
import co.edu.poli.productos.dto.ProductoResponseDTO;

/**
 * Contrato de las operaciones de negocio sobre los productos.
 */
public interface ProductoService {

    /** Lista todos los productos. */
    List<ProductoResponseDTO> listar();

    /**
     * Busca un producto por su identificador.
     *
     * @throws co.edu.poli.productos.exception.ProductoNoEncontradoException si no existe
     */
    ProductoResponseDTO obtenerPorId(Long id);

    /**
     * Crea un producto aplicando las reglas de validación y los valores predeterminados.
     *
     * @throws co.edu.poli.productos.exception.ReglaNegocioException si viola una regla de negocio
     */
    ProductoResponseDTO crear(ProductoRequestDTO solicitud);

    /**
     * Actualiza un producto existente.
     *
     * @throws co.edu.poli.productos.exception.ProductoNoEncontradoException si no existe
     * @throws co.edu.poli.productos.exception.ReglaNegocioException si viola una regla de negocio
     */
    ProductoResponseDTO actualizar(Long id, ProductoRequestDTO solicitud);

    /**
     * Elimina un producto.
     *
     * @throws co.edu.poli.productos.exception.ProductoNoEncontradoException si no existe
     */
    void eliminar(Long id);

    /**
     * Calcula el precio final de un producto con la estrategia de precio indicada
     * (patrón Strategy). Si {@code estrategia} es nula se usa {@code SIN_DESCUENTO}.
     *
     * @throws co.edu.poli.productos.exception.ProductoNoEncontradoException si no existe
     * @throws co.edu.poli.productos.exception.ReglaNegocioException si la estrategia no existe
     *         o el producto está vencido
     */
    PrecioFinalResponseDTO calcularPrecioFinal(Long id, String estrategia);
}
