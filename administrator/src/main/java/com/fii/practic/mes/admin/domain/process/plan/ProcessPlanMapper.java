package com.fii.practic.mes.admin.domain.process.plan;

import com.fii.practic.mes.admin.general.mapper.AbstractEntityMapper;
import com.fii.practic.mes.models.OrderedProcessStepDTO;
import com.fii.practic.mes.models.ProcessPlanDTO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "jakarta",
        uses = AbstractEntityMapper.class)
public interface ProcessPlanMapper {
    ProcessPlanDTO mapToDto(ProcessPlanEntity entity);

    @Mapping(target = "version",                ignore = true)
    @Mapping(target = "updated",	            ignore = true)
    @Mapping(target = "updatedBy",	            ignore = true)
    @Mapping(target = "orderedProcessSteps",    ignore = true)
    ProcessPlanEntity mapToEntity(ProcessPlanDTO dto);

    @Mapping(target = "id",                 ignore = true)
    @Mapping(target = "uuid",               ignore = true)
    @Mapping(target = "name",               ignore = true)
    @Mapping(target = "version",            ignore = true)
    @Mapping(target = "updated",	        ignore = true)
    @Mapping(target = "updatedBy",	        ignore = true)
    @Mapping(target = "orderedProcessSteps",ignore = true)
    ProcessPlanEntity updateEntityWithDtoInfo(@MappingTarget ProcessPlanEntity entity, ProcessPlanDTO dto);

    @Mapping(target = "uuid",           source = "processStep.uuid")
    @Mapping(target = "name",           source = "processStep.name")
    OrderedProcessStepDTO mapToDto(ProcessPlanStepEntity entity);
}
