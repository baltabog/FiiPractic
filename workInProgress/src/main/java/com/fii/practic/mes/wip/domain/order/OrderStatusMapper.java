package com.fii.practic.mes.wip.domain.order;

import com.fii.practic.mes.admin.models.OrderDTO;
import com.fii.practic.mes.models.OrderStatusDTO;
import com.fii.practic.mes.models.OrderStatusType;
import org.mapstruct.Context;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "jakarta")
public interface OrderStatusMapper {
    @Mapping(target = "orderUuid",      source = "uuid")
    @Mapping(target = "orderName",      source = "name")
    @Mapping(target = "status",         expression = "java( status )")
    @Mapping(target = "timestamp", 		ignore = true)
    OrderStatusEntity getNewOrderStatus(OrderDTO orderDTO, @Context OrderStatusType status);

    @Mapping(target = "identity.uuid",  source = "uuid")
    @Mapping(target = "identity.name",  source = "name")
    @Mapping(target = "status",         expression = "java( status )")
    OrderStatusDTO toOrderStatusDto(OrderDTO orderDTO, @Context OrderStatusType status);

    @Mapping(target = "identity.uuid",  source = "orderUuid")
    @Mapping(target = "identity.name",  source = "orderName")
    @Mapping(target = "timestamp",     expression = "java(entity.getTimestamp().atOffset(java.time.OffsetDateTime.now().getOffset()))")
    OrderStatusDTO toOrderStatusDto(OrderStatusEntity entity);
}
