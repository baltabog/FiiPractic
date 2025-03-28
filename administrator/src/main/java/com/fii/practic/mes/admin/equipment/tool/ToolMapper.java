package com.fii.practic.mes.admin.equipment.tool;

import com.fii.practic.mes.admin.equipment.type.EquipmentTypeEntity;
import com.fii.practic.mes.models.IdentityDTO;
import com.fii.practic.mes.models.ToolDTO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "jakarta")
public interface ToolMapper {
    @Mapping(target = "equipmentTypeIdentity", source = "equipmentType")
    @Mapping(target = "updated", 			expression = "java(entity.getUpdated().atOffset(java.time.OffsetDateTime.now().getOffset()))")
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

    IdentityDTO getEquipmentTypeIdentity(EquipmentTypeEntity entity);
}
