package cl.utem.oirs.rest.api.v1;

import cl.utem.oirs.rest.api.vo.ResponseVO;
import cl.utem.oirs.rest.api.vo.TicketRequestVO;
import cl.utem.oirs.rest.domain.model.Category;
import cl.utem.oirs.rest.domain.model.User;
import cl.utem.oirs.rest.exception.AuthException;
import cl.utem.oirs.rest.exception.ValidationException;
import cl.utem.oirs.rest.manager.AuthManager;
import cl.utem.oirs.rest.manager.TicketManager;
import cl.utem.oirs.rest.utils.IpUtils;
import jakarta.servlet.http.HttpServletRequest;
import java.io.Serializable;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
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

    @PostMapping(value = "/{categoryToken}/ticket",
            consumes = {MediaType.APPLICATION_JSON_VALUE},
            produces = {MediaType.APPLICATION_JSON_VALUE}
    )
    public ResponseEntity<ResponseVO> ticket(HttpServletRequest request,
            @RequestHeader(name = "Authorization", required = true) String authorization,
            @PathVariable(name = "categoryToken") String categoryToken,
            @RequestBody TicketRequestVO body) {
        final String ip = IpUtils.getClientIp(request);
        final String userAgent = StringUtils.trimToEmpty(request.getHeader("User-Agent"));
        final User user = authManager.authenticate(authorization, ip, userAgent);
        if (user == null) {
            throw new AuthException();
        }

        final Category category = ticketManager.getCategory(categoryToken);
        if (category == null) {
            throw new ValidationException(String.format("No se ha encontrado la categoría con token %s", categoryToken));
        }

        final String type = StringUtils.trimToEmpty(body.getType());
        if (StringUtils.isBlank(type)) {
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
}
