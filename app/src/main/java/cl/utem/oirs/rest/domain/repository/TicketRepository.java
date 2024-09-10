package cl.utem.oirs.rest.domain.repository;

import cl.utem.oirs.rest.domain.model.Category;
import cl.utem.oirs.rest.domain.model.Ticket;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TicketRepository extends JpaRepository<Ticket, Long> {

    public Ticket findByToken(String token);

    public List<Ticket> findByCategory(Category category);
}
