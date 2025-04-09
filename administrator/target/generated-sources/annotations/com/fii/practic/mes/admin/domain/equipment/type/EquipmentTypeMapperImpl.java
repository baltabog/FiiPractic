package com.fii.practic.mes.admin.domain.equipment.type;

import com.fii.practic.mes.models.EquipmentTypeDTO;
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
public class EquipmentTypeMapperImpl implements EquipmentTypeMapper {

    @Override
    public EquipmentTypeDTO mapToDto(EquipmentTypeEntity entity) {
        if ( entity == null ) {
            return null;
        }

        EquipmentTypeDTO equipmentTypeDTO = new EquipmentTypeDTO();

        equipmentTypeDTO.setName( entity.getName() );
        equipmentTypeDTO.setUuid( entity.getUuid() );
        equipmentTypeDTO.setDescription( entity.getDescription() );
        equipmentTypeDTO.setVersion( entity.getVersion() );
        equipmentTypeDTO.setUpdatedBy( entity.getUpdatedBy() );

        equipmentTypeDTO.setUpdated( entity.getUpdated().atOffset(java.time.OffsetDateTime.now().getOffset()) );

        return equipmentTypeDTO;
    }

    @Override
    public EquipmentTypeEntity mapToEntity(EquipmentTypeDTO dto) {
        if ( dto == null ) {
            return null;
        }

        EquipmentTypeEntity equipmentTypeEntity = new EquipmentTypeEntity();

        equipmentTypeEntity.setUuid( dto.getUuid() );
        equipmentTypeEntity.setName( dto.getName() );
        equipmentTypeEntity.setDescription( dto.getDescription() );

        return equipmentTypeEntity;
    }

    @Override
    public EquipmentTypeEntity updateEntityWithDtoInfo(EquipmentTypeEntity entity, EquipmentTypeDTO dto) {
        if ( dto == null ) {
            return entity;
        }

        entity.setDescription( dto.getDescription() );

        return entity;
    }
}
