package com.fii.practic.mes.wip.domain.equipment;

import com.fii.practic.mes.admin.models.MaterialDTO;
import com.fii.practic.mes.admin.models.OrderedProcessStepDTO;
import com.fii.practic.mes.admin.models.ProcessPlanDTO;
import com.fii.practic.mes.admin.models.ProcessStepDTO;
import com.fii.practic.mes.admin.models.ProcessStepMaterialDTO;
import com.fii.practic.mes.models.EqStatusType;
import com.fii.practic.mes.models.IdentityDTO;
import com.fii.practic.mes.models.InputEqStatusType;
import com.fii.practic.mes.models.UpdateEquipmentStatusRequest;
import com.fii.practic.mes.models.UpdateEquipmentStatusResponse;
import com.fii.practic.mes.wip.domain.order.OrderStatusEntity;
import com.fii.practic.mes.wip.domain.order.OrderStatusRepository;
import com.fii.practic.mes.wip.general.error.ApplicationRuntimeException;
import com.fii.practic.mes.wip.general.error.ServerErrorEnum;
import io.quarkus.narayana.jta.QuarkusTransaction;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import org.apache.commons.collections4.CollectionUtils;

import java.time.OffsetDateTime;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;

@ApplicationScoped
public class EquipmentStatusService {
    private final EquipmentStatusRepository equipmentStatusRepository;
    private final OrderStatusRepository orderStatusRepository;
    private final ExternalInfoProvider externalInfoProvider;

    public EquipmentStatusService(EquipmentStatusRepository equipmentStatusRepository,
                                  OrderStatusRepository orderStatusRepository,
                                  ExternalInfoProvider externalInfoProvider) {
        this.equipmentStatusRepository = equipmentStatusRepository;
        this.orderStatusRepository = orderStatusRepository;
        this.externalInfoProvider = externalInfoProvider;
    }

    public void checkIfEquipmentStatusCanBeChanged(@Valid @NotNull UpdateEquipmentStatusRequest updateEquipmentStatusRequest) {
        QuarkusTransaction.begin();

        OrderStatusEntity activeOrder = orderStatusRepository.getActiveOrder()
                .orElseThrow(() -> new ApplicationRuntimeException(ServerErrorEnum.FIND_ERROR, OrderStatusEntity.ENTITY_NAME));
        ProcessStepDTO processStepDTO = getActiveOrderProcessStepContainingEquipment(activeOrder, updateEquipmentStatusRequest.getEquipment());

        EqStatusType equipmentActualStatus = equipmentStatusRepository.getEquipmentActiveStatus(
                updateEquipmentStatusRequest.getEquipment().getUuid());
        checkIfEquipmentCanHaveNewStatus(equipmentActualStatus, updateEquipmentStatusRequest.getInputStatus());

        if (InputEqStatusType.PROCESS_START.equals(updateEquipmentStatusRequest.getInputStatus())) {
            checkIfExistEnoughInputMaterial(processStepDTO.getInputMaterials());
        }

        QuarkusTransaction.commit();
    }

