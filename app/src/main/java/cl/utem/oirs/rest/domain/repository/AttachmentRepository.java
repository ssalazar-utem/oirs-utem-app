package cl.utem.oirs.rest.domain.repository;

import cl.utem.oirs.rest.domain.model.Attachment;
import cl.utem.oirs.rest.domain.model.Ticket;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface AttachmentRepository extends JpaRepository<Attachment, Long> {

    public Attachment findByToken(String token);

    @Query("SELECT a.token FROM Attachment a WHERE a.ticket = :ticket")
    public List<String> searchTokenByTicket(@Param("ticket") Ticket ticket);
}
