package cl.utem.oirs.rest.exception;

public class NoDataException extends RuntimeException {

    public NoDataException() {
        super("No se ha encontrado información con los criterios de búsqueda seleccionados");
    }

    public NoDataException(String message) {
        super(message);
    }
}
