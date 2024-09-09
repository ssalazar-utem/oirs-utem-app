package cl.utem.oirs.rest.api.vo;

import cl.utem.oirs.rest.domain.model.Seba;
import io.swagger.v3.oas.annotations.media.Schema;
import com.fasterxml.jackson.annotation.JsonInclude;

@JsonInclude(JsonInclude.Include.NON_EMPTY)
@Schema(description = "Objeto de transferencia de datos para la creación o actualización de un ticket.")
public class TicketRequestVO extends Seba {

    @Schema(description = "Tipo de ticket que se está solicitando", example = "INFORMATION", allowableValues = {"CLAIM", "SUGGESTION", "INFORMATION"})
    private String type = null;

    @Schema(description = "Asunto del ticket", example = "Problema con la plataforma")
    private String subject = null;

    @Schema(description = "Mensaje o descripción del ticket", example = "Tengo problemas para acceder a la plataforma desde mi cuenta.")
    private String message = null;

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
