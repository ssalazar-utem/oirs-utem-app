package cl.utem.oirs.rest.domain.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;

@Entity
@Table(name = "access")
public class Access extends PkBaseEntity {

    @Column(name = "email", nullable = false, unique = true)
    private String email = null;

    @Column(name = "ip", nullable = false)
    private String ip = null;

    @Column(name = "user_agent", nullable = false)
    private String userAgent = null;

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public String getUserAgent() {
        return userAgent;
    }

    public void setUserAgent(String userAgent) {
        this.userAgent = userAgent;
    }
}
