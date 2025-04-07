package com.fii.practic.mes.admin.process.step;

import com.fii.practic.mes.admin.equipment.tool.ToolEntity;
import com.fii.practic.mes.admin.equipment.tool.ToolService;
import com.fii.practic.mes.admin.general.AbstractCRUDService;
import com.fii.practic.mes.admin.general.AbstractRepository;
import com.fii.practic.mes.admin.general.dto.CreateArtificialDto;
import com.fii.practic.mes.admin.general.dto.UpdateArtificialDto;
import com.fii.practic.mes.admin.material.MaterialEntity;
import com.fii.practic.mes.admin.material.MaterialService;
import com.fii.practic.mes.models.IdentityDTO;
import com.fii.practic.mes.models.ProcessStepDTO;
import com.fii.practic.mes.models.ProcessStepMaterialDTO;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import org.apache.commons.collections4.CollectionUtils;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@ApplicationScoped
public class ProcessStepService extends AbstractCRUDService<ProcessStepDTO, ProcessStepEntity> {
    private final ProcessStepMapper mapper;
    private final ProcessStepRepository repository;
    private final ToolService toolService;
    private final MaterialService materialService;

    @Inject
    public ProcessStepService(ProcessStepMapper mapper, ProcessStepRepository repository, ToolService toolService, MaterialService materialService) {
        this.mapper = mapper;
        this.repository = repository;
        this.toolService = toolService;
        this.materialService = materialService;
    }

    @Override
    protected ProcessStepDTO mapToDto(ProcessStepEntity entity) {
        return mapper.mapToDto(entity);
    }

    @Override
    protected ProcessStepEntity mapToEntity(ProcessStepDTO dto) {
        return mapper.mapToEntity(dto);
    }

    @Override
    protected ProcessStepEntity mapToEntity(ProcessStepEntity entity, ProcessStepDTO dto) {
        return mapper.updateEntityWithDtoInfo(entity, dto);
    }

    @Override
    protected String getDtoName() {
        return "ProcessStepDTO";
    }

    @Override
    protected String getEntityName() {
        return ProcessStepEntity.ENTITY_NAME;
    }

    @Override
    protected AbstractRepository<ProcessStepEntity> getRepository() {
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
    protected ProcessStepEntity createEntityFromDto(ProcessStepDTO dto, CreateArtificialDto createArtificialDto) {
        ProcessStepEntity entity = super.createEntityFromDto(dto, createArtificialDto);

        entity.getProcessStepInputMaterial().addAll(getStepMaterials(entity, dto.getInputMaterials(), ProcessStepInputMaterialEntity.class));
        entity.getSuccessOutputMaterials().addAll(getStepMaterials(entity, dto.getInputMaterials(), ProcessStepMaterialSuccessEntity.class));
        entity.getFailOutputMaterials().addAll(getStepMaterials(entity, dto.getInputMaterials(), ProcessStepMaterialFailEntity.class));
        entity.getEquipments().addAll(getEquipmentEntities(dto.getEquipments()));

        return entity;
    }

    private <E extends ProcessStepMaterialEntity> List<E> getStepMaterials(ProcessStepEntity processStepEntity,
                                                                           Set<ProcessStepMaterialDTO> materialReferences, Class<E> entityType) {
        if (CollectionUtils.isEmpty(materialReferences)) {
            return Collections.emptyList();
        }
        List<E> processStepMaterialEntities = new ArrayList<>();
        for (ProcessStepMaterialDTO materialReference : materialReferences) {
            MaterialEntity materialEntity = materialService.getByIdentity(new IdentityDTO()
                    .uuid(materialReference.getUuid())
                    .name(materialReference.getName()));
            ProcessStepMaterialEntity processStepMaterialEntity = new ProcessStepMaterialEntity();
            processStepMaterialEntity.setProcessStep(processStepEntity);
            processStepMaterialEntity.setMaterial(materialEntity);
            processStepMaterialEntity.setQuantity(materialReference.getQuantity());

            processStepMaterialEntities.add(entityType.cast(processStepMaterialEntity));
        }
        return processStepMaterialEntities;

    }

    private Collection<? extends ToolEntity> getEquipmentEntities(Set<IdentityDTO> equipmentReferences) {
        if (CollectionUtils.isEmpty(equipmentReferences)) {
            return Collections.emptyList();
        }
        Set<ToolEntity> equipmentEntities = new HashSet<>();
        equipmentReferences.stream()
                .map(toolService::getByIdentity)
                .forEach(equipmentEntities::add);

        return equipmentEntities;
    }

    @Override
    protected ProcessStepEntity updateEntityWithDto(ProcessStepDTO dto, UpdateArtificialDto updateArtificialDto) {
        ProcessStepEntity processStepEntity = super.updateEntityWithDto(dto, updateArtificialDto);
        return processStepEntity;
    }
}
