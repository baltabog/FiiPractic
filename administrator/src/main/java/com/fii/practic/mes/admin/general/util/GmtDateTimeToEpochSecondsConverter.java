package com.fii.practic.mes.admin.general.util;

import jakarta.persistence.AttributeConverter;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;

public class GmtDateTimeToEpochSecondsConverter implements AttributeConverter<LocalDateTime, Long> {
    @Override
    public Long convertToDatabaseColumn(LocalDateTime localDateTime) {
        if (localDateTime == null) {
            return null;
        }

        return localDateTime.atZone(ZoneId.systemDefault()).toInstant().getEpochSecond();
    }

    @Override
    public LocalDateTime convertToEntityAttribute(Long epochSeconds) {
        if (epochSeconds == null) {
            return null;
        }

        return LocalDateTime.ofInstant(Instant.ofEpochSecond(epochSeconds), ZoneId.systemDefault());
    }
}
