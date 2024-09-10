package cl.utem.oirs.rest.api.vo;

import cl.utem.oirs.rest.domain.model.Seba;
import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.v3.oas.annotations.media.Schema;

/**
 * DataVO es una clase que extiende de Seba y representa un objeto de datos con
 * atributos como nombre, tipo MIME y el contenido de los datos en formato de
 * cadena.
 */
@JsonInclude(JsonInclude.Include.NON_EMPTY)
@Schema(description = "Objeto de datos que contiene información de nombre, tipo MIME y datos.")
public class DataVO extends Seba {

    @Schema(description = "El nombre del archivo o dato.", example = "archivo.pdf")
    private String name = null;

    @Schema(description = "El tipo MIME del archivo.", example = "application/pdf")
    private String mime = null;

    @Schema(description = "Los datos en formato Base64.", example = "dGVzdCBkYXRhCg==")
    private String data = null;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getMime() {
        return mime;
    }

    public void setMime(String mime) {
        this.mime = mime;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }
}
