package cl.utem.oirs.rest.manager;

import cl.utem.oirs.rest.domain.enums.UserRole;
import cl.utem.oirs.rest.domain.model.Access;
import cl.utem.oirs.rest.domain.model.User;
import cl.utem.oirs.rest.domain.repository.AccessRepository;
import cl.utem.oirs.rest.domain.repository.UserRepository;
import cl.utem.oirs.rest.utils.GoogleAuthUtils;
import cl.utem.oirs.rest.utils.IpUtils;
import jakarta.servlet.http.HttpServletRequest;
import java.io.Serializable;
import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.List;
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

    private void saveAccess(HttpServletRequest request, User user) {
        if (request != null && user != null) {
            final String ip = IpUtils.getClientIp(request);
            final String userAgent = StringUtils.trimToEmpty(request.getHeader("User-Agent"));
            final String request_uri = StringUtils.trimToEmpty(request.getRequestURI());

            if (!StringUtils.isAnyBlank(ip, userAgent, request_uri)) {
                Access access = new Access();
                access.setEmail(user.getEmail());
                access.setIp(ip);
                access.setRequestUri(request_uri);
                access.setUserAgent(userAgent);
                access.setCreated(user.getUpdated());
                access.setUpdated(user.getUpdated());
                accessRepository.save(access);
            }
        }
    }

    @Transactional
    public User authenticate(HttpServletRequest request, final String authorization) {
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
            saveAccess(request, user);
        }
        return user;
    }

    public List<Access> getAccess(final User user) {
        List<Access> list = new ArrayList<>();
        if (user != null) {
            list = accessRepository.findByEmailIgnoreCase(user.getEmail());
        }
        return list;
    }
}
