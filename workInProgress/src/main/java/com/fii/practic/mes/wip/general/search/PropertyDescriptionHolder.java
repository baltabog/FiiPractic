package com.fii.practic.mes.wip.general.search;

import org.apache.commons.collections4.MapUtils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public final class PropertyDescriptionHolder {

    private static final Map<String, List<PropertyDescription>> PROPERTY_DESCRIPTION_MAP = new HashMap<>();

    private PropertyDescriptionHolder() {
        // not requested
    }

    public static List<PropertyDescription> getPropertyDescriptions(String referredDto) {
        if (MapUtils.isEmpty(PROPERTY_DESCRIPTION_MAP)) {
            new PropertyDescriptionHolder().registerProperties();
        }

        return PROPERTY_DESCRIPTION_MAP.getOrDefault(referredDto, Collections.emptyList());
    }

    private void registerProperties() {
        registerEquipmentTypeDTO();
        registerToolDto();
    }

    private void registerEquipmentTypeDTO() {
        PROPERTY_DESCRIPTION_MAP.put("EquipmentTypeDTO", getDefaultListOfProperties());
    }

    private void registerToolDto() {
        List<PropertyDescription> list = new ArrayList<>(getDefaultListOfProperties());
        list.add(new PropertyDescription("alias", "alias", PropertyType.STRING));
        list.add(new PropertyDescription("active", "active", PropertyType.BOOLEAN));

        PROPERTY_DESCRIPTION_MAP.put("ToolDTO", list);
    }

    private List<PropertyDescription> getDefaultListOfProperties() {
        return List.of(
                new PropertyDescription("uuid", PropertyType.STRING),
                new PropertyDescription("name", PropertyType.STRING),
                new PropertyDescription("updated", PropertyType.DATE),
                new PropertyDescription("description", PropertyType.STRING),
                new PropertyDescription("updatedBy", PropertyType.STRING),
                new PropertyDescription("division", PropertyType.STRING)
        );
    }
}
