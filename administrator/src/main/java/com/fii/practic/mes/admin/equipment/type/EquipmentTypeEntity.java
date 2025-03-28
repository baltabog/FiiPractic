package com.fii.practic.mes.admin.equipment.type;

import com.fii.practic.mes.admin.general.AbstractEntity;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.envers.Audited;

@Entity(name = EquipmentTypeEntity.ENTITY_NAME)
@Table(
        name = EquipmentTypeEntity.TABLE_NAME,
        uniqueConstraints = {
                @UniqueConstraint(columnNames = {"UUID"}, name = "T_EQUIPMENT_TYPE__UUID"),
                @UniqueConstraint(columnNames = {"NAME"}, name = "T_EQUIPMENT_TYPE__NAME")
        }
)
@Audited
@Getter @Setter
public class EquipmentTypeEntity extends AbstractEntity {
    public static final String ENTITY_NAME = "EquipmentType";
    public static final String TABLE_NAME = "T_EQUIPMENT_TYPE";
}
