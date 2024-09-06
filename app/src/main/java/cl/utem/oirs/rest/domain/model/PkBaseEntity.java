package cl.utem.oirs.rest.domain.model;

import jakarta.persistence.Column;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.MappedSuperclass;
import java.time.OffsetDateTime;
import java.util.Objects;

@MappedSuperclass
public class PkBaseEntity extends Seba implements Comparable<PkBaseEntity> {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "pk", nullable = false, updatable = false)
    private Long id = null;

    @Column(name = "created", nullable = false, updatable = false)
    protected OffsetDateTime created = OffsetDateTime.now();

    @Column(name = "updated", nullable = false)
    protected OffsetDateTime updated = OffsetDateTime.now();

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public OffsetDateTime getCreated() {
        return created;
    }

    public void setCreated(OffsetDateTime created) {
        this.created = created;
    }

    public OffsetDateTime getUpdated() {
        return updated;
    }

    public void setUpdated(OffsetDateTime updated) {
        this.updated = updated;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null || getClass() != obj.getClass()) {
            return false;
        }
        PkBaseEntity other = (PkBaseEntity) obj;
        return Objects.equals(this.id, other.id);
    }

    @Override
    public int compareTo(PkBaseEntity other) {
        return created.compareTo(other.getCreated());
    }
}
