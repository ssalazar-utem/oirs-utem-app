package cl.utem.oirs.rest.api.vo;

import cl.utem.oirs.rest.domain.model.Seba;
import io.swagger.v3.oas.annotations.media.Schema;
import com.fasterxml.jackson.annotation.JsonInclude;

@JsonInclude(JsonInclude.Include.NON_EMPTY)
@Schema(description = "Objeto de transferencia de datos para la respuesta de una solicitud.")
public class CurrentResponse extends Seba {

    @Schema(description = "Estado actual de la solicitud", example = "IN_PROGRESS", allowableValues = {"ERROR", "RECEIVED", "UNDER_REVIEW", "IN_PROGRESS", "PENDING_INFORMATION", "RESOLVED", "CLOSED", "REJECTED", "CANCELLED"})
    private String status = null;

    @Schema(description = "Respuesta proporcionada por el usuario o el sistema", example = "La solicitud está siendo revisado, se le informará cualquier novedad.")
    private String response = null;

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
}
