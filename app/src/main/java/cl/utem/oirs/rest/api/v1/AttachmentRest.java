package cl.utem.oirs.rest.api.v1;

import cl.utem.oirs.rest.manager.AuthManager;
import cl.utem.oirs.rest.manager.TicketManager;
import java.io.Serializable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@CrossOrigin
@RestController
@RequestMapping(value = "/v1/attachments",
        consumes = {MediaType.APPLICATION_JSON_VALUE},
        produces = {MediaType.APPLICATION_JSON_VALUE}
)
public class AttachmentRest implements Serializable {

    private static final long serialVersionUID = 1;
    private final transient AuthManager authManager;
    private final transient TicketManager ticketManager;
    private static final Logger LOGGER = LoggerFactory.getLogger(AttachmentRest.class);

    @Autowired
    public AttachmentRest(AuthManager authManager, TicketManager ticketManager) {
        this.authManager = authManager;
        this.ticketManager = ticketManager;
    }

    
}
