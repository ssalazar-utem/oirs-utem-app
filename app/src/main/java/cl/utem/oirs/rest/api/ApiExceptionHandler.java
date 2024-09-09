package cl.utem.oirs.rest.api;

import cl.utem.oirs.rest.exception.AuthException;
import cl.utem.oirs.rest.exception.BadRoleException;
import cl.utem.oirs.rest.exception.NoDataException;
import cl.utem.oirs.rest.exception.ValidationException;
import jakarta.servlet.http.HttpServletRequest;
import java.net.URI;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class ApiExceptionHandler {

    private static final Logger LOGGER = LoggerFactory.getLogger(ApiExceptionHandler.class);

    private ProblemDetail makeProblemDetail(HttpServletRequest request, HttpStatus status, Exception e) {
        final URI type = URI.create("https://developer.mozilla.org/en-US/docs/Web/HTTP/Status/" + status.value());
        final String detail = StringUtils.trimToEmpty(e.getLocalizedMessage());

        ProblemDetail pd = ProblemDetail.forStatusAndDetail(status, detail);
        pd.setType(type);
        pd.setTitle(status.getReasonPhrase());

        if (request != null) {
            final URI instance = URI.create(request.getRequestURI());
            pd.setInstance(instance);
        }
        return pd;
    }

    @ExceptionHandler({AuthException.class})
    public ResponseEntity<ProblemDetail> handleException(HttpServletRequest request, AuthException e) {
        LOGGER.error("Error de autenticación: {}", e.getLocalizedMessage());
        LOGGER.debug("Error de autenticación: {}", e.getMessage(), e);

        ProblemDetail body = makeProblemDetail(request, HttpStatus.UNAUTHORIZED, e);
        return new ResponseEntity<>(body, HttpStatus.UNAUTHORIZED);
    }

    @ExceptionHandler({BadRoleException.class})
    public ResponseEntity<ProblemDetail> handleException(HttpServletRequest request, BadRoleException e) {
        LOGGER.error("Error de permisos: {}", e.getLocalizedMessage());
        LOGGER.debug("Error de permisos: {}", e.getMessage(), e);

        ProblemDetail body = makeProblemDetail(request, HttpStatus.FORBIDDEN, e);
        return new ResponseEntity<>(body, HttpStatus.FORBIDDEN);
    }

    @ExceptionHandler({NoDataException.class})
    public ResponseEntity<ProblemDetail> handleException(HttpServletRequest request, NoDataException e) {
        LOGGER.error("Sin datos: {}", e.getLocalizedMessage());
        LOGGER.debug("Sin datos: {}", e.getMessage(), e);

        ProblemDetail body = makeProblemDetail(request, HttpStatus.NOT_FOUND, e);
        return new ResponseEntity<>(body, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler({ValidationException.class})
    public ResponseEntity<ProblemDetail> handleException(HttpServletRequest request, ValidationException e) {
        LOGGER.error("Error de Validación: {}", e.getLocalizedMessage());
        LOGGER.debug("Error de Validación: {}", e.getMessage(), e);

        ProblemDetail body = makeProblemDetail(request, HttpStatus.BAD_REQUEST, e);
        return new ResponseEntity<>(body, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler({Exception.class})
    public ResponseEntity<ProblemDetail> handleException(HttpServletRequest request, Exception e) {
        LOGGER.warn("Error NO manejado: {}", e.getLocalizedMessage());
        LOGGER.error("Error NO manejado: {}", e.getMessage(), e);

        ProblemDetail body = makeProblemDetail(request, HttpStatus.INTERNAL_SERVER_ERROR, e);
        return new ResponseEntity<>(body, HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
