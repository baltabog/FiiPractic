package com.fii.practic.mes.admin.domain.equipment.tool;

import com.fii.practic.mes.admin.domain.equipment.type.EquipmentTypeEntity;
import com.fii.practic.mes.admin.general.AbstractEntity;
import com.fii.practic.mes.admin.general.util.NullableBooleanToStringConverter;
import jakarta.persistence.Column;
import jakarta.persistence.Convert;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.envers.Audited;

@Entity(name = ToolEntity.ENTITY_NAME)
@Table(
        name = ToolEntity.TABLE_NAME,
        uniqueConstraints = {
                @UniqueConstraint(columnNames = {"UUID"}, name = "T_EQUIPMENT__UUID"),
                @UniqueConstraint(columnNames = {"NAME"}, name = "T_EQUIPMENT__NAME")
        }
)
@Audited
@Getter @Setter
public class ToolEntity extends AbstractEntity {
    public static final String ENTITY_NAME = "Equipment";
    public static final String TABLE_NAME = "T_EQUIPMENT";

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "EQ_TYPE_ID", nullable = false, updatable = false)
    private EquipmentTypeEntity equipmentType;

    @Column(name = "ALIAS")
    private String alias;

    @Column(name = "ACTIVE", nullable = false)
    @Convert(converter = NullableBooleanToStringConverter.class)
    private Boolean active;
}