    public UpdateEquipmentStatusResponse changeEqStatus(@Valid @NotNull UpdateEquipmentStatusRequest updateEquipmentStatusRequest) {
        UpdateEquipmentStatusResponse response = new UpdateEquipmentStatusResponse()
                .equipment(updateEquipmentStatusRequest.getEquipment());

        QuarkusTransaction.begin();
        OrderStatusEntity activeOrder = orderStatusRepository.getActiveOrder()
                .orElseThrow(() -> new ApplicationRuntimeException(ServerErrorEnum.FIND_ERROR, OrderStatusEntity.ENTITY_NAME));
        ProcessStepDTO processStepDTO = getActiveOrderProcessStepContainingEquipment(activeOrder, updateEquipmentStatusRequest.getEquipment());

        EqStatusType equipmentActualStatus = equipmentStatusRepository.getEquipmentActiveStatus(
                updateEquipmentStatusRequest.getEquipment().getUuid());
        checkIfEquipmentCanHaveNewStatus(equipmentActualStatus, updateEquipmentStatusRequest.getInputStatus());
        if (InputEqStatusType.PROCESS_START.equals(updateEquipmentStatusRequest.getInputStatus())) {
            updateEquipmentInputMaterialQuantity(processStepDTO.getInputMaterials());
        } else if (InputEqStatusType.PROCESS_SUCCEED.equals(updateEquipmentStatusRequest.getInputStatus())) {
            updateEquipmentOutputMaterialQuantity(processStepDTO.getSuccessOutputMaterials());
            updateOrderCompleteQtyQuantityIfRequired(activeOrder, processStepDTO);
        } else if (InputEqStatusType.PROCESS_FAIL.equals(updateEquipmentStatusRequest.getInputStatus())) {
            updateEquipmentOutputMaterialQuantity(processStepDTO.getFailOutputMaterials());
        }

        EquipmentStatusEntity equipmentNewStatusEntity = saveNewEquipmentStatus(updateEquipmentStatusRequest, activeOrder);
        response.setOrder(new IdentityDTO().name(activeOrder.getOrderName()).uuid(activeOrder.getOrderUuid()));
        response.setEquipmentStatus(equipmentNewStatusEntity.getStatus());
        response.setLastChange(equipmentNewStatusEntity.getTimestamp().atOffset(OffsetDateTime.now().getOffset()));

        QuarkusTransaction.commit();

        return response;
    }

    private ProcessStepDTO getActiveOrderProcessStepContainingEquipment(OrderStatusEntity orderStatusEntity,
                                                                        IdentityDTO equipment) {
        Optional<ProcessStepDTO> optionalProcessStepDTO = externalInfoProvider.getOrderProcessStepContainingEquipment(
                orderStatusEntity.getOrderUuid(), equipment.getUuid());

        return optionalProcessStepDTO.orElseThrow(() -> new ApplicationRuntimeException(
                ServerErrorEnum.EQUIPMENT_IS_NOT_PART_OF_ACTIVE_ORDER, equipment.getName()));
    }

    private void checkIfEquipmentCanHaveNewStatus(EqStatusType currentStatus,
                                                  InputEqStatusType inputEquipmentStatus) {
        if (EqStatusType.STOPPED.equals(currentStatus)
                && InputEqStatusType.START.equals(inputEquipmentStatus)) {
            return;
        }

        if (EqStatusType.ON_REPAIR.equals(currentStatus)
                && List.of(InputEqStatusType.STOP, InputEqStatusType.START).contains(inputEquipmentStatus)) {
            return;
        }

        if (EqStatusType.WAIT_FOR_MATERIALS.equals(currentStatus)
                && InputEqStatusType.PROCESS_START.equals(inputEquipmentStatus)) {
            return;
        }

        if (EqStatusType.PROCESSING.equals(currentStatus)
                && List.of(InputEqStatusType.PROCESS_SUCCEED, InputEqStatusType.PROCESS_FAIL).contains(inputEquipmentStatus)) {
            return;
        }

        throw new ApplicationRuntimeException(ServerErrorEnum.EQUIPMENT_STATUS_INCOMPATIBLE_WITH_NEW_STATUS, inputEquipmentStatus.name());
    }

    private void checkIfExistEnoughInputMaterial(Set<ProcessStepMaterialDTO> checkedMaterials) {
        if (CollectionUtils.isEmpty(checkedMaterials)) {
            return;
        }

        for (ProcessStepMaterialDTO checkedMaterial : checkedMaterials) {
            MaterialDTO materialDTO = externalInfoProvider.getMaterialInfo(checkedMaterial.getUuid());

            if (materialDTO.getAvailableQuantity() < checkedMaterial.getQuantity()) {
                throw new ApplicationRuntimeException(
                        ServerErrorEnum.EQUIPMENT_PROCESS_CAN_NOT_BE_STARTED_INPUT_MATERIAL_QUANTITY_NOT_ENOUGH,
                        materialDTO.getName());
            }
        }
    }
    
