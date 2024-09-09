package cl.utem.oirs.rest.api.v1;

import cl.utem.oirs.rest.api.vo.CategoryVO;
import cl.utem.oirs.rest.domain.enums.IcsoStatus;
import cl.utem.oirs.rest.domain.enums.IcsoType;
import cl.utem.oirs.rest.domain.model.Category;
import cl.utem.oirs.rest.domain.model.User;
import cl.utem.oirs.rest.exception.AuthException;
import cl.utem.oirs.rest.exception.NoDataException;
import cl.utem.oirs.rest.manager.AuthManager;
import cl.utem.oirs.rest.manager.TicketManager;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.servlet.http.HttpServletRequest;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import org.apache.commons.collections.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ProblemDetail;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@CrossOrigin
@RestController
@RequestMapping(value = "/v1/info",
        consumes = {MediaType.APPLICATION_JSON_VALUE},
        produces = {MediaType.APPLICATION_JSON_VALUE}
)
public class InfoRest implements Serializable {

    private static final long serialVersionUID = 1;
    private final transient AuthManager authManager;
    private final transient TicketManager ticketManager;
    private static final Logger LOGGER = LoggerFactory.getLogger(InfoRest.class);

    @Autowired
    public InfoRest(AuthManager authManager, TicketManager ticketManager) {
        this.authManager = authManager;
        this.ticketManager = ticketManager;
    }

    @Operation(summary = "Obtener todas las categorías", description = "Retorna una lista de todas las categorías disponibles.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Lista de categorías obtenida con éxito",
                content = {
                    @Content(mediaType = "application/json",
                            schema = @Schema(implementation = CategoryVO.class))}),
        @ApiResponse(responseCode = "401", description = "No autorizado, el token es inválido o falta autenticación",
                content = @Content(schema = @Schema(implementation = ProblemDetail.class))),
        @ApiResponse(responseCode = "404", description = "No se encontraron categorías disponibles",
                content = @Content(schema = @Schema(implementation = ProblemDetail.class)))
    })
    @GetMapping(value = "/categories",
            consumes = {MediaType.ALL_VALUE},
            produces = {MediaType.APPLICATION_JSON_VALUE}
    )
    public ResponseEntity<List<CategoryVO>> allCategories(HttpServletRequest request,
            @RequestHeader(name = "Authorization", required = true) String authorization) {
        final User user = authManager.authenticate(request, authorization);
        if (user == null) {
            throw new AuthException();
        }

        List<Category> categories = ticketManager.getCatogories();
        if (CollectionUtils.isEmpty(categories)) {
            throw new NoDataException();
        }

        List<CategoryVO> list = new ArrayList<>();
        for (Category category : categories) {
            list.add(new CategoryVO(category));
        }
        LOGGER.debug("Respondiendo {} categorías", list.size());

        return ResponseEntity.ok(list);
    }

    @Operation(summary = "Obtener todas los tipos de requerimientos", description = "Retorna una lista de todos los tipos de requerimientos disponibles.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Lista de tipos de requerimientos obtenida con éxito",
                content = {
                    @Content(mediaType = "application/json",
                            schema = @Schema(implementation = IcsoType.class))}),
        @ApiResponse(responseCode = "401", description = "No autorizado, el token es inválido o falta autenticación",
                content = @Content(schema = @Schema(implementation = ProblemDetail.class))),
        @ApiResponse(responseCode = "404", description = "No se encontraron tipos disponibles",
                content = @Content(schema = @Schema(implementation = ProblemDetail.class)))
    })
    @GetMapping(value = "/types",
            consumes = {MediaType.ALL_VALUE},
            produces = {MediaType.APPLICATION_JSON_VALUE}
    )
    public ResponseEntity<List<IcsoType>> allTypes(HttpServletRequest request,
            @RequestHeader(name = "Authorization", required = true) String authorization) {
        final User user = authManager.authenticate(request, authorization);
        if (user == null) {
            throw new AuthException();
        }

        List<IcsoType> list = Arrays.asList(IcsoType.values());
        if (CollectionUtils.isEmpty(list)) {
            throw new NoDataException();
        }
        LOGGER.debug("Respondiendo {} tipos de solicitudes", list.size());

        return ResponseEntity.ok(list);
    }

    @Operation(summary = "Obtener todas los estados de requerimientos", description = "Retorna una lista de todos los estados de requerimientos disponibles.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Lista de estados de requerimientos obtenida con éxito",
                content = {
                    @Content(mediaType = "application/json",
                            schema = @Schema(implementation = IcsoStatus.class))}),
        @ApiResponse(responseCode = "401", description = "No autorizado, el token es inválido o falta autenticación",
                content = @Content(schema = @Schema(implementation = ProblemDetail.class))),
        @ApiResponse(responseCode = "404", description = "No se encontraron estados disponibles",
                content = @Content(schema = @Schema(implementation = ProblemDetail.class)))
    })
    @GetMapping(value = "/status",
            consumes = {MediaType.ALL_VALUE},
            produces = {MediaType.APPLICATION_JSON_VALUE}
    )
    public ResponseEntity<List<IcsoStatus>> allStatus(HttpServletRequest request,
            @RequestHeader(name = "Authorization", required = true) String authorization) {
        final User user = authManager.authenticate(request, authorization);
        if (user == null) {
            throw new AuthException();
        }

        List<IcsoStatus> list = Arrays.asList(IcsoStatus.values());
        if (CollectionUtils.isEmpty(list)) {
            throw new NoDataException();
        }
        LOGGER.debug("Respondiendo {} tipos de solicitudes", list.size());

        return ResponseEntity.ok(list);
    }
}
