package com.fii.practic.mes.admin.material;

import com.fii.practic.mes.admin.general.AbstractEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.envers.Audited;

@Entity(name = MaterialEntity.ENTITY_NAME)
@Table(
        name = MaterialEntity.TABLE_NAME,
        uniqueConstraints = {
                @UniqueConstraint(columnNames = {"UUID"}, name = MaterialEntity.TABLE_NAME + "__UUID"),
                @UniqueConstraint(columnNames = {"NAME"}, name = MaterialEntity.TABLE_NAME + "__NAME")
        }
)
@Audited
@Getter @Setter
public class MaterialEntity extends AbstractEntity {
    public static final String ENTITY_NAME = "Material";
    public static final String TABLE_NAME = "T_MATERIAL";

    @Column(name = "AVAILABLE_QTY")
    private Integer availableQuantity;

    @Column(name = "QTY_UNIT")
    private String quantityUnit;
}
