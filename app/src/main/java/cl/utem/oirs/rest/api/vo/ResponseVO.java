package cl.utem.oirs.rest.api.vo;

import cl.utem.oirs.rest.domain.model.Seba;
import cl.utem.oirs.rest.domain.model.Ticket;
import com.fasterxml.jackson.annotation.JsonInclude;
import java.time.Instant;

/**
 *
 * @author seba
 */
@JsonInclude(JsonInclude.Include.NON_EMPTY)
public class ResponseVO extends Seba {

    private final boolean success;
    private final String token;
    private final String detail;
    private final Instant date;

    public ResponseVO() {
        this.success = false;
        this.token = null;
        this.detail = null;
        this.date = Instant.now();
    }

    public ResponseVO(Ticket ticket) {
        this.success = true;
        this.token = ticket.getToken();
        this.detail = String.format("Su requerimiento está en estado %s", ticket.getStatus().getLabel());
        this.date = ticket.getUpdated().toInstant();
    }

    public boolean isSuccess() {
        return success;
    }

    public String getToken() {
        return token;
    }

    public String getDetail() {
        return detail;
    }

    public Instant getDate() {
        return date;
    }
}
