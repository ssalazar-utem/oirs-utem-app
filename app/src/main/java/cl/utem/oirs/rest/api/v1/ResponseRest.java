package cl.utem.oirs.rest.api.v1;

import cl.utem.oirs.rest.api.vo.CurrentResponse;
import cl.utem.oirs.rest.api.vo.ResponseVO;
import cl.utem.oirs.rest.domain.enums.IcsoStatus;
import cl.utem.oirs.rest.domain.enums.UserRole;
import cl.utem.oirs.rest.domain.model.Ticket;
import cl.utem.oirs.rest.domain.model.User;
import cl.utem.oirs.rest.exception.AuthException;
import cl.utem.oirs.rest.exception.BadRoleException;
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
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.servlet.http.HttpServletRequest;
import java.io.Serializable;
import java.time.OffsetDateTime;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ProblemDetail;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@CrossOrigin
@RestController
@RequestMapping(value = "/v1/response",
        consumes = {MediaType.APPLICATION_JSON_VALUE},
        produces = {MediaType.APPLICATION_JSON_VALUE}
)
public class ResponseRest implements Serializable {

    private static final long serialVersionUID = 1;
    private final transient AuthManager authManager;
    private final transient TicketManager ticketManager;
    private static final Logger LOGGER = LoggerFactory.getLogger(ResponseRest.class);

    @Autowired
    public ResponseRest(AuthManager authManager, TicketManager ticketManager) {
        this.authManager = authManager;
        this.ticketManager = ticketManager;
    }

    @Operation(summary = "Actualiza un ticket de respuesta", description = "Este endpoint permite actualizar la respuesta y el estado de un ticket existente mediante el token del ticket.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "202", description = "Ticket actualizado exitosamente",
                content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = ResponseVO.class))),
        @ApiResponse(responseCode = "400", description = "Error de validación en los datos ingresados",
                content = @Content(schema = @Schema(implementation = ProblemDetail.class))),
        @ApiResponse(responseCode = "401", description = "Usuario no autorizado",
                content = @Content(schema = @Schema(implementation = ProblemDetail.class))),
        @ApiResponse(responseCode = "403", description = "Rol de usuario no permitido para realizar la operación",
                content = @Content(schema = @Schema(implementation = ProblemDetail.class))),
        @ApiResponse(responseCode = "404", description = "Ticket no encontrado",
                content = @Content(schema = @Schema(implementation = ProblemDetail.class)))
    })
    @PutMapping(value = "/{ticketToken}/ticket",
            consumes = {MediaType.APPLICATION_JSON_VALUE},
            produces = {MediaType.APPLICATION_JSON_VALUE}
    )
    public ResponseEntity<ResponseVO> responseTicket(HttpServletRequest request,
            @RequestHeader(name = "Authorization", required = true) String authorization,
            @PathVariable(name = "ticketToken") String ticketToken,
            @RequestBody CurrentResponse body) {

        final User user = authManager.authenticate(request, authorization);
        if (user == null) {
            throw new AuthException();
        }

        if (UserRole.STUDENT.equals(user.getRole())) {
            throw new BadRoleException(String.format("El usuario %s tiene rol de estudiante", user.getEmail()));
        }

        Ticket ticket = ticketManager.getTicket(ticketToken);
        if (ticket == null) {
            throw new NoDataException(String.format("No se ha encontrado el ticket con token %s", ticketToken));
        }

        final String response = StringUtils.trimToEmpty(body.getResponse());
        if (StringUtils.isBlank(response)) {
            final String errStr = String.format("No se puede actualizar el ticket %s porque no tiene una respuesta a la solicitud", ticket.getToken());
            LOGGER.error("{} : {} : {}", errStr, ticket.getMessage(), ticket.getResponse());
            throw new ValidationException(errStr);
        }
        ticket.setResponse(response);

        final IcsoStatus status = IcsoUtils.getStatus(body.getStatus());
        final boolean canChangeStatus = IcsoUtils.canChangeStatus(ticket.getStatus(), status);
        if (!canChangeStatus) {
            final String errStr = String.format("No se puede cambiar el estado de la solicitud ({} => {})", ticket.getToken(), ticket.getStatus(), status);
            LOGGER.error("{}", errStr);
            throw new ValidationException(errStr);
        }
        ticket.setStatus(status);
        ticket.setUpdated(OffsetDateTime.now());

        ResponseVO vo = new ResponseVO(ticketManager.response(user, ticket));
        return ResponseEntity.status(HttpStatus.ACCEPTED).body(vo);
    }
}
