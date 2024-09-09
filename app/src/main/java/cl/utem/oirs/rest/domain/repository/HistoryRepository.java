package cl.utem.oirs.rest.domain.repository;

import cl.utem.oirs.rest.domain.model.History;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface HistoryRepository extends JpaRepository<History, Long> {

}
