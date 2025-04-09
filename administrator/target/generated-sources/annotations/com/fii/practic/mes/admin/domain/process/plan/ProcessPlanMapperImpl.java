package com.fii.practic.mes.admin.domain.process.plan;

import com.fii.practic.mes.admin.domain.process.step.ProcessStepEntity;
import com.fii.practic.mes.admin.general.mapper.AbstractEntityMapper;
import com.fii.practic.mes.models.OrderedProcessStepDTO;
import com.fii.practic.mes.models.ProcessPlanDTO;
import jakarta.inject.Inject;
import jakarta.inject.Named;
import jakarta.inject.Singleton;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;
import javax.annotation.processing.Generated;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2025-04-09T10:36:11+0300",
    comments = "version: 1.5.3.Final, compiler: javac, environment: Java 17.0.7 (Amazon.com Inc.)"
)
@Singleton
@Named
public class ProcessPlanMapperImpl implements ProcessPlanMapper {

    @Inject
    private AbstractEntityMapper abstractEntityMapper;

    @Override
    public ProcessPlanDTO mapToDto(ProcessPlanEntity entity) {
        if ( entity == null ) {
            return null;
        }

        ProcessPlanDTO processPlanDTO = new ProcessPlanDTO();

        processPlanDTO.setName( entity.getName() );
        processPlanDTO.setOrderedProcessSteps( processPlanStepEntityListToOrderedProcessStepDTOSet( entity.getOrderedProcessSteps() ) );
        processPlanDTO.setUuid( entity.getUuid() );
        processPlanDTO.setDescription( entity.getDescription() );
        processPlanDTO.setVersion( entity.getVersion() );
        processPlanDTO.setUpdated( abstractEntityMapper.toOffsetDateTime( entity.getUpdated() ) );
        processPlanDTO.setUpdatedBy( entity.getUpdatedBy() );

        return processPlanDTO;
    }

    @Override
    public ProcessPlanEntity mapToEntity(ProcessPlanDTO dto) {
        if ( dto == null ) {
            return null;
        }

        ProcessPlanEntity processPlanEntity = new ProcessPlanEntity();

        processPlanEntity.setUuid( dto.getUuid() );
        processPlanEntity.setName( dto.getName() );
        processPlanEntity.setDescription( dto.getDescription() );

        return processPlanEntity;
    }

    @Override
    public ProcessPlanEntity updateEntityWithDtoInfo(ProcessPlanEntity entity, ProcessPlanDTO dto) {
        if ( dto == null ) {
            return entity;
        }

        entity.setDescription( dto.getDescription() );

        return entity;
    }

    @Override
    public OrderedProcessStepDTO mapToDto(ProcessPlanStepEntity entity) {
        if ( entity == null ) {
            return null;
        }

        OrderedProcessStepDTO orderedProcessStepDTO = new OrderedProcessStepDTO();

        orderedProcessStepDTO.setUuid( entityProcessStepUuid( entity ) );
        orderedProcessStepDTO.setName( entityProcessStepName( entity ) );
        orderedProcessStepDTO.setOrderInProcess( entity.getOrderInProcess() );

        return orderedProcessStepDTO;
    }

    protected Set<OrderedProcessStepDTO> processPlanStepEntityListToOrderedProcessStepDTOSet(List<ProcessPlanStepEntity> list) {
        if ( list == null ) {
            return null;
        }

        Set<OrderedProcessStepDTO> set = new LinkedHashSet<OrderedProcessStepDTO>( Math.max( (int) ( list.size() / .75f ) + 1, 16 ) );
        for ( ProcessPlanStepEntity processPlanStepEntity : list ) {
            set.add( mapToDto( processPlanStepEntity ) );
        }

        return set;
    }

    private String entityProcessStepUuid(ProcessPlanStepEntity processPlanStepEntity) {
        if ( processPlanStepEntity == null ) {
            return null;
        }
        ProcessStepEntity processStep = processPlanStepEntity.getProcessStep();
        if ( processStep == null ) {
            return null;
        }
        String uuid = processStep.getUuid();
        if ( uuid == null ) {
            return null;
        }
        return uuid;
    }

    private String entityProcessStepName(ProcessPlanStepEntity processPlanStepEntity) {
        if ( processPlanStepEntity == null ) {
            return null;
        }
        ProcessStepEntity processStep = processPlanStepEntity.getProcessStep();
        if ( processStep == null ) {
            return null;
        }
        String name = processStep.getName();
        if ( name == null ) {
            return null;
        }
        return name;
    }
}
