package com.fii.practic.mes.wip.domain.order;

import com.fii.practic.mes.models.OrderStatusType;
import com.fii.practic.mes.wip.general.AbstractRepository;
import jakarta.enterprise.context.ApplicationScoped;

import java.util.Optional;

@ApplicationScoped
public class OrderStatusRepository extends AbstractRepository<OrderStatusEntity> {

    protected OrderStatusRepository() {
        super(OrderStatusEntity.class, OrderStatusEntity.ENTITY_NAME);
    }

    public Optional<OrderStatusEntity> getActiveOrder() {
        return this.findOneIfExist("status = ?1", OrderStatusType.STARTED);
    }
}
