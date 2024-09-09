package cl.utem.oirs.rest.api.v1;

import cl.utem.oirs.rest.api.vo.ResponseVO;
import cl.utem.oirs.rest.api.vo.TicketRequestVO;
import cl.utem.oirs.rest.api.vo.TicketResponseVO;
import cl.utem.oirs.rest.domain.enums.IcsoType;
import cl.utem.oirs.rest.domain.model.Category;
import cl.utem.oirs.rest.domain.model.Ticket;
import cl.utem.oirs.rest.domain.model.User;
import cl.utem.oirs.rest.exception.AuthException;
import cl.utem.oirs.rest.exception.NoDataException;
import cl.utem.oirs.rest.exception.ValidationException;
import cl.utem.oirs.rest.manager.AuthManager;
import cl.utem.oirs.rest.manager.TicketManager;
import cl.utem.oirs.rest.utils.IcsoUtils;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.servlet.http.HttpServletRequest;
import java.io.Serializable;
import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.List;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ProblemDetail;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@CrossOrigin
@RestController
@RequestMapping(value = "/v1/icso",
        consumes = {MediaType.APPLICATION_JSON_VALUE},
        produces = {MediaType.APPLICATION_JSON_VALUE}
)
public class IcsoRest implements Serializable {

    private static final long serialVersionUID = 1;
    private final transient AuthManager authManager;
    private final transient TicketManager ticketManager;
    private static final Logger LOGGER = LoggerFactory.getLogger(IcsoRest.class);

    @Autowired
    public IcsoRest(AuthManager authManager, TicketManager ticketManager) {
        this.authManager = authManager;
        this.ticketManager = ticketManager;
    }

