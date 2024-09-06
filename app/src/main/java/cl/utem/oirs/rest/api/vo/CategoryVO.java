package cl.utem.oirs.rest.api.vo;

import cl.utem.oirs.rest.domain.model.Category;
import cl.utem.oirs.rest.domain.model.Seba;
import com.fasterxml.jackson.annotation.JsonInclude;

@JsonInclude(JsonInclude.Include.NON_EMPTY)
public class CategoryVO extends Seba {

    private final String token;
    private final String name;
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
