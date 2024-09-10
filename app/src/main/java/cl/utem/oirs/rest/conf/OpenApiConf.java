package cl.utem.oirs.rest.conf;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenApiConf {

    @Bean
    public OpenAPI customizeOpenAPI() {
        final String securitySchemeName = "bearerAuth";

        return new OpenAPI()
                .addSecurityItem(new SecurityRequirement().addList(securitySchemeName))
                .components(
                        new Components()
                                .addSecuritySchemes(securitySchemeName,
                                        new SecurityScheme()
                                                .name(securitySchemeName)
                                                .type(SecurityScheme.Type.HTTP)
                                                .scheme("bearer")
                                                .bearerFormat("JWT")
                                )
                )
                .info(new Info()
                        .title("API para sistema de información, reclamos y sugerencias")
                        .description("Aplicación de ejemplo para explicar el funcionamiento de servicios REST")
                        .version("0.9.9")
                        .contact(new Contact().email("sebasalazar@gmail.com").name("Sebastián Salazar Molina").url("https://sebastian.cl"))
                        .license(new License().name("CC BY-NC-ND 4.0 DEED").url("https://creativecommons.org/licenses/by-nc-nd/4.0/"))
                );
    }
}
