package com.fii.practic.mes.admin.domain.equipment.tool;

import com.fii.practic.mes.admin.general.mapper.AbstractEntityMapper;
import com.fii.practic.mes.models.ToolDTO;
import jakarta.inject.Inject;
import jakarta.inject.Named;
import jakarta.inject.Singleton;
import javax.annotation.processing.Generated;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2025-04-07T11:57:35+0300",
    comments = "version: 1.5.3.Final, compiler: javac, environment: Java 17.0.14 (Amazon.com Inc.)"
)
@Singleton
@Named
public class ToolMapperImpl implements ToolMapper {

    @Inject
    private AbstractEntityMapper abstractEntityMapper;

    @Override
    public ToolDTO mapToDto(ToolEntity entity) {
        if ( entity == null ) {
            return null;
        }

        ToolDTO toolDTO = new ToolDTO();

        toolDTO.setEquipmentTypeIdentity( abstractEntityMapper.toIdentityDto( entity.getEquipmentType() ) );
        toolDTO.setName( entity.getName() );
        toolDTO.setActive( entity.getActive() );
        toolDTO.setUuid( entity.getUuid() );
        toolDTO.setDescription( entity.getDescription() );
        toolDTO.setVersion( entity.getVersion() );
        toolDTO.setUpdated( abstractEntityMapper.toOffsetDateTime( entity.getUpdated() ) );
        toolDTO.setUpdatedBy( entity.getUpdatedBy() );
        toolDTO.setAlias( entity.getAlias() );

        return toolDTO;
    }

    @Override
    public ToolEntity mapToEntity(ToolDTO dto) {
        if ( dto == null ) {
            return null;
        }

        ToolEntity toolEntity = new ToolEntity();

        toolEntity.setUuid( dto.getUuid() );
        toolEntity.setName( dto.getName() );
        toolEntity.setDescription( dto.getDescription() );
        toolEntity.setAlias( dto.getAlias() );
        toolEntity.setActive( dto.getActive() );

        return toolEntity;
    }

    @Override
    public ToolEntity updateEntityWithDtoInfo(ToolEntity entity, ToolDTO dto) {
        if ( dto == null ) {
            return entity;
        }

        entity.setDescription( dto.getDescription() );
        entity.setAlias( dto.getAlias() );
        entity.setActive( dto.getActive() );

        return entity;
    }
}
