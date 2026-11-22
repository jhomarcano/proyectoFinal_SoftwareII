package co.edu.poli.productos.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.math.BigDecimal;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import co.edu.poli.productos.dto.PrecioFinalResponseDTO;
import co.edu.poli.productos.dto.ProductoResponseDTO;
import co.edu.poli.productos.exception.ProductoNoEncontradoException;
import co.edu.poli.productos.exception.ReglaNegocioException;
import co.edu.poli.productos.service.ProductoService;

@WebMvcTest(ProductoController.class)
class ProductoControllerTest {

    @Autowired
    private MockMvc mvc;

    @MockitoBean
    private ProductoService service;

    private static final String JSON_VALIDO = """
            {"nombre":"Ibuprofeno","precio":12000,"stock":30,"categoria":"MEDICAMENTO"}
            """;

    @Test
    void crearDevuelve201ConLocation() throws Exception {
        ProductoResponseDTO creado = new ProductoResponseDTO();
        creado.setId(8L);
        creado.setNombre("Ibuprofeno");
        when(service.crear(any())).thenReturn(creado);

        mvc.perform(post("/api/productos").contentType(MediaType.APPLICATION_JSON).content(JSON_VALIDO))
                .andExpect(status().isCreated())
                .andExpect(header().string("Location", "/api/productos/8"))
                .andExpect(jsonPath("$.id").value(8));
    }

    @Test
    void crearConDatosInvalidosDevuelve400ConDetalles() throws Exception {
        String invalido = """
                {"nombre":"","precio":-5,"stock":-1}
                """;

        mvc.perform(post("/api/productos").contentType(MediaType.APPLICATION_JSON).content(invalido))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.detalles").isNotEmpty())
                .andExpect(jsonPath("$.status").value(400));
    }

    @Test
    void crearConCategoriaInexistenteDevuelve400() throws Exception {
        String invalido = """
                {"nombre":"X","precio":1,"stock":1,"categoria":"JUGUETES"}
                """;

        mvc.perform(post("/api/productos").contentType(MediaType.APPLICATION_JSON).content(invalido))
                .andExpect(status().isBadRequest());
    }

    @Test
    void obtenerInexistenteDevuelve404() throws Exception {
        when(service.obtenerPorId(99L)).thenThrow(new ProductoNoEncontradoException(99L));

        mvc.perform(get("/api/productos/99"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.mensaje").value("No existe un producto con id 99"));
    }

    @Test
    void idNoNumericoDevuelve400() throws Exception {
        mvc.perform(get("/api/productos/abc")).andExpect(status().isBadRequest());
    }

    @Test
    void eliminarDevuelve204() throws Exception {
        mvc.perform(delete("/api/productos/4")).andExpect(status().isNoContent());
    }

    @Test
    void precioFinalDevuelveCalculo() throws Exception {
        when(service.calcularPrecioFinal(eq(2L), eq("CATEGORIA"))).thenReturn(new PrecioFinalResponseDTO(
                2L, "Vitamina C", "CATEGORIA", "desc", new BigDecimal("10000.00"), new BigDecimal("9000.00")));

        mvc.perform(get("/api/productos/2/precio-final").param("estrategia", "CATEGORIA"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.precioFinal").value(9000.00))
                .andExpect(jsonPath("$.descuento").value(1000.00));
    }

    @Test
    void precioFinalConEstrategiaInvalidaDevuelve400() throws Exception {
        when(service.calcularPrecioFinal(eq(2L), eq("X"))).thenThrow(new ReglaNegocioException("no válida"));

        mvc.perform(get("/api/productos/2/precio-final").param("estrategia", "X"))
                .andExpect(status().isBadRequest());
    }
}
