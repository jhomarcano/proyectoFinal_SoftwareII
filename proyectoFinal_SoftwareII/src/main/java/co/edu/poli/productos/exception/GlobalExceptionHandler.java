package co.edu.poli.productos.exception;

import java.time.LocalDateTime;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.ErrorResponse;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

/**
 * Traduce las excepciones de la aplicación a respuestas HTTP con un cuerpo
 * uniforme ({@link ErrorRespuesta}).
 */
@RestControllerAdvice
public class GlobalExceptionHandler {

    private static final Logger log = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    /**
     * Estructura común de las respuestas de error.
     */
    public record ErrorRespuesta(LocalDateTime timestamp, int status, String error,
            String mensaje, List<String> detalles) {
    }

    @ExceptionHandler(ProductoNoEncontradoException.class)
    public ResponseEntity<ErrorRespuesta> manejarNoEncontrado(ProductoNoEncontradoException ex) {
        return construir(HttpStatus.NOT_FOUND, ex.getMessage(), List.of());
    }

    @ExceptionHandler(ReglaNegocioException.class)
    public ResponseEntity<ErrorRespuesta> manejarReglaNegocio(ReglaNegocioException ex) {
        return construir(HttpStatus.BAD_REQUEST, ex.getMessage(), List.of());
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorRespuesta> manejarValidacion(MethodArgumentNotValidException ex) {
        List<String> detalles = ex.getBindingResult().getFieldErrors().stream()
                .map(error -> error.getField() + ": " + error.getDefaultMessage())
                .sorted()
                .toList();

        return construir(HttpStatus.BAD_REQUEST, "Los datos enviados no son válidos", detalles);
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<ErrorRespuesta> manejarCuerpoIlegible(HttpMessageNotReadableException ex) {
        return construir(HttpStatus.BAD_REQUEST,
                "El cuerpo de la solicitud es ilegible o tiene un valor con formato inválido "
                        + "(fechas: yyyy-MM-dd; categoría: MEDICAMENTO, CUIDADO_PERSONAL, SUPLEMENTO, "
                        + "DISPOSITIVO_MEDICO, BEBIDA u OTROS)",
                List.of());
    }

    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public ResponseEntity<ErrorRespuesta> manejarTipoArgumento(MethodArgumentTypeMismatchException ex) {
        return construir(HttpStatus.BAD_REQUEST,
                "El parámetro '" + ex.getName() + "' tiene un valor inválido: " + ex.getValue(), List.of());
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorRespuesta> manejarInesperado(Exception ex) {
        // Errores HTTP propios de Spring (405, 415, 404 de ruta...) conservan su estado
        if (ex instanceof ErrorResponse errorSpring) {
            return construir(errorSpring.getStatusCode(), ex.getMessage(), List.of());
        }
        log.error("Error inesperado", ex);
        return construir(HttpStatus.INTERNAL_SERVER_ERROR, "Ocurrió un error interno en el servidor", List.of());
    }

    private ResponseEntity<ErrorRespuesta> construir(HttpStatusCode estado, String mensaje, List<String> detalles) {
        HttpStatus conocido = HttpStatus.resolve(estado.value());
        ErrorRespuesta cuerpo = new ErrorRespuesta(LocalDateTime.now(), estado.value(),
                conocido != null ? conocido.getReasonPhrase() : "Error", mensaje, detalles);
        return ResponseEntity.status(estado).body(cuerpo);
    }
}
