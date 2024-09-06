package cl.utem.oirs.rest.manager;

import cl.utem.oirs.rest.domain.model.Category;
import cl.utem.oirs.rest.domain.model.Ticket;
import cl.utem.oirs.rest.domain.model.User;
import cl.utem.oirs.rest.domain.repository.CategoryRepository;
import cl.utem.oirs.rest.domain.repository.TicketRepository;
import cl.utem.oirs.rest.exception.ValidationException;
import java.io.Serializable;
import java.util.List;
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

    public List<Category> getCatogories() {
        return categoryRepository.findAll();
    }

    @Transactional
    public Ticket create(final User user, final String type, final String subject, final String message) {
        Ticket ticket = null;
        if (user == null) {
            throw new ValidationException("Se requiere un usuario válido");
        }
        
        
        
        return ticket;
    }
}
