package cl.utem.oirs.rest.domain.repository;

import cl.utem.oirs.rest.domain.enums.IcsoStatus;
import cl.utem.oirs.rest.domain.enums.IcsoType;
import cl.utem.oirs.rest.domain.model.Category;
import cl.utem.oirs.rest.domain.model.Ticket;
import cl.utem.oirs.rest.domain.model.User;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface TicketRepository extends JpaRepository<Ticket, Long> {

    public Ticket findByToken(String token);

    @Query("SELECT t FROM Ticket t WHERE t.category = :category "
            + " AND (:user IS NULL OR t.user = :user) "
            + " AND (:type IS NULL OR t.type = :type) "
            + " AND (:status IS NULL OR t.status = :status) "
            + " ORDER BY t.created ASC, t.updated DESC")
    public List<Ticket> search(@Param("category") Category category,
            @Param("user") User user,
            @Param("type") IcsoType type,
            @Param("status") IcsoStatus status);

}
