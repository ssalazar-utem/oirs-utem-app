package cl.utem.oirs.rest.api.vo;

import cl.utem.oirs.rest.domain.model.Seba;
import com.fasterxml.jackson.annotation.JsonInclude;

/**
 *
 * @author seba
 */
@JsonInclude(JsonInclude.Include.NON_EMPTY)
public class TicketRequestVO extends Seba {

    private String type = null;
    private String subject = null;
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
