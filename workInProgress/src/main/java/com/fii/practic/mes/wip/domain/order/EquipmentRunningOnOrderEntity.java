package com.fii.practic.mes.wip.domain.order;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.SequenceGenerator;
import jakarta.persistence.Table;

@Entity(name = EquipmentRunningOnOrderEntity.ENTITY_NAME)
@Table(name = EquipmentRunningOnOrderEntity.TABLE_NAME)
public class EquipmentRunningOnOrderEntity {
    public static final String ENTITY_NAME = "EquipmentRunningOnOrder";
    public static final String TABLE_NAME = "T_EQUIPMENT_RUNNING_ON_ORDER";

    @Id
    @Column(name = "ID", nullable = false, unique = true)
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "entity_id_generator")
    @SequenceGenerator(name = "entity_id_generator", sequenceName = "T_SEQUENCE__ID", allocationSize = 1)
    private Long id;

    @ManyToOne
    OrderStatusEntity orderStatus;
}
