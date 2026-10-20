package co.edu.poli.productos.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import co.edu.poli.productos.model.Producto;

/**
 * Repositorio para el acceso a los datos de los productos.
 */
public interface ProductoRepository extends JpaRepository<Producto, Long> {

}