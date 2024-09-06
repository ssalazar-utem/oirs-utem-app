package cl.utem.oirs.rest.manager;

import cl.utem.oirs.rest.domain.enums.UserRole;
import cl.utem.oirs.rest.domain.model.Access;
import cl.utem.oirs.rest.domain.model.User;
import cl.utem.oirs.rest.domain.repository.AccessRepository;
import cl.utem.oirs.rest.domain.repository.UserRepository;
import cl.utem.oirs.rest.utils.GoogleAuthUtils;
import java.io.Serializable;
import java.time.OffsetDateTime;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class AuthManager implements Serializable {

    private static final long serialVersionUID = 1L;

    private final transient AccessRepository accessRepository;
    private final transient UserRepository userRepository;

    @Autowired
    public AuthManager(AccessRepository accessRepository, UserRepository userRepository) {
        this.accessRepository = accessRepository;
        this.userRepository = userRepository;
    }

    @Transactional
    public User authenticate(final String authorization, final String ip, final String userAgent) {
        User user = null;
        if (StringUtils.isNotBlank(authorization)) {
            final OffsetDateTime now = OffsetDateTime.now();
            final String idToken = StringUtils.trimToEmpty(StringUtils.removeStartIgnoreCase(authorization, "bearer"));
            final String email = GoogleAuthUtils.getEmail(idToken);
            User current = userRepository.findByEmailIgnoreCase(email);
            if (current == null) {
                current = new User();
                current.setActive(true);
                current.setEmail(email);
                current.setRole(UserRole.STUDENT);
                current.setCreated(now);
                current.setUpdated(now);
            } else {
                current.setUpdated(now);
            }
            user = userRepository.save(current);
            if (!StringUtils.isAnyBlank(ip, userAgent)) {
                Access access = new Access();
                access.setEmail(user.getEmail());
                access.setIp(ip);
                access.setUserAgent(userAgent);
                access.setCreated(now);
                access.setUpdated(now);
                accessRepository.save(access);
            }
        }
        return user;
    }
}
