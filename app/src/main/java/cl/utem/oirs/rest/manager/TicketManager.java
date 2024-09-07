package cl.utem.oirs.rest.manager;

import cl.utem.oirs.rest.domain.enums.IcsoStatus;
import cl.utem.oirs.rest.domain.model.Category;
import cl.utem.oirs.rest.domain.model.Ticket;
import cl.utem.oirs.rest.domain.model.User;
import cl.utem.oirs.rest.domain.repository.CategoryRepository;
import cl.utem.oirs.rest.domain.repository.TicketRepository;
import cl.utem.oirs.rest.exception.ValidationException;
import cl.utem.oirs.rest.utils.IcsoUtils;
import java.io.Serializable;
import java.time.OffsetDateTime;
import java.util.List;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class TicketManager implements Serializable {

    private static final long serialVersionUID = 1L;
    private final transient CategoryRepository categoryRepository;
    private final transient TicketRepository ticketRepository;
    private static final Logger LOGGER = LoggerFactory.getLogger(TicketManager.class);

    @Autowired
    public TicketManager(CategoryRepository categoryRepository, TicketRepository ticketRepository) {
        this.categoryRepository = categoryRepository;
        this.ticketRepository = ticketRepository;
    }

    public Category getCategory(final String token) {
        Category category = null;
        if (StringUtils.isNotBlank(token)) {
            category = categoryRepository.findByToken(token);
        }
        return category;
    }

    public List<Category> getCatogories() {
        return categoryRepository.findAll();
    }

    @Transactional
    public Ticket create(final Category category, final User user, final String type, final String subject, final String message) {
        final OffsetDateTime now = OffsetDateTime.now();

        Ticket ticket = new Ticket();
        ticket.setCategory(category);
        ticket.setMessage(message);
        ticket.setResponse(StringUtils.EMPTY);
        ticket.setStatus(IcsoStatus.RECEIVED);
        ticket.setSubject(subject);
        ticket.setToken(IcsoUtils.randomAlphanumeric(17));
        ticket.setType(IcsoUtils.getType(type));
        ticket.setUser(user);
        ticket.setCreated(now);
        ticket.setUpdated(now);

        return ticketRepository.save(ticket);
    }
}
