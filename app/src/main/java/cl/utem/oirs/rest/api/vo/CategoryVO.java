package cl.utem.oirs.rest.api.vo;

import cl.utem.oirs.rest.domain.model.Category;
import cl.utem.oirs.rest.domain.model.Seba;
import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.v3.oas.annotations.media.Schema;

@JsonInclude(JsonInclude.Include.NON_EMPTY)
@Schema(description = "Objeto de transferencia de datos para la categoría.")
public class CategoryVO extends Seba {

    @Schema(description = "Token único que identifica la categoría", example = "abc123")
    private final String token;

    @Schema(description = "Nombre de la categoría", example = "Sugerencias")
    private final String name;

    @Schema(description = "Descripción detallada de la categoría", example = "Categoría destinada a sugerencias de los usuarios")
    private final String description;

    public CategoryVO() {
        this.token = null;
        this.name = null;
        this.description = null;
    }

    public CategoryVO(Category category) {
        this.token = category.getToken();
        this.name = category.getName();
        this.description = category.getDescription();
    }

    public String getToken() {
        return token;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }
}
