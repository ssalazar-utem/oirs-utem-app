package cl.utem.oirs.rest.api.vo;

import cl.utem.oirs.rest.domain.model.Seba;
import com.fasterxml.jackson.annotation.JsonInclude;
import java.time.Instant;
import java.time.OffsetDateTime;

/**
 *
 * @author seba
 */
@JsonInclude(JsonInclude.Include.NON_EMPTY)
public class ErrorVO extends Seba {

    private final int code;
    private final String cause;
    private final Instant date;

    public ErrorVO() {
        this.code = 999;
        this.cause = "Error desconocido";
        this.date = Instant.now();
    }

    public ErrorVO(int code, String cause) {
        this.code = code;
        this.cause = cause;
        this.date = Instant.now();
    }

    public ErrorVO(int code, String cause, OffsetDateTime date) {
        this.code = code;
        this.cause = cause;
        this.date = date.toInstant();
    }

    public int getCode() {
        return code;
    }

    public String getCause() {
        return cause;
    }

    public Instant getDate() {
        return date;
    }
}
