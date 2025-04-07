package com.fii.practic.mes.admin.domain.equipment.tool;

import com.fii.practic.mes.admin.general.mapper.AbstractEntityMapper;
import com.fii.practic.mes.models.ToolDTO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "jakarta",
        uses = {AbstractEntityMapper.class})
public interface ToolMapper {
    @Mapping(target = "equipmentTypeIdentity", source = "equipmentType")
    ToolDTO mapToDto(ToolEntity entity);

    @Mapping(target = "version",            ignore = true)
    @Mapping(target = "updated",	        ignore = true)
    @Mapping(target = "updatedBy",	        ignore = true)
    ToolEntity mapToEntity(ToolDTO dto);

    @Mapping(target = "id",                 ignore = true)
    @Mapping(target = "uuid",               ignore = true)
    @Mapping(target = "name",               ignore = true)
    @Mapping(target = "version",            ignore = true)
    @Mapping(target = "updated",	        ignore = true)
    @Mapping(target = "updatedBy",	        ignore = true)
    ToolEntity updateEntityWithDtoInfo(@MappingTarget ToolEntity entity, ToolDTO dto);
}
