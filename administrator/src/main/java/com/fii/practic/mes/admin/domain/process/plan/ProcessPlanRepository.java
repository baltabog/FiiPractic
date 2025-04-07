package com.fii.practic.mes.admin.domain.process.plan;

import com.fii.practic.mes.admin.general.AbstractRepository;
import jakarta.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class ProcessPlanRepository extends AbstractRepository<ProcessPlanEntity> {
    protected ProcessPlanRepository() {
        super(ProcessPlanEntity.class, ProcessPlanEntity.ENTITY_NAME);
    }
}
