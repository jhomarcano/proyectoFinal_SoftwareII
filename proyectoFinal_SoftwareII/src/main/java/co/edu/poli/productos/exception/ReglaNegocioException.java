package co.edu.poli.productos.exception;

/**
 * Se lanza cuando una operación viola una regla del negocio
 * (valores negativos, fecha de vencimiento pasada, estrategia inexistente...).
 * Se traduce a HTTP 400.
 */
public class ReglaNegocioException extends RuntimeException {

    public ReglaNegocioException(String mensaje) {
        super(mensaje);
    }
}
