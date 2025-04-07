package com.fii.practic.mes.admin.domain.order;

import com.fii.practic.mes.admin.general.AbstractRepository;
import jakarta.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class OrderRepository extends AbstractRepository<OrderEntity> {

    protected OrderRepository() {
        super(OrderEntity.class, OrderEntity.ENTITY_NAME);
    }
}