    @Operation(summary = "Obtiene el ticket por token", description = "Retorna el ticket asociado al token proporcionado.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Ticket encontrado",
                content = @Content(schema = @Schema(implementation = TicketResponseVO.class))),
        @ApiResponse(responseCode = "404", description = "Ticket no encontrado",
                content = @Content(schema = @Schema(implementation = ProblemDetail.class))),
        @ApiResponse(responseCode = "401", description = "No autorizado",
                content = @Content(schema = @Schema(implementation = ProblemDetail.class)))
    })
    @GetMapping(value = "/{ticketToken}/ticket",
            consumes = {MediaType.ALL_VALUE},
            produces = {MediaType.APPLICATION_JSON_VALUE}
    )
    public ResponseEntity<TicketResponseVO> getTicket(HttpServletRequest request,
            @RequestHeader(name = "Authorization", required = true) String authorization,
            @PathVariable(name = "ticketToken") String ticketToken) {

        final User user = authManager.authenticate(request, authorization);
        if (user == null) {
            throw new AuthException();
        }

        Ticket ticket = ticketManager.getTicket(ticketToken);
        if (ticket == null) {
            throw new NoDataException(String.format("No se ha encontrado un ticket con el token", ticketToken));
        }

        List<String> attachments = ticketManager.getAttachmentsTokens(ticket);
        return ResponseEntity.ok(new TicketResponseVO(ticket, attachments));
    }

    @Operation(summary = "Obtiene todos los tickets de una categoría", description = "Retorna una lista de tickets asociados a la categoría especificada.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Tickets encontrados",
                content = @Content(schema = @Schema(implementation = TicketResponseVO.class))),
        @ApiResponse(responseCode = "404", description = "Categoría no encontrada o sin tickets",
                content = @Content(schema = @Schema(implementation = ProblemDetail.class))),
        @ApiResponse(responseCode = "401", description = "No autorizado",
                content = @Content(schema = @Schema(implementation = ProblemDetail.class)))
    })
    @GetMapping(value = "/{categoryToken}/tickets",
            consumes = {MediaType.ALL_VALUE},
            produces = {MediaType.APPLICATION_JSON_VALUE}
    )
    public ResponseEntity<List<TicketResponseVO>> getTickets(HttpServletRequest request,
            @RequestHeader(name = "Authorization", required = true) String authorization,
            @PathVariable(name = "categoryToken") String categoryToken) {

        final User user = authManager.authenticate(request, authorization);
        if (user == null) {
            throw new AuthException();
        }

        final Category category = ticketManager.getCategory(categoryToken);
        if (category == null) {
            throw new NoDataException(String.format("No se ha encontrado la categoría con token %s", categoryToken));
        }

        List<Ticket> tickets = ticketManager.getTickets(category);
        if (CollectionUtils.isEmpty(tickets)) {
            throw new NoDataException(String.format("No se ha encontrado tickets en la categoría %s", category.getName()));
        }

        List<TicketResponseVO> vos = new ArrayList<>();
        for (Ticket ticket : tickets) {
            List<String> attachments = ticketManager.getAttachmentsTokens(ticket);
            vos.add(new TicketResponseVO(ticket, attachments));
        }

        return ResponseEntity.ok(vos);
    }

    @Operation(summary = "Crea un nuevo ticket", description = "Permite la creación de un ticket en una categoría específica.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "201", description = "Ticket creado",
                content = @Content(schema = @Schema(implementation = ResponseVO.class))),
        @ApiResponse(responseCode = "400", description = "Error de validación",
                content = @Content(schema = @Schema(implementation = ProblemDetail.class))),
        @ApiResponse(responseCode = "401", description = "No autorizado",
                content = @Content(schema = @Schema(implementation = ProblemDetail.class))),
        @ApiResponse(responseCode = "404", description = "Categoría no encontrada",
                content = @Content(schema = @Schema(implementation = ProblemDetail.class)))
    })
    @PostMapping(value = "/{categoryToken}/ticket",
            consumes = {MediaType.APPLICATION_JSON_VALUE},
            produces = {MediaType.APPLICATION_JSON_VALUE}
    )
    public ResponseEntity<ResponseVO> createTicket(HttpServletRequest request,
            @RequestHeader(name = "Authorization", required = true) String authorization,
            @PathVariable(name = "categoryToken") String categoryToken,
            @RequestBody TicketRequestVO body) {

        final User user = authManager.authenticate(request, authorization);
        if (user == null) {
            throw new AuthException();
        }

        final Category category = ticketManager.getCategory(categoryToken);
        if (category == null) {
            throw new NoDataException(String.format("No se ha encontrado la categoría con token %s", categoryToken));
        }

        final IcsoType type = IcsoUtils.getType(body.getType());
        if (type == null) {
            throw new ValidationException("Se requiere un tipo de solicitud");
        }

        final String subject = StringUtils.trimToEmpty(body.getSubject());
        if (StringUtils.isBlank(subject)) {
            throw new ValidationException("La solicitud requiere un motivo");
        }

        final String message = StringUtils.trimToEmpty(body.getMessage());
        if (StringUtils.isBlank(message)) {
            throw new ValidationException("La solicitud requiere una descripción");
        }

        ResponseVO vo = new ResponseVO(ticketManager.create(category, user, type, subject, message));
        return ResponseEntity.status(HttpStatus.CREATED).body(vo);
    }

    @Operation(summary = "Actualiza un ticket existente", description = "Permite la actualización de un ticket ya creado.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "202", description = "Ticket actualizado",
                content = @Content(schema = @Schema(implementation = ResponseVO.class))),
        @ApiResponse(responseCode = "400", description = "Error de validación",
                content = @Content(schema = @Schema(implementation = ProblemDetail.class))),
        @ApiResponse(responseCode = "401", description = "No autorizado",
                content = @Content(schema = @Schema(implementation = ProblemDetail.class))),
        @ApiResponse(responseCode = "404", description = "Ticket o categoría no encontrados",
                content = @Content(schema = @Schema(implementation = ProblemDetail.class)))
    })
    @PutMapping(value = "/{ticketToken}/ticket",
            consumes = {MediaType.APPLICATION_JSON_VALUE},
            produces = {MediaType.APPLICATION_JSON_VALUE}
    )
    public ResponseEntity<ResponseVO> updateTicket(HttpServletRequest request,
            @RequestHeader(name = "Authorization", required = true) String authorization,
            @PathVariable(name = "ticketToken") String ticketToken,
            @RequestBody TicketRequestVO body) {

        final User user = authManager.authenticate(request, authorization);
        if (user == null) {
            throw new AuthException();
        }

        Ticket ticket = ticketManager.getTicket(ticketToken);
        if (ticket == null) {
            throw new NoDataException(String.format("No se ha encontrado el ticket con token %s", ticketToken));
        }

        if (StringUtils.isNotBlank(ticket.getResponse())) {
            final String errStr = String.format("No se puede actualizar el ticket %s porque ya tiene una respuesta, por favor ingrese una nueva solicitud", ticket.getToken());
            LOGGER.error("{} : {} : {}", errStr, ticket.getMessage(), ticket.getResponse());
            throw new ValidationException(errStr);
        }

        final IcsoType type = IcsoUtils.getType(body.getType());
        if (type != null) {
            throw new ValidationException("Se requiere un tipo de solicitud");
        }
        ticket.setType(type);

        final String subject = StringUtils.trimToEmpty(body.getSubject());
        if (StringUtils.isBlank(subject)) {
            throw new ValidationException("La solicitud requiere un motivo");
        }
        ticket.setSubject(subject);

        final String message = StringUtils.trimToEmpty(body.getMessage());
        if (StringUtils.isBlank(message)) {
            throw new ValidationException("La solicitud requiere una descripción");
        }
        ticket.setMessage(message);
        ticket.setUpdated(OffsetDateTime.now());

        ResponseVO vo = new ResponseVO(ticketManager.save(ticket));
        return ResponseEntity.status(HttpStatus.ACCEPTED).body(vo);
    }

    @Operation(summary = "Elimina un ticket", description = "Permite la eliminación de un ticket existente.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "204", description = "Ticket eliminado exitosamente"),
        @ApiResponse(responseCode = "404", description = "Ticket no encontrado",
                content = @Content(schema = @Schema(implementation = ProblemDetail.class))),
        @ApiResponse(responseCode = "401", description = "No autorizado",
                content = @Content(schema = @Schema(implementation = ProblemDetail.class)))
    })
    @DeleteMapping(value = "/{ticketToken}/ticket",
            consumes = {MediaType.ALL_VALUE},
            produces = {MediaType.APPLICATION_JSON_VALUE}
    )
    public ResponseEntity<Void> deleteTicket(HttpServletRequest request,
            @RequestHeader(name = "Authorization", required = true) String authorization,
            @PathVariable(name = "ticketToken") String ticketToken) {

        final User user = authManager.authenticate(request, authorization);
        if (user == null) {
            throw new AuthException();
        }

        final boolean deleted = ticketManager.deleteTicket(ticketToken);
        if (!deleted) {
            throw new NoDataException(String.format("No se ha encontrado un ticket con el token %s", ticketToken));
        }

        return ResponseEntity.noContent().build();  // Usar 204 No Content
    }
}
