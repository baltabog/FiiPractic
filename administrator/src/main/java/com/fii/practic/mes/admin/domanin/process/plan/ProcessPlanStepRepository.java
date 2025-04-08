package com.fii.practic.mes.admin.domanin.process.plan;

import io.quarkus.hibernate.orm.panache.PanacheRepository;
import jakarta.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class ProcessPlanStepRepository implements PanacheRepository<ProcessPlanStepEntity> {
}
