package cl.utem.oirs.rest.api.vo;

import cl.utem.oirs.rest.domain.model.Seba;
import com.fasterxml.jackson.annotation.JsonInclude;

@JsonInclude(JsonInclude.Include.NON_EMPTY)
public class CurrentResponse extends Seba {

    private String status = null;
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
