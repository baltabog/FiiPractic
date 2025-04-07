package com.fii.practic.mes.admin.domanin.equipment.tool;

import com.fii.practic.mes.admin.general.AbstractRepository;
import jakarta.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class ToolRepository extends AbstractRepository<ToolEntity> {

    protected ToolRepository() {
        super(ToolEntity.class, ToolEntity.ENTITY_NAME);
    }
}
