package cl.utem.oirs.rest.api.vo;

import cl.utem.oirs.rest.domain.model.Ticket;
import io.swagger.v3.oas.annotations.media.Schema;
import com.fasterxml.jackson.annotation.JsonInclude;
import java.time.Instant;

@JsonInclude(JsonInclude.Include.NON_EMPTY)
@Schema(description = "Objeto de transferencia de datos que representa la respuesta de un ticket.")
public class TicketResponseVO extends TicketRequestVO {

    @Schema(description = "Token único que identifica el ticket", example = "abc123")
    private String token = null;

    @Schema(description = "Estado actual de la solicitud", example = "IN_PROGRESS", allowableValues = {"ERROR", "RECEIVED", "UNDER_REVIEW", "IN_PROGRESS", "PENDING_INFORMATION", "RESOLVED", "CLOSED", "REJECTED", "CANCELLED"})
    private String status = null;

    @Schema(description = "Respuesta a la solicitud asociada al ticket", example = "Su solicitud ha sido recibida y está siendo procesada.")
    private String response = null;

    @Schema(description = "Fecha y hora en la que se creó el ticket", example = "2024-09-09T12:45:30Z")
    private Instant created = null;

    @Schema(description = "Fecha y hora de la última actualización del ticket", example = "2024-09-10T12:45:30Z")
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
