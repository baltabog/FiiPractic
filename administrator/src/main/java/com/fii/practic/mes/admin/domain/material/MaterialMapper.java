package com.fii.practic.mes.admin.domain.material;

import com.fii.practic.mes.admin.general.mapper.AbstractEntityMapper;
import com.fii.practic.mes.models.MaterialDTO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "jakarta",
        uses = AbstractEntityMapper.class)
public interface MaterialMapper {
    MaterialDTO mapToDto(MaterialEntity entity);

    @Mapping(target = "version",                ignore = true)
    @Mapping(target = "updated",	            ignore = true)
    @Mapping(target = "updatedBy",	            ignore = true)
    MaterialEntity mapToEntity(MaterialDTO dto);

    @Mapping(target = "id",                 ignore = true)
    @Mapping(target = "uuid",               ignore = true)
    @Mapping(target = "name",               ignore = true)
    @Mapping(target = "version",            ignore = true)
    @Mapping(target = "updated",	        ignore = true)
    @Mapping(target = "updatedBy",	        ignore = true)
    MaterialEntity updateEntityWithDtoInfo(@MappingTarget MaterialEntity entity, MaterialDTO dto);

}
