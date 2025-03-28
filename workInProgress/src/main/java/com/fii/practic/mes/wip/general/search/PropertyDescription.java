package com.fii.practic.mes.wip.general.search;

import lombok.Getter;

import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;

@Getter
public class PropertyDescription {
    private final String dtoName;
    private final String hqlName;
    private final PropertyType hqlType;
    private final Function<String, Object> valueTransformer;
    private final boolean filterable;
    private final boolean sortable;

    public PropertyDescription(String name, PropertyType propertyType) {
        this(name, name, propertyType, null, true, true);
    }

    public PropertyDescription(String dtoName, String hqlName, PropertyType propertyType) {
        this(dtoName, hqlName, propertyType, null, true, true);
    }

    public PropertyDescription(String dtoName, String hqlName, PropertyType propertyType,
                               boolean filterable, boolean sortable) {
        this(dtoName, hqlName, propertyType, null, filterable, sortable);
    }

    public PropertyDescription(String dtoName, String hqlName, PropertyType hqlType,
                               Function<String, Object> valueTransformer, boolean filterable, boolean sortable) {

        this.dtoName = dtoName;
        this.hqlName = hqlName;
        this.hqlType = hqlType;
        this.valueTransformer = valueTransformer;
        this.filterable = filterable;
        this.sortable = sortable;
    }

    public Object getValue(String searchValue) {
        return switch (hqlType) {
            case BOOLEAN -> Boolean.valueOf(searchValue);
            case SHORT -> Short.valueOf(searchValue);
            case INTEGER -> Integer.valueOf(searchValue);
            case LONG -> Long.valueOf(searchValue);
            case DOUBLE -> Double.valueOf(searchValue);
            case STRING -> searchValue;
            case DATE -> OffsetDateTime.parse(searchValue).toLocalDateTime();
            default -> valueTransformer.apply(searchValue);
        };
    }

    public List<Object> getValues(List<String> searchValue) {
        List<Object> list = new ArrayList<>();
        for (String s : searchValue) {
            Object value = getValue(s);
            list.add(value);
        }

        return list;
    }
}
