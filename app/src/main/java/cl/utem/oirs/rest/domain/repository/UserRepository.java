package cl.utem.oirs.rest.domain.repository;

import cl.utem.oirs.rest.domain.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    public User findByEmailIgnoreCase(String email);
}
