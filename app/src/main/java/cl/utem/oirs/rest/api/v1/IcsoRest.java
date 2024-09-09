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

        return ResponseEntity.ok(new TicketResponseVO(ticket));
    }

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
            vos.add(new TicketResponseVO(ticket));
        }

        return ResponseEntity.ok(vos);
    }

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
        if (type != null) {
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

    @PutMapping(value = "/{categoryToken}/{ticketToken}/ticket",
            consumes = {MediaType.APPLICATION_JSON_VALUE},
            produces = {MediaType.APPLICATION_JSON_VALUE}
    )
    public ResponseEntity<ResponseVO> updateTicket(HttpServletRequest request,
            @RequestHeader(name = "Authorization", required = true) String authorization,
            @PathVariable(name = "categoryToken") String categoryToken,
            @PathVariable(name = "ticketToken") String ticketToken,
            @RequestBody TicketRequestVO body) {

        final User user = authManager.authenticate(request, authorization);
        if (user == null) {
            throw new AuthException();
        }

        final Category category = ticketManager.getCategory(categoryToken);
        if (category == null) {
            throw new NoDataException(String.format("No se ha encontrado la categoría con token %s", categoryToken));
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

    @DeleteMapping(value = "/{ticketToken}/ticket",
            consumes = {MediaType.ALL_VALUE},
            produces = {MediaType.APPLICATION_JSON_VALUE}
    )
    public ResponseEntity<ResponseVO> deleteTicket(HttpServletRequest request,
            @RequestHeader(name = "Authorization", required = true) String authorization,
            @PathVariable(name = "ticketToken") String ticketToken) {

        final User user = authManager.authenticate(request, authorization);
        if (user == null) {
            throw new AuthException();
        }

        final boolean deleted = ticketManager.deleteTicket(ticketToken);
        if (!deleted) {
            throw new NoDataException(String.format("No se ha encontrado un ticket con el token", ticketToken));
        }

        return ResponseEntity.ok(new ResponseVO(ticketToken));
    }
}
