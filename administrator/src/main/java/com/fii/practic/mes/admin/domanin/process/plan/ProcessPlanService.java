package com.fii.practic.mes.admin.domanin.process.plan;

import com.fii.practic.mes.admin.domanin.equipment.tool.ToolEntity;
import com.fii.practic.mes.admin.domanin.process.step.ProcessStepEntity;
import com.fii.practic.mes.admin.domanin.process.step.ProcessStepService;
import com.fii.practic.mes.admin.general.AbstractCRUDService;
import com.fii.practic.mes.admin.general.AbstractRepository;
import com.fii.practic.mes.admin.general.dto.CreateArtificialDto;
import com.fii.practic.mes.models.IdentityDTO;
import com.fii.practic.mes.models.OrderedProcessStepDTO;
import com.fii.practic.mes.models.ProcessPlanDTO;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import org.apache.commons.collections4.CollectionUtils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

@ApplicationScoped
public class ProcessPlanService extends AbstractCRUDService<ProcessPlanDTO, ProcessPlanEntity> {
    private final ProcessPlanRepository repository;
    private final ProcessPlanMapper mapper;
    private final ProcessStepService processStepService;

    @Inject
    public ProcessPlanService(ProcessPlanRepository repository, ProcessPlanMapper mapper, ProcessStepService processStepService) {
        this.repository = repository;
        this.mapper = mapper;
        this.processStepService = processStepService;
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
        Map<ProcessStepEntity, Set<ToolEntity>> equipmentsGroupedByProcessSteps = currentProcessPlan.getOrderedProcessSteps().stream()
                .map(ProcessPlanStepEntity::getProcessStep)
                .collect(Collectors.toMap(
                        processStep -> processStep,
                        ProcessStepEntity::getEquipments
                ));

        for (Set<ToolEntity> tools : equipmentsGroupedByProcessSteps.values()) {

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
}
