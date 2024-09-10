package cl.utem.oirs.rest.exception;

public class ValidationException extends RuntimeException {

    public ValidationException() {
        super("Error de validación");
    }

    public ValidationException(String message) {
        super(message);
    }

    public ValidationException(String message, Throwable cause) {
        super(message, cause);
    }
}
