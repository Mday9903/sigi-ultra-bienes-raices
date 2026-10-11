package excepciones;

public class ReservaNoVigenteException extends Exception {
    public ReservaNoVigenteException(String mensaje) {
        super(mensaje);
    }
}