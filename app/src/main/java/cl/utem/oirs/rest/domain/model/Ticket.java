package cl.utem.oirs.rest.domain.model;

import cl.utem.oirs.rest.domain.enums.IcsoStatus;
import cl.utem.oirs.rest.domain.enums.IcsoType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

/**
 *
 * @author seba
 */
@Entity
@Table(name = "tickets")
public class Ticket extends PkBaseEntity {

    @Column(name = "token", nullable = false, unique = true)
    private String token = null;

    @JoinColumn(name = "user_fk", referencedColumnName = "pk", nullable = false)
    @ManyToOne
    private User user = null;

    @JoinColumn(name = "category_fk", referencedColumnName = "pk", nullable = false)
    @ManyToOne
    private Category category = null;

    @Column(name = "icso_type", nullable = false)
    private IcsoType type = IcsoType.INFORMATION;

    @Column(name = "icso_status", nullable = false)
    private IcsoStatus status = IcsoStatus.ERROR;

    @Column(name = "subject", nullable = false)
    private String subject = null;

    @Column(name = "message", nullable = false)
    private String message = null;

    @Column(name = "response")
    private String response = null;

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Category getCategory() {
        return category;
    }

    public void setCategory(Category category) {
        this.category = category;
    }

    public IcsoType getType() {
        return type;
    }

    public void setType(IcsoType type) {
        this.type = type;
    }

    public IcsoStatus getStatus() {
        return status;
    }

    public void setStatus(IcsoStatus status) {
        this.status = status;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getResponse() {
        return response;
    }

    public void setResponse(String response) {
        this.response = response;
    }
}