    private void updateEquipmentInputMaterialQuantity(Set<ProcessStepMaterialDTO> updateMaterials) {
        if (CollectionUtils.isEmpty(updateMaterials)) {
            return;
        }

        for (ProcessStepMaterialDTO processStepMaterial : updateMaterials) {
            MaterialDTO materialDTO = externalInfoProvider.getMaterialInfo(processStepMaterial.getUuid());

            if (materialDTO.getAvailableQuantity() < processStepMaterial.getQuantity()) {
                throw new ApplicationRuntimeException(
                        ServerErrorEnum.EQUIPMENT_PROCESS_CAN_NOT_BE_STARTED_INPUT_MATERIAL_QUANTITY_NOT_ENOUGH,
                        materialDTO.getName());
            } else {
                externalInfoProvider.updateMaterialQuantity(processStepMaterial.getUuid(), -1 * processStepMaterial.getQuantity());
            }
        }
    }

    private void updateEquipmentOutputMaterialQuantity(Set<ProcessStepMaterialDTO> updateMaterials) {
        if (CollectionUtils.isEmpty(updateMaterials)) {
            return;
        }

        for (ProcessStepMaterialDTO processStepMaterial : updateMaterials) {
            externalInfoProvider.updateMaterialQuantity(processStepMaterial.getUuid(), processStepMaterial.getQuantity());
        }
    }

    private EquipmentStatusEntity saveNewEquipmentStatus(UpdateEquipmentStatusRequest updateEquipmentStatusRequest,
                                                         OrderStatusEntity activeOrder) {
        EquipmentStatusEntity newEquipmentStatus = new EquipmentStatusEntity();
        newEquipmentStatus.setEquipmentName(updateEquipmentStatusRequest.getEquipment().getName());
        newEquipmentStatus.setEquipmentUuid(updateEquipmentStatusRequest.getEquipment().getUuid());
        newEquipmentStatus.setOrderName(activeOrder.getOrderName());
        newEquipmentStatus.setOrderUuid(activeOrder.getOrderUuid());
        newEquipmentStatus.setStatus(mapInputEqStatusType(updateEquipmentStatusRequest.getInputStatus()));
        newEquipmentStatus.setUpdatedBy("system");

        equipmentStatusRepository.persistAndFlush(newEquipmentStatus);

        return newEquipmentStatus;
    }

    private EqStatusType mapInputEqStatusType(InputEqStatusType eqStatus) {
        return switch (eqStatus) {
            case STOP -> EqStatusType.STOPPED;
            case ON_REPAIR -> EqStatusType.ON_REPAIR;
            case PROCESS_START -> EqStatusType.PROCESSING;
            case START, PROCESS_SUCCEED, PROCESS_FAIL -> EqStatusType.WAIT_FOR_MATERIALS;
        };
    }

    private void updateOrderCompleteQtyQuantityIfRequired(OrderStatusEntity activeOrder, ProcessStepDTO processStepDTO) {
        ProcessPlanDTO orderProcessPlan = externalInfoProvider.getOrderProcessPlan(activeOrder.getOrderUuid());
        int psIndexInPp = -1;
        int maxPsIndexInPp = -1;
        for (OrderedProcessStepDTO processStep : orderProcessPlan.getOrderedProcessSteps()) {
            if (Objects.equals(processStep.getUuid(), processStepDTO.getUuid())) {
                psIndexInPp = processStep.getOrderInProcess();
            }
            maxPsIndexInPp = Math.max(maxPsIndexInPp, processStep.getOrderInProcess());
        }

        if (psIndexInPp == maxPsIndexInPp) {
            externalInfoProvider.increaseOrderCompleteQty(activeOrder.getOrderUuid());
        }
    }
}
