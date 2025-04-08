package com.fii.practic.mes.admin.domanin.process.step;

import com.fii.practic.mes.admin.general.mapper.AbstractEntityMapper;
import com.fii.practic.mes.models.ProcessStepDTO;
import com.fii.practic.mes.models.ProcessStepMaterialDTO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "jakarta",
    uses = {AbstractEntityMapper.class})
public interface ProcessStepMapper {
    @Mapping(target = "inputMaterials",         source = "processStepInputMaterial")
    ProcessStepDTO mapToDto(ProcessStepEntity entity);

    @Mapping(target = "version",            ignore = true)
    @Mapping(target = "updated",	        ignore = true)
    @Mapping(target = "updatedBy",	        ignore = true)
    @Mapping(target = "equipments",         ignore = true)
    @Mapping(target = "processStepInputMaterial",   ignore = true)
    @Mapping(target = "successOutputMaterials",     ignore = true)
    @Mapping(target = "failOutputMaterials",        ignore = true)
    ProcessStepEntity mapToEntity(ProcessStepDTO dto);

    @Mapping(target = "id",                 ignore = true)
    @Mapping(target = "uuid",               ignore = true)
    @Mapping(target = "name",               ignore = true)
    @Mapping(target = "version",            ignore = true)
    @Mapping(target = "updated",	        ignore = true)
    @Mapping(target = "updatedBy",	        ignore = true)
    @Mapping(target = "processStepInputMaterial",   ignore = true)
    @Mapping(target = "successOutputMaterials",     ignore = true)
    @Mapping(target = "failOutputMaterials",        ignore = true)
    @Mapping(target = "equipments",        ignore = true)
    ProcessStepEntity updateEntityWithDtoInfo(@MappingTarget ProcessStepEntity entity, ProcessStepDTO dto);

    @Mapping(target = "uuid",           source = "material.uuid")
    @Mapping(target = "name",           source = "material.name")
    ProcessStepMaterialDTO mapToDto(ProcessStepMaterialEntity entity);
}
