package cl.utem.oirs.rest.api.vo;

import cl.utem.oirs.rest.domain.model.Seba;
import cl.utem.oirs.rest.domain.model.Ticket;
import io.swagger.v3.oas.annotations.media.Schema;
import com.fasterxml.jackson.annotation.JsonInclude;
import java.time.Instant;

@JsonInclude(JsonInclude.Include.NON_EMPTY)
@Schema(description = "Objeto de transferencia de datos para la respuesta de una solicitud.")
public class ResponseVO extends Seba {

    @Schema(description = "Indica si la operación fue exitosa", example = "true")
    private final boolean success;

    @Schema(description = "Token único que identifica la solicitud o ticket", example = "abc123")
    private final String token;

    @Schema(description = "Detalle del estado actual de la solicitud", example = "Su requerimiento está en estado APROBADO")
    private final String detail;

    @Schema(description = "Fecha y hora de la última actualización de la solicitud", example = "2023-09-09T12:45:30Z")
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
