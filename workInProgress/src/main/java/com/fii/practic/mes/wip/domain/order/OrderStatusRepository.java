package com.fii.practic.mes.wip.domain.order;

import com.fii.practic.mes.wip.general.AbstractRepository;
import jakarta.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class OrderStatusRepository extends AbstractRepository<OrderStatusEntity> {
    protected OrderStatusRepository() {
        super(OrderStatusEntity.class, OrderStatusEntity.ENTITY_NAME);
    }
}
