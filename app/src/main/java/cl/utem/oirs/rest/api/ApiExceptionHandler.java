package cl.utem.oirs.rest.api;

import cl.utem.oirs.rest.api.vo.ErrorVO;
import cl.utem.oirs.rest.exception.AuthException;
import cl.utem.oirs.rest.exception.NoDataException;
import cl.utem.oirs.rest.exception.ValidationException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class ApiExceptionHandler {

    private static final Logger LOGGER = LoggerFactory.getLogger(ApiExceptionHandler.class);

    @ExceptionHandler({AuthException.class})
    public ResponseEntity<ErrorVO> handleException(AuthException e) {
        LOGGER.error("Error de autenticación: {}", e.getLocalizedMessage());
        LOGGER.debug("Error de autenticación: {}", e.getMessage(), e);

        return new ResponseEntity<>(new ErrorVO(-999, e.getLocalizedMessage()), HttpStatus.UNAUTHORIZED);
    }

    @ExceptionHandler({NoDataException.class})
    public ResponseEntity<ErrorVO> handleException(NoDataException e) {
        LOGGER.error("Sin datos: {}", e.getLocalizedMessage());
        LOGGER.debug("Sin datos: {}", e.getMessage(), e);

        return new ResponseEntity<>(new ErrorVO(-999, e.getLocalizedMessage()), HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler({ValidationException.class})
    public ResponseEntity<ErrorVO> handleException(ValidationException e) {
        LOGGER.error("Error de Validación: {}", e.getLocalizedMessage());
        LOGGER.debug("Error de Validación: {}", e.getMessage(), e);

        return new ResponseEntity<>(new ErrorVO(-999, e.getLocalizedMessage()), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler({Exception.class})
    public ResponseEntity<ErrorVO> handleException(Exception e) {
        LOGGER.warn("Error NO manejado: {}", e.getLocalizedMessage());
        LOGGER.error("Error NO manejado: {}", e.getMessage(), e);

        return new ResponseEntity<>(new ErrorVO(-999, e.getLocalizedMessage()), HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
