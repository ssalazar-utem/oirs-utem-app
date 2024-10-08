package cl.utem.oirs.rest.domain.repository;

import cl.utem.oirs.rest.domain.model.Access;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AccessRepository extends JpaRepository<Access, Long> {

    public List<Access> findByEmailIgnoreCase(String email);
}
