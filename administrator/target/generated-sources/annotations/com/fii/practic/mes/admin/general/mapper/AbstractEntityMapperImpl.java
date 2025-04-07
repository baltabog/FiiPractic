package com.fii.practic.mes.admin.general.mapper;

import com.fii.practic.mes.admin.general.AbstractEntity;
import com.fii.practic.mes.models.IdentityDTO;
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
public class AbstractEntityMapperImpl implements AbstractEntityMapper {

    @Override
    public IdentityDTO toIdentityDto(AbstractEntity entity) {
        if ( entity == null ) {
            return null;
        }

        IdentityDTO identityDTO = new IdentityDTO();

        identityDTO.setUuid( entity.getUuid() );
        identityDTO.setName( entity.getName() );

        return identityDTO;
    }
}
