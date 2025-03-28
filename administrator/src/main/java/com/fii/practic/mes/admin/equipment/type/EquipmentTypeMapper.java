package com.fii.practic.mes.admin.equipment.type;

import com.fii.practic.mes.models.EquipmentTypeDTO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "jakarta")
public interface EquipmentTypeMapper {
    @Mapping(target = "updated", 			expression = "java(entity.getUpdated().atOffset(java.time.OffsetDateTime.now().getOffset()))")
    EquipmentTypeDTO mapToDto(EquipmentTypeEntity entity);

    @Mapping(target = "version",            ignore = true)
    @Mapping(target = "updated",	        ignore = true)
    @Mapping(target = "updatedBy",	        ignore = true)
    EquipmentTypeEntity mapToEntity(EquipmentTypeDTO dto);

    @Mapping(target = "id",                 ignore = true)
    @Mapping(target = "uuid",               ignore = true)
    @Mapping(target = "name",               ignore = true)
    @Mapping(target = "version",            ignore = true)
    @Mapping(target = "updated",	        ignore = true)
    @Mapping(target = "updatedBy",	        ignore = true)
    EquipmentTypeEntity updateEntityWithDtoInfo(@MappingTarget EquipmentTypeEntity entity, EquipmentTypeDTO dto);
}
