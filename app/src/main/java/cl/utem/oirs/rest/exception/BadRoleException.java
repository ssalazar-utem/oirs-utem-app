package cl.utem.oirs.rest.exception;

public class BadRoleException extends RuntimeException {

    public BadRoleException() {
        super("Las credenciales provistas no permiten acceder a este recurso");
    }

    public BadRoleException(String message) {
        super(message);
    }

}
