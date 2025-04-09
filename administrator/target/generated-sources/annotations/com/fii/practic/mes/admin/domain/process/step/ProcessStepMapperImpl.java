package com.fii.practic.mes.admin.domain.process.step;

import com.fii.practic.mes.admin.domain.equipment.tool.ToolEntity;
import com.fii.practic.mes.admin.domain.material.MaterialEntity;
import com.fii.practic.mes.admin.general.mapper.AbstractEntityMapper;
import com.fii.practic.mes.models.IdentityDTO;
import com.fii.practic.mes.models.ProcessStepDTO;
import com.fii.practic.mes.models.ProcessStepMaterialDTO;
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
public class ProcessStepMapperImpl implements ProcessStepMapper {

    @Inject
    private AbstractEntityMapper abstractEntityMapper;

    @Override
    public ProcessStepDTO mapToDto(ProcessStepEntity entity) {
        if ( entity == null ) {
            return null;
        }

        ProcessStepDTO processStepDTO = new ProcessStepDTO();

        processStepDTO.setInputMaterials( processStepInputMaterialEntityListToProcessStepMaterialDTOSet( entity.getProcessStepInputMaterial() ) );
        processStepDTO.setName( entity.getName() );
        processStepDTO.setEquipments( toolEntitySetToIdentityDTOSet( entity.getEquipments() ) );
        processStepDTO.setSuccessOutputMaterials( processStepMaterialSuccessEntityListToProcessStepMaterialDTOSet( entity.getSuccessOutputMaterials() ) );
        processStepDTO.setUuid( entity.getUuid() );
        processStepDTO.setDescription( entity.getDescription() );
        processStepDTO.setVersion( entity.getVersion() );
        processStepDTO.setUpdated( abstractEntityMapper.toOffsetDateTime( entity.getUpdated() ) );
        processStepDTO.setUpdatedBy( entity.getUpdatedBy() );
        processStepDTO.setFailOutputMaterials( processStepMaterialFailEntityListToProcessStepMaterialDTOSet( entity.getFailOutputMaterials() ) );

        return processStepDTO;
    }

    @Override
    public ProcessStepEntity mapToEntity(ProcessStepDTO dto) {
        if ( dto == null ) {
            return null;
        }

        ProcessStepEntity processStepEntity = new ProcessStepEntity();

        processStepEntity.setUuid( dto.getUuid() );
        processStepEntity.setName( dto.getName() );
        processStepEntity.setDescription( dto.getDescription() );

        return processStepEntity;
    }

    @Override
    public ProcessStepEntity updateEntityWithDtoInfo(ProcessStepEntity entity, ProcessStepDTO dto) {
        if ( dto == null ) {
            return entity;
        }

        entity.setDescription( dto.getDescription() );

        return entity;
    }

    @Override
    public ProcessStepMaterialDTO mapToDto(ProcessStepMaterialEntity entity) {
        if ( entity == null ) {
            return null;
        }

        ProcessStepMaterialDTO processStepMaterialDTO = new ProcessStepMaterialDTO();

        processStepMaterialDTO.setUuid( entityMaterialUuid( entity ) );
        processStepMaterialDTO.setName( entityMaterialName( entity ) );
        processStepMaterialDTO.setQuantity( entity.getQuantity() );

        return processStepMaterialDTO;
    }

    protected Set<ProcessStepMaterialDTO> processStepInputMaterialEntityListToProcessStepMaterialDTOSet(List<ProcessStepInputMaterialEntity> list) {
        if ( list == null ) {
            return null;
        }

        Set<ProcessStepMaterialDTO> set = new LinkedHashSet<ProcessStepMaterialDTO>( Math.max( (int) ( list.size() / .75f ) + 1, 16 ) );
        for ( ProcessStepInputMaterialEntity processStepInputMaterialEntity : list ) {
            set.add( mapToDto( processStepInputMaterialEntity ) );
        }

        return set;
    }

    protected Set<IdentityDTO> toolEntitySetToIdentityDTOSet(Set<ToolEntity> set) {
        if ( set == null ) {
            return null;
        }

        Set<IdentityDTO> set1 = new LinkedHashSet<IdentityDTO>( Math.max( (int) ( set.size() / .75f ) + 1, 16 ) );
        for ( ToolEntity toolEntity : set ) {
            set1.add( abstractEntityMapper.toIdentityDto( toolEntity ) );
        }

        return set1;
    }

    protected Set<ProcessStepMaterialDTO> processStepMaterialSuccessEntityListToProcessStepMaterialDTOSet(List<ProcessStepMaterialSuccessEntity> list) {
        if ( list == null ) {
            return null;
        }

        Set<ProcessStepMaterialDTO> set = new LinkedHashSet<ProcessStepMaterialDTO>( Math.max( (int) ( list.size() / .75f ) + 1, 16 ) );
        for ( ProcessStepMaterialSuccessEntity processStepMaterialSuccessEntity : list ) {
            set.add( mapToDto( processStepMaterialSuccessEntity ) );
        }

        return set;
    }

    protected Set<ProcessStepMaterialDTO> processStepMaterialFailEntityListToProcessStepMaterialDTOSet(List<ProcessStepMaterialFailEntity> list) {
        if ( list == null ) {
            return null;
        }

        Set<ProcessStepMaterialDTO> set = new LinkedHashSet<ProcessStepMaterialDTO>( Math.max( (int) ( list.size() / .75f ) + 1, 16 ) );
        for ( ProcessStepMaterialFailEntity processStepMaterialFailEntity : list ) {
            set.add( mapToDto( processStepMaterialFailEntity ) );
        }

        return set;
    }

    private String entityMaterialUuid(ProcessStepMaterialEntity processStepMaterialEntity) {
        if ( processStepMaterialEntity == null ) {
            return null;
        }
        MaterialEntity material = processStepMaterialEntity.getMaterial();
        if ( material == null ) {
            return null;
        }
        String uuid = material.getUuid();
        if ( uuid == null ) {
            return null;
        }
        return uuid;
    }

    private String entityMaterialName(ProcessStepMaterialEntity processStepMaterialEntity) {
        if ( processStepMaterialEntity == null ) {
            return null;
        }
        MaterialEntity material = processStepMaterialEntity.getMaterial();
        if ( material == null ) {
            return null;
        }
        String name = material.getName();
        if ( name == null ) {
            return null;
        }
        return name;
    }
}
