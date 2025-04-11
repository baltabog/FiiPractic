package com.fii.practic.mes.wip.domain.order;

import com.fii.practic.mes.models.OrderStatusType;
import com.fii.practic.mes.wip.general.AbstractEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.NaturalId;

@Entity(name = OrderStatusEntity.ENTITY_NAME)
@Table(name = OrderStatusEntity.TABLE_NAME)
@Getter @Setter
public class OrderStatusEntity extends AbstractEntity {
    public static final String ENTITY_NAME = "OrderStatus";
    public static final String TABLE_NAME = "T_ORDER_STATUS";

    @Column(name = "ORDER_UUID", nullable = false, unique = true)
    private String orderUuid;

    @Column(name = "ORDER_NAME", nullable = false, updatable = false)
    @NaturalId
    private String orderName;

    @Column(name = "STATUS",columnDefinition = "VARCHAR(16)")
    @Enumerated(value = EnumType.STRING)
    private OrderStatusType status;
}
