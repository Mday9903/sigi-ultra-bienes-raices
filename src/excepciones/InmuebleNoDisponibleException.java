package excepciones;

public class InmuebleNoDisponibleException extends Exception {
    public InmuebleNoDisponibleException(String mensaje) {
        super(mensaje);
    }
}