package com.fii.practic.mes.wip.domain.equipment;

import com.fii.practic.mes.models.EqStatusType;
import com.fii.practic.mes.wip.general.AbstractEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;


@Entity(name = EquipmentStatusEntity.ENTITY_NAME)
@Table(name = EquipmentStatusEntity.TABLE_NAME)
@Getter @Setter
public class EquipmentStatusEntity extends AbstractEntity {
    public static final String ENTITY_NAME = "EquipmentStatus";
    public static final String TABLE_NAME = "T_EQUIPMENT_STATUS";

    @Column(name = "ORDER_NAME")
    private String orderName;
    @Column(name = "ORDER_UUID")
    private String orderUuid;

    @Column(name = "EQUIPMENT_NAME")
    private String equipmentName;
    @Column(name = "EQUIPMENT_UUID")
    private String equipmentUuid;

    @Column(name = "STATUS")
    @Enumerated(EnumType.STRING)
    private EqStatusType status;
}
