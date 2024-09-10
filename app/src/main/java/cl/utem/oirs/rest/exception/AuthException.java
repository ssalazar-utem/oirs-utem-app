package cl.utem.oirs.rest.exception;

/**
 *
 * @author seba
 */
public class AuthException extends RuntimeException {

    public AuthException() {
        super("Credenciales inválidas");
    }

    public AuthException(String message) {
        super(message);
    }

    public AuthException(String message, Throwable cause) {
        super(message, cause);
    }
}
