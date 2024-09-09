package cl.utem.oirs.rest.manager;

import cl.utem.oirs.rest.domain.enums.IcsoStatus;
import cl.utem.oirs.rest.domain.enums.IcsoType;
import cl.utem.oirs.rest.domain.model.Category;
import cl.utem.oirs.rest.domain.model.Ticket;
import cl.utem.oirs.rest.domain.model.User;
import cl.utem.oirs.rest.domain.repository.CategoryRepository;
import cl.utem.oirs.rest.domain.repository.TicketRepository;
import cl.utem.oirs.rest.utils.IcsoUtils;
import java.io.Serializable;
import java.time.OffsetDateTime;
import java.util.ArrayList;
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

    public Ticket getTicket(final String token) {
        Ticket ticket = null;
        if (StringUtils.isNotBlank(token)) {
            ticket = ticketRepository.findByToken(token);
        }
        return ticket;
    }

    public List<Ticket> getTickets(Category category) {
        List<Ticket> tickets = new ArrayList<>();
        if (category != null) {
            tickets = ticketRepository.findByCategory(category);
        }
        return tickets;
    }

    @Transactional
    public Ticket create(final Category category, final User user, final IcsoType type, final String subject, final String message) {
        final OffsetDateTime now = OffsetDateTime.now();

        Ticket ticket = new Ticket();
        ticket.setCategory(category);
        ticket.setMessage(message);
        ticket.setResponse(StringUtils.EMPTY);
        ticket.setStatus(IcsoStatus.RECEIVED);
        ticket.setSubject(subject);
        ticket.setToken(IcsoUtils.randomAlphanumeric(17));
        ticket.setType(type);
        ticket.setUser(user);
        ticket.setCreated(now);
        ticket.setUpdated(now);

        return ticketRepository.save(ticket);
    }

    @Transactional
    public Ticket save(Ticket ticket) {
        Ticket saved = null;
        if (ticket != null) {
            saved = ticketRepository.save(ticket);
        }
        return saved;
    }

    @Transactional
    public boolean deleteTicket(final String tokenToken) {
        boolean deleted = false;
        if (StringUtils.isNotBlank(tokenToken)) {
            Ticket ticket = ticketRepository.findByToken(tokenToken);
            if (ticket != null) {
                ticketRepository.delete(ticket);
                deleted = true;
            }
        }
        return deleted;
    }
}
