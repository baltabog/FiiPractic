package com.fii.practic.mes.wip.domain.equipment;

import com.fii.practic.mes.admin.api.ApiException;
import com.fii.practic.mes.admin.models.IdentityDTO;
import com.fii.practic.mes.admin.models.MaterialDTO;
import com.fii.practic.mes.admin.models.ProcessStepDTO;
import com.fii.practic.mes.wip.general.error.ApplicationRuntimeException;
import com.fii.practic.mes.wip.general.error.ServerErrorEnum;
import com.fii.practic.mes.wip.general.external.AdminClientService;
import jakarta.enterprise.context.ApplicationScoped;

import java.util.Optional;

@ApplicationScoped
public class ExternalInfoProvider {

    private final AdminClientService adminClientService;

    public ExternalInfoProvider(AdminClientService adminClientService) {
        this.adminClientService = adminClientService;
    }

    public Optional<ProcessStepDTO> getOrderProcessStepContainingEquipment(String orderUuid, String equipmentUuid) {


        return Optional.empty();
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
}
