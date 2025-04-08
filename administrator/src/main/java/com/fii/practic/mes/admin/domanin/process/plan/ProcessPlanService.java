package com.fii.practic.mes.admin.domanin.process.plan;

import com.fii.practic.mes.admin.domanin.equipment.tool.ToolEntity;
import com.fii.practic.mes.admin.domanin.process.step.ProcessStepEntity;
import com.fii.practic.mes.admin.domanin.process.step.ProcessStepService;
import com.fii.practic.mes.admin.general.AbstractCRUDService;
import com.fii.practic.mes.admin.general.AbstractRepository;
import com.fii.practic.mes.admin.general.dto.CreateArtificialDto;
import com.fii.practic.mes.admin.general.dto.UpdateArtificialDto;
import com.fii.practic.mes.admin.general.error.ApplicationRuntimeException;
import com.fii.practic.mes.admin.general.error.ServerErrorEnum;
import com.fii.practic.mes.models.IdentityDTO;
import com.fii.practic.mes.models.OrderedProcessStepDTO;
import com.fii.practic.mes.models.ProcessPlanDTO;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import org.apache.commons.collections4.CollectionUtils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@ApplicationScoped
public class ProcessPlanService extends AbstractCRUDService<ProcessPlanDTO, ProcessPlanEntity> {
    private final ProcessPlanRepository repository;
    private final ProcessPlanMapper mapper;
    private final ProcessStepService processStepService;
    private final ProcessPlanStepRepository processPlanStepRepository;

    @Inject
    public ProcessPlanService(ProcessPlanRepository repository, ProcessPlanMapper mapper,
                              ProcessStepService processStepService,
                              ProcessPlanStepRepository processPlanStepRepository) {
        this.repository = repository;
        this.mapper = mapper;
        this.processStepService = processStepService;
        this.processPlanStepRepository = processPlanStepRepository;
    }

    @Override
    protected ProcessPlanDTO mapToDto(ProcessPlanEntity entity) {
        return mapper.mapToDto(entity);
    }

    @Override
    protected ProcessPlanEntity mapToEntity(ProcessPlanDTO dto) {
        return mapper.mapToEntity(dto);
    }

    @Override
    protected ProcessPlanEntity mapToEntity(ProcessPlanEntity entity, ProcessPlanDTO dto) {
        return mapper.updateEntityWithDtoInfo(entity, dto);
    }

    @Override
    protected String getDtoName() {
        return "ProcessPlanDTO";
    }

    @Override
    protected String getEntityName() {
        return ProcessPlanEntity.ENTITY_NAME;
    }

    @Override
    protected AbstractRepository<ProcessPlanEntity> getRepository() {
        return repository;
    }

    @Override
    protected boolean isCreateAllowed() {
        return true;
    }

    @Override
    protected boolean isReadAllowed() {
        return true;
    }

    @Override
    protected boolean isUpdateAllowed() {
        return true;
    }

    @Override
    protected boolean isNameUpdateAllowed() {
        return true;
    }

    @Override
    protected boolean isDeleteAllowed() {
        return true;
    }

    @Override
    protected ProcessPlanEntity createEntityFromDto(ProcessPlanDTO dto, CreateArtificialDto createArtificialDto) {
        ProcessPlanEntity entity = super.createEntityFromDto(dto, createArtificialDto);

        entity.getOrderedProcessSteps().addAll(getOrderedProcessSteps(entity, dto.getOrderedProcessSteps()));
        checkNoEquipmentHasMultipleTasksPerPlan(entity);

        return entity;
    }

    private void checkNoEquipmentHasMultipleTasksPerPlan(ProcessPlanEntity currentProcessPlan) {
        List<ToolEntity> toolEntities = currentProcessPlan.getOrderedProcessSteps().stream()
                .map(ProcessPlanStepEntity::getProcessStep)
                .map(ProcessStepEntity::getEquipments)
                .flatMap(Set::stream)
                .toList();

        Set<ToolEntity> helperSet = new HashSet<>(toolEntities);
        if (toolEntities.size() != helperSet.size()) {
            throw new ApplicationRuntimeException(ServerErrorEnum.PROCESS_PLAN_HAS_AN_EQUIPMENT_EXECUTING_TWO_STEPS_OF_THE_SAME_PLAN);
        }
    }

