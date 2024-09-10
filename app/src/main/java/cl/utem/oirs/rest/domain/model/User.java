package cl.utem.oirs.rest.domain.model;

import cl.utem.oirs.rest.domain.enums.UserRole;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;

@Entity
@Table(name = "users")
public class User extends PkBaseEntity {

    @Column(name = "email", nullable = false, unique = true)
    private String email = null;

    @Column(name = "role", nullable = false)
    private UserRole role = UserRole.STUDENT;

    @Column(name = "active", nullable = false)
    private boolean active = true;

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public UserRole getRole() {
        return role;
    }

    public void setRole(UserRole role) {
        this.role = role;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    @Override
    public int hashCode() {
        return super.hashCode();
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }

        return super.equals(obj);
    }
}
