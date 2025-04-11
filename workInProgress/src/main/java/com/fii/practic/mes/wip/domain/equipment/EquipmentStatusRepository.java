package com.fii.practic.mes.wip.domain.equipment;

import com.fii.practic.mes.wip.general.AbstractRepository;
import jakarta.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class EquipmentStatusRepository extends AbstractRepository<EquipmentStatusEntity> {

    protected EquipmentStatusRepository() {
        super(EquipmentStatusEntity.class, EquipmentStatusEntity.ENTITY_NAME);
    }

    public EquipmentStatusEntity getEquipmentActiveStatus(String equipmentUuid) {
        return findOneMandatory("id = (select max(es.id) from EquipmentStatus es where uuid = ?1) ", equipmentUuid);
    }
}
