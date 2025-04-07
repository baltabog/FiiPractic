package com.fii.practic.mes.admin.domanin.material;

import com.fii.practic.mes.admin.general.AbstractRepository;
import jakarta.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class MaterialRepository extends AbstractRepository<MaterialEntity> {
    protected MaterialRepository() {
        super(MaterialEntity.class, MaterialEntity.ENTITY_NAME);
    }
}
