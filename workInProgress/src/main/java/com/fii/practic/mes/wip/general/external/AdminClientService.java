package com.fii.practic.mes.wip.general.external;

import com.fii.practic.mes.admin.api.MaterialApi;
import com.fii.practic.mes.admin.api.OrderApi;
import com.fii.practic.mes.admin.api.ProcessPlanApi;
import com.fii.practic.mes.admin.api.ProcessStepApi;
import com.fii.practic.mes.admin.models.FilterParamCriteriaType;
import com.fii.practic.mes.admin.models.FilterParamType;
import com.fii.practic.mes.admin.models.IdentityDTO;
import com.fii.practic.mes.admin.models.SearchType;
import com.fii.practic.mes.api.EquipmentApi;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import lombok.Getter;
import org.apache.commons.lang3.ArrayUtils;
import org.eclipse.microprofile.rest.client.inject.RestClient;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@ApplicationScoped
@Getter
public class AdminClientService {
    private final EquipmentApi adminEquipmentApi;
    private final MaterialApi adminMaterialApi;
    private final ProcessStepApi adminProcessStepApi;
    private final ProcessPlanApi adminProcessPlanApi;
    private final OrderApi adminOrderApi;

    @Inject
    public AdminClientService(@RestClient EquipmentApi equipmentApi,
                              @RestClient MaterialApi materialApi,
                              @RestClient ProcessStepApi processStepApi,
                              @RestClient ProcessPlanApi processPlanApi,
                              @RestClient OrderApi orderApi) {
        this.adminEquipmentApi = equipmentApi;
        this.adminMaterialApi = materialApi;
        this.adminProcessStepApi = processStepApi;
        this.adminProcessPlanApi = processPlanApi;
        this.adminOrderApi = orderApi;
    }

    public FilterParamCriteriaType getFilterCriteriaFromIdentity(IdentityDTO identityDTO) {
        return new FilterParamCriteriaType()
                .propertyName("uuid")
                .operator(FilterParamCriteriaType.OperatorEnum.EQ)
                .values(List.of(identityDTO.getUuid()));
    }

    public SearchType getSearchTypeFromIdentity(IdentityDTO ...identities) {
        FilterParamType filterParamType = new FilterParamType();
        if (ArrayUtils.isNotEmpty(identities)) {
            if (identities.length == 1) {
                filterParamType.addCriteriaItem(getFilterCriteriaFromIdentity(identities[0]));
            } else {
                Set<String> identityUuids = Arrays.stream(identities)
                        .map(IdentityDTO::getUuid)
                        .collect(Collectors.toSet());
                filterParamType.addCriteriaItem(new FilterParamCriteriaType()
                        .propertyName("uuid")
                        .operator(FilterParamCriteriaType.OperatorEnum.IN)
                        .values(new ArrayList<>(identityUuids)));
            }
        }

        return new SearchType().filter(filterParamType);
    }
}
