package com.fii.practic.mes.wip.domain.equipment;

import com.fii.practic.mes.wip.general.AbstractEntity;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;


@Entity(name = EquipmentStatusEntity.ENTITY_NAME)
@Table(name = EquipmentStatusEntity.TABLE_NAME)
@Getter
@Setter
public class EquipmentStatusEntity extends AbstractEntity {
    public static final String ENTITY_NAME = "EquipmentStatus";
    public static final String TABLE_NAME = "T_EQUIPMENT_STATUS";
}
