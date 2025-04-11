package com.fii.practic.mes.wip.domain.equipment;

import com.fii.practic.mes.models.EqStatusType;
import com.fii.practic.mes.wip.general.AbstractRepository;
import jakarta.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class EquipmentStatusRepository extends AbstractRepository<EquipmentStatusEntity> {

    protected EquipmentStatusRepository() {
        super(EquipmentStatusEntity.class, EquipmentStatusEntity.ENTITY_NAME);
    }

    public EqStatusType getEquipmentActiveStatus(String equipmentUuid) {
        return findOneIfExist("id = (select max(es.id) from EquipmentStatus es where es.equipmentUuid = ?1) ", equipmentUuid)
                .map(EquipmentStatusEntity::getStatus)
                .orElse(EqStatusType.STOPPED);
    }
}
