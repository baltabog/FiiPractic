package com.fii.practic.mes.wip.general;

import com.fii.practic.mes.wip.general.util.GmtDateTimeToEpochSecondsConverter;
import jakarta.persistence.Column;
import jakarta.persistence.Convert;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.MappedSuperclass;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
import jakarta.persistence.SequenceGenerator;
import jakarta.validation.constraints.NotEmpty;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Objects;

@MappedSuperclass
@Getter @Setter
public abstract class AbstractEntity implements Serializable {
    @Id
    @Column(name = "ID", nullable = false, unique = true)
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "entity_id_generator")
    @SequenceGenerator(name = "entity_id_generator", sequenceName = "T_SEQUENCE__ID", allocationSize = 1)
    private Long id;

    @Column(name = "UPDATED", nullable = false)
    @Convert(converter = GmtDateTimeToEpochSecondsConverter.class)
    private LocalDateTime updated;

    @Column(name = "UPDATED_BY", nullable = false)
    @NotEmpty
    private String updatedBy;

    /**
     * Runs before the EntityManager persists the entity the first time
     */
    @PrePersist
    protected void prePersist() {
        preUpdate();
    }

    /**
     * Runs before update of entity after the EntityManager recognizes, that the entity is changed.
     */
    @PreUpdate
    protected void preUpdate() {
        updated = LocalDateTime.now();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof AbstractEntity that)) return false;

        if (!Objects.equals(id, that.id)) return false;
        if (!Objects.equals(updated, that.updated)) return false;
        return Objects.equals(updatedBy, that.updatedBy);
    }

    @Override
    public int hashCode() {
        int result = id != null ? id.hashCode() : 0;
        result = 31 * result + (updated != null ? updated.hashCode() : 0);
        result = 31 * result + (updatedBy != null ? updatedBy.hashCode() : 0);
        return result;
    }
}

