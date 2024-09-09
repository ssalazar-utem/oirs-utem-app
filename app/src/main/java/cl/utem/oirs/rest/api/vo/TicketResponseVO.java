package cl.utem.oirs.rest.api.vo;

import cl.utem.oirs.rest.domain.model.Ticket;
import com.fasterxml.jackson.annotation.JsonInclude;
import java.time.Instant;

@JsonInclude(JsonInclude.Include.NON_EMPTY)
public class TicketResponseVO extends TicketRequestVO {

    private String token = null;
    private String status = null;
    private String response = null;
    private Instant created = null;
    private Instant updated = null;

    public TicketResponseVO(Ticket ticket) {
        super.setType(ticket.getType().name());
        super.setSubject(ticket.getSubject());
        super.setMessage(ticket.getMessage());
        this.token = ticket.getToken();
        this.status = ticket.getStatus().name();
        this.response = ticket.getResponse();
        this.created = ticket.getCreated().toInstant();
        this.updated = ticket.getUpdated().toInstant();
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getResponse() {
        return response;
    }

    public void setResponse(String response) {
        this.response = response;
    }

    public Instant getCreated() {
        return created;
    }

    public void setCreated(Instant created) {
        this.created = created;
    }

    public Instant getUpdated() {
        return updated;
    }

    public void setUpdated(Instant updated) {
        this.updated = updated;
    }
}
