package com.fii.practic.mes.admin.general.util;

import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

@Converter
public class NullableBooleanToStringConverter implements AttributeConverter<Boolean, String> {
    private static final String YES = "Y";
    private static final String NO = "N";

    @Override
    public String convertToDatabaseColumn(Boolean value) {
        if (value == null) {
            return null;
        }
        return value ? YES : NO;
    }

    @Override
    public Boolean convertToEntityAttribute(String value) {
        if (value == null) {
            return Boolean.FALSE;
        }
        return YES.equals(value);
    }
}
