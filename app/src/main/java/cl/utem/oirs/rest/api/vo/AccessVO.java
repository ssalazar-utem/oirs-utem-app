package cl.utem.oirs.rest.api.vo;

import cl.utem.oirs.rest.domain.model.Access;
import cl.utem.oirs.rest.domain.model.Seba;
import io.swagger.v3.oas.annotations.media.Schema;
import com.fasterxml.jackson.annotation.JsonInclude;
import java.time.Instant;

@JsonInclude(JsonInclude.Include.NON_EMPTY)
@Schema(description = "Objeto de transferencia de datos para información de acceso.")
public class AccessVO extends Seba {

    @Schema(description = "Dirección IP desde la cual se realizó el acceso", example = "192.168.1.1")
    private String ip = null;

    @Schema(description = "Cadena del User-Agent del cliente que realizó la solicitud", example = "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36")
    private String userAgent = null;

    @Schema(description = "URI solicitada en el acceso", example = "/api/v1/oirs")
    private String requestUri = null;

    @Schema(description = "Fecha y hora en la que se creó el acceso", example = "2023-08-09T12:45:30Z")
    private Instant created = null;

    public AccessVO() {
        this.ip = null;
        this.userAgent = null;
        this.requestUri = null;
        this.created = null;
    }

    public AccessVO(Access access) {
        this.ip = access.getIp();
        this.userAgent = access.getUserAgent();
        this.requestUri = access.getRequestUri();
        this.created = access.getCreated().toInstant();
    }

    public String getIp() {
        return ip;
    }

    public String getUserAgent() {
        return userAgent;
    }

    public String getRequestUri() {
        return requestUri;
    }

    public Instant getCreated() {
        return created;
    }
}
