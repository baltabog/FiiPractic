package com.fii.practic.mes.admin.domanin.process.step;

import com.fii.practic.mes.admin.general.AbstractRepository;
import jakarta.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class ProcessStepRepository extends AbstractRepository<ProcessStepEntity> {
    protected ProcessStepRepository() {
        super(ProcessStepEntity.class, ProcessStepEntity.ENTITY_NAME);
    }
}
