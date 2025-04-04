package com.fii.practic.mes.admin.general.mapper;

import com.fii.practic.mes.admin.general.AbstractEntity;
import com.fii.practic.mes.models.IdentityDTO;
import org.mapstruct.Mapper;

import java.time.LocalDateTime;
import java.time.OffsetDateTime;

@Mapper(componentModel = "jakarta")
public interface AbstractEntityMapper {
    IdentityDTO toIdentityDto(AbstractEntity entity);

    default OffsetDateTime toOffsetDateTime(LocalDateTime localDateTime) {
        return localDateTime.atOffset(java.time.OffsetDateTime.now().getOffset());
    }
}
