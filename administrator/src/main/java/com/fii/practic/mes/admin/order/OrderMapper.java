package com.fii.practic.mes.admin.order;

import com.fii.practic.mes.admin.general.mapper.AbstractEntityMapper;
import com.fii.practic.mes.models.OrderDTO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "jakarta",
        uses = AbstractEntityMapper.class)
public interface OrderMapper {
    @Mapping(target = "process",        source = "processPlan")
    OrderDTO mapToDto(OrderEntity entity);

    @Mapping(target = "version",            ignore = true)
    @Mapping(target = "updated",	        ignore = true)
    @Mapping(target = "updatedBy",	        ignore = true)
    OrderEntity mapToEntity(OrderDTO dto);

    @Mapping(target = "id",                 ignore = true)
    @Mapping(target = "uuid",               ignore = true)
    @Mapping(target = "name",               ignore = true)
    @Mapping(target = "version",            ignore = true)
    @Mapping(target = "updated",	        ignore = true)
    @Mapping(target = "updatedBy",	        ignore = true)
    OrderEntity updateEntityWithDtoInfo(@MappingTarget OrderEntity entity, OrderDTO dto);
}
