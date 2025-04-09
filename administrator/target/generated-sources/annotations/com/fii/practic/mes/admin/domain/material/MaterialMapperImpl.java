package com.fii.practic.mes.admin.domain.material;

import com.fii.practic.mes.admin.general.mapper.AbstractEntityMapper;
import com.fii.practic.mes.models.MaterialDTO;
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
public class MaterialMapperImpl implements MaterialMapper {

    @Inject
    private AbstractEntityMapper abstractEntityMapper;

    @Override
    public MaterialDTO mapToDto(MaterialEntity entity) {
        if ( entity == null ) {
            return null;
        }

        MaterialDTO materialDTO = new MaterialDTO();

        materialDTO.setName( entity.getName() );
        materialDTO.setAvailableQuantity( entity.getAvailableQuantity() );
        materialDTO.setQuantityUnit( entity.getQuantityUnit() );
        materialDTO.setUuid( entity.getUuid() );
        materialDTO.setDescription( entity.getDescription() );
        materialDTO.setVersion( entity.getVersion() );
        materialDTO.setUpdated( abstractEntityMapper.toOffsetDateTime( entity.getUpdated() ) );
        materialDTO.setUpdatedBy( entity.getUpdatedBy() );

        return materialDTO;
    }

    @Override
    public MaterialEntity mapToEntity(MaterialDTO dto) {
        if ( dto == null ) {
            return null;
        }

        MaterialEntity materialEntity = new MaterialEntity();

        materialEntity.setUuid( dto.getUuid() );
        materialEntity.setName( dto.getName() );
        materialEntity.setDescription( dto.getDescription() );
        materialEntity.setAvailableQuantity( dto.getAvailableQuantity() );
        materialEntity.setQuantityUnit( dto.getQuantityUnit() );

        return materialEntity;
    }

    @Override
    public MaterialEntity updateEntityWithDtoInfo(MaterialEntity entity, MaterialDTO dto) {
        if ( dto == null ) {
            return entity;
        }

        entity.setDescription( dto.getDescription() );
        entity.setAvailableQuantity( dto.getAvailableQuantity() );
        entity.setQuantityUnit( dto.getQuantityUnit() );

        return entity;
    }
}
