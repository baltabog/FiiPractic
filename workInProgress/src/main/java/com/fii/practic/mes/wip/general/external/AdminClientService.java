package com.fii.practic.mes.wip.general.external;

import com.fii.practic.mes.admin.api.MaterialApi;
import com.fii.practic.mes.admin.api.OrderApi;
import com.fii.practic.mes.admin.api.ProcessPlanApi;
import com.fii.practic.mes.admin.api.ProcessStepApi;
import com.fii.practic.mes.admin.api.ToolApi;
import com.fii.practic.mes.admin.models.FilterParamCriteriaType;
import com.fii.practic.mes.admin.models.FilterParamType;
import com.fii.practic.mes.admin.models.IdentityDTO;
import com.fii.practic.mes.admin.models.SearchType;
import com.fii.practic.mes.wip.general.error.ApplicationRuntimeException;
import com.fii.practic.mes.wip.general.error.ServerErrorEnum;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import lombok.Getter;
import org.apache.commons.collections4.CollectionUtils;
import org.eclipse.microprofile.rest.client.inject.RestClient;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;

@ApplicationScoped
@Getter
public class AdminClientService {
    private final ToolApi adminEquipmentApi;
    private final MaterialApi adminMaterialApi;
    private final ProcessStepApi adminProcessStepApi;
    private final ProcessPlanApi adminProcessPlanApi;
    private final OrderApi adminOrderApi;

    @Inject
    public AdminClientService(@RestClient ToolApi equipmentApi,
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

    public SearchType getSearchTypeFromIdentity(IdentityDTO identity) {
        FilterParamType filterParamType = new FilterParamType();
        filterParamType.addCriteriaItem(getFilterCriteriaFromIdentity(identity));

        return new SearchType().filter(filterParamType);
    }

    public SearchType getSearchTypeFromIdentity(List<IdentityDTO> identities) {
        FilterParamType filterParamType = new FilterParamType();
        if (CollectionUtils.isNotEmpty(identities)) {
            if (identities.size() == 1) {
                filterParamType.addCriteriaItem(getFilterCriteriaFromIdentity(identities.get(0)));
            } else {
                Set<String> identityUuids = identities.stream()
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

    public <O> O getByIdentity(Function<SearchType, List<O>> searchFunction,
                                                    IdentityDTO identityDTO, Class<O> searchedObjectType) {

        List<O> objects = searchFunction.apply(getSearchTypeFromIdentity(identityDTO));
        if (CollectionUtils.size(objects) != 1) {
            throw new ApplicationRuntimeException(ServerErrorEnum.FIND_ERROR, searchedObjectType.getSimpleName());
        }

        return objects.get(0);
    }

    public <O> List<O> getByIdentity(Function<SearchType, List<O>> searchFunction,
                                                          List<IdentityDTO> identityDTOs, Class<O> searchedObjectType) {

        List<O> objects = searchFunction.apply(getSearchTypeFromIdentity(identityDTOs));
        if (CollectionUtils.size(objects) != CollectionUtils.size(identityDTOs)) {
            throw new ApplicationRuntimeException(ServerErrorEnum.FIND_ERROR, searchedObjectType.getSimpleName());
        }

        return objects;
    }

    public <O> List<O> getBySearchType(Function<SearchType, List<O>> searchFunction,
                                 SearchType searchType) {
        return searchFunction.apply(searchType);
    }
}