    private List<ProcessPlanStepEntity> getOrderedProcessSteps(ProcessPlanEntity processPlan, Set<OrderedProcessStepDTO> orderedProcessStepsRefs) {
        if (CollectionUtils.isEmpty(orderedProcessStepsRefs)) {
            return Collections.emptyList();
        }
        List<ProcessPlanStepEntity> processPlanStepEntities = new ArrayList<>();
        for (OrderedProcessStepDTO orderedProcessStepsRef : orderedProcessStepsRefs) {
            IdentityDTO processStepIdentity = new IdentityDTO()
                    .name(orderedProcessStepsRef.getName())
                    .uuid(orderedProcessStepsRef.getUuid());
            ProcessStepEntity processStep = processStepService.getByIdentity(processStepIdentity);

            ProcessPlanStepEntity processPlanStep = new ProcessPlanStepEntity();
            processPlanStep.setProcessStep(processStep);
            processPlanStep.setProcessPlan(processPlan);
            processPlanStep.setOrderInProcess(orderedProcessStepsRef.getOrderInProcess());
            processPlanStepEntities.add(processPlanStep);
        }
        return processPlanStepEntities;
    }

    @Override
    protected ProcessPlanEntity updateEntityWithDto(ProcessPlanDTO dto, UpdateArtificialDto updateArtificialDto) {
        ProcessPlanEntity entity = super.updateEntityWithDto(dto, updateArtificialDto);
        updateProcessSteps(entity, dto.getOrderedProcessSteps());
        checkNoEquipmentHasMultipleTasksPerPlan(entity);
        return entity;
    }

    private void updateProcessSteps(ProcessPlanEntity entity, Set<OrderedProcessStepDTO> orderedProcessSteps) {
        entity.getOrderedProcessSteps().removeIf(
                orderedProcessStep -> orderedProcessSteps.stream()
                        .noneMatch(orderedProcessStepDTO
                                -> orderedProcessStepDTO.getUuid().equals(orderedProcessStep.getProcessStep().getUuid()))
        );

        List<ProcessPlanStepEntity> processPlanStepEntities = new ArrayList<>();
        for (OrderedProcessStepDTO orderedProcessStepDTO : orderedProcessSteps) {
            ProcessStepEntity processStepEntity = processStepService.getByIdentity(new IdentityDTO()
                    .uuid(orderedProcessStepDTO.getUuid())
                    .name(orderedProcessStepDTO.getName()));

            ProcessPlanStepEntity processPlanStepEntity = entity.getOrderedProcessSteps().stream()
                    .filter(orderedProcessStep -> orderedProcessStep.getProcessStep().getUuid().equals(processStepEntity.getUuid()))
                    .findFirst()
                    .orElseGet(() -> getNewOrderedProcessStep(entity, processStepEntity));
            processPlanStepEntity.setOrderInProcess(orderedProcessStepDTO.getOrderInProcess());
            processPlanStepEntities.add(processPlanStepEntity);
        }
        entity.getOrderedProcessSteps().clear();
        entity.getOrderedProcessSteps().addAll(processPlanStepEntities);
    }

    private ProcessPlanStepEntity getNewOrderedProcessStep(ProcessPlanEntity entity, ProcessStepEntity processStepEntity) {
        ProcessPlanStepEntity processPlanStepEntity = new ProcessPlanStepEntity();
        processPlanStepEntity.setProcessPlan(entity);
        processPlanStepEntity.setProcessStep(processStepEntity);
        return processPlanStepEntity;
    }

    public boolean existsProcessPlanWithProcessStep(ProcessStepEntity processStepEntity) {
        return processPlanStepRepository.stream("processStep.id", processStepEntity.getId())
                .findAny().isPresent();
    }
}
