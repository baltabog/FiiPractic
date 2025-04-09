package com.fii.practic.mes.admin.domain.order;

import com.fii.practic.mes.admin.general.mapper.AbstractEntityMapper;
import com.fii.practic.mes.models.OrderDTO;
import jakarta.inject.Inject;
import jakarta.inject.Named;
import jakarta.inject.Singleton;
import javax.annotation.processing.Generated;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2025-04-09T10:36:11+0300",
    comments = "version: 1.5.3.Final, compiler: javac, environment: Java 17.0.7 (Amazon.com Inc.)"
)
@Singleton
@Named
public class OrderMapperImpl implements OrderMapper {

    @Inject
    private AbstractEntityMapper abstractEntityMapper;

    @Override
    public OrderDTO mapToDto(OrderEntity entity) {
        if ( entity == null ) {
            return null;
        }

        OrderDTO orderDTO = new OrderDTO();

        orderDTO.setProcess( abstractEntityMapper.toIdentityDto( entity.getProcessPlan() ) );
        orderDTO.setName( entity.getName() );
        orderDTO.setQuantity( entity.getQuantity() );
        orderDTO.setUuid( entity.getUuid() );
        orderDTO.setDescription( entity.getDescription() );
        orderDTO.setVersion( entity.getVersion() );
        orderDTO.setUpdated( abstractEntityMapper.toOffsetDateTime( entity.getUpdated() ) );
        orderDTO.setUpdatedBy( entity.getUpdatedBy() );
        orderDTO.setCompleteQty( entity.getCompleteQty() );

        return orderDTO;
    }

    @Override
    public OrderEntity mapToEntity(OrderDTO dto) {
        if ( dto == null ) {
            return null;
        }

        OrderEntity orderEntity = new OrderEntity();

        orderEntity.setUuid( dto.getUuid() );
        orderEntity.setName( dto.getName() );
        orderEntity.setDescription( dto.getDescription() );
        orderEntity.setQuantity( dto.getQuantity() );
        orderEntity.setCompleteQty( dto.getCompleteQty() );

        return orderEntity;
    }

    @Override
    public OrderEntity updateEntityWithDtoInfo(OrderEntity entity, OrderDTO dto) {
        if ( dto == null ) {
            return entity;
        }

        entity.setDescription( dto.getDescription() );
        entity.setQuantity( dto.getQuantity() );
        entity.setCompleteQty( dto.getCompleteQty() );

        return entity;
    }
}
