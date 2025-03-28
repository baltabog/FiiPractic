package com.fii.practic.mes.admin.equipment.type;

import com.fii.practic.mes.admin.general.AbstractRepository;
import jakarta.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class EquipmentTypeRepository extends AbstractRepository<EquipmentTypeEntity> {

    protected EquipmentTypeRepository() {
        super(EquipmentTypeEntity.class, EquipmentTypeEntity.ENTITY_NAME);
    }
}
