package com.fii.practic.mes.wip.domain.equipment;

import com.fii.practic.mes.admin.api.ApiException;
import com.fii.practic.mes.admin.models.FilterParamCriteriaType;
import com.fii.practic.mes.admin.models.FilterParamType;
import com.fii.practic.mes.admin.models.IdentityDTO;
import com.fii.practic.mes.admin.models.MaterialDTO;
import com.fii.practic.mes.admin.models.OrderDTO;
import com.fii.practic.mes.admin.models.ProcessPlanDTO;
import com.fii.practic.mes.admin.models.ProcessStepDTO;
import com.fii.practic.mes.admin.models.SearchType;
import com.fii.practic.mes.wip.general.error.ApplicationRuntimeException;
import com.fii.practic.mes.wip.general.error.ServerErrorEnum;
import com.fii.practic.mes.wip.general.external.AdminClientService;
import jakarta.enterprise.context.ApplicationScoped;
import org.apache.commons.collections4.CollectionUtils;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

@ApplicationScoped
public class ExternalInfoProvider {

    private final AdminClientService adminClientService;

    public ExternalInfoProvider(AdminClientService adminClientService) {
        this.adminClientService = adminClientService;
    }

    public Optional<ProcessStepDTO> getOrderProcessStepContainingEquipment(String orderUuid, String equipmentUuid) {
        SearchType searchType = new SearchType()
                .filter(new FilterParamType()
                        .addCriteriaItem(new FilterParamCriteriaType()
                                .propertyName("orderUuid")
                                .operator(FilterParamCriteriaType.OperatorEnum.EQ)
                                .values(List.of(orderUuid)))
                        .addCriteriaItem(new FilterParamCriteriaType()
                                .propertyName("withEquipment")
                                .operator(FilterParamCriteriaType.OperatorEnum.EQ)
                                .values(List.of(equipmentUuid)))
                );

        List<ProcessStepDTO> processStepDTOs = Collections.emptyList();
        try {
            processStepDTOs = adminClientService.getAdminProcessStepApi().searchProcessSteps(searchType);
        } catch (ApiException e) {
            throw new ApplicationRuntimeException(ServerErrorEnum.FIND_ERROR, "ProcessStepDTO");
        }

        if (CollectionUtils.size(processStepDTOs) != 1) {
            return Optional.empty();
        }

        return Optional.of(processStepDTOs.get(0));
    }

    public ProcessPlanDTO getOrderProcessPlan(String orderUuid) {
        SearchType searchType = new SearchType().filter(new FilterParamType()
                .addCriteriaItem(new FilterParamCriteriaType()
                        .propertyName("orderUuid")
                        .operator(FilterParamCriteriaType.OperatorEnum.EQ)
                        .values(List.of(orderUuid))));

        List<ProcessPlanDTO> processPlanDTOs = Collections.emptyList();
        try {
            processPlanDTOs = adminClientService.getAdminProcessPlanApi().searchProcesses(searchType);
        } catch (ApiException e) {
            throw new ApplicationRuntimeException(ServerErrorEnum.FIND_ERROR, "ProcessPlanDTO");
        }

        if (CollectionUtils.size(processPlanDTOs) != 1) {
            throw new ApplicationRuntimeException(ServerErrorEnum.FIND_ERROR, "ProcessPlanDTO");
        }

        return processPlanDTOs.get(0);
    }

    public MaterialDTO getMaterialInfo(String materialUuid) {
        return adminClientService.getByIdentity(
                searchType -> {
                    try {
                        return adminClientService.getAdminMaterialApi().searchMaterials(searchType);
                    } catch (ApiException e) {
                        throw new ApplicationRuntimeException(ServerErrorEnum.FIND_ERROR, "MaterialDTO");
                    }
                },
                new IdentityDTO().uuid(materialUuid), MaterialDTO.class);
    }

    public void updateMaterialQuantity(String materialUuid, Integer qtyDelta) {
        MaterialDTO materialDTO = getMaterialInfo(materialUuid);
        materialDTO.setAvailableQuantity(materialDTO.getAvailableQuantity() + qtyDelta);
        try {
            adminClientService.getAdminMaterialApi().updateMaterial(materialDTO);
        } catch (ApiException e) {
            throw new ApplicationRuntimeException(ServerErrorEnum.UPDATE_FIELD_NOT_ALLOWED, "MaterialDTO", "availableQuantity");
        }
    }

    public OrderDTO getOrderInfo(String orderUuid) {
        return adminClientService.getByIdentity(
                searchType -> {
                    try {
                        return adminClientService.getAdminOrderApi().searchOrders(searchType);
                    } catch (ApiException e) {
                        throw new ApplicationRuntimeException(ServerErrorEnum.FIND_ERROR, "OrderDTO");
                    }
                },
                new IdentityDTO().uuid(orderUuid), OrderDTO.class);
    }

    public void increaseOrderCompleteQty(String orderUuid) {
        OrderDTO orderDTO = getOrderInfo(orderUuid);
        orderDTO.setCompleteQty(orderDTO.getCompleteQty() + 1);
        try {
            adminClientService.getAdminOrderApi().updateOrder(orderDTO);
        } catch (ApiException e) {
            throw new ApplicationRuntimeException(ServerErrorEnum.UPDATE_FIELD_NOT_ALLOWED, "OrderDTO", "completeQty");
        }
    }
}
