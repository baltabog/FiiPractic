package com.fii.practic.mes.admin.domanin.process.step;

import com.fii.practic.mes.admin.domanin.equipment.tool.ToolEntity;
import com.fii.practic.mes.admin.domanin.equipment.tool.ToolService;
import com.fii.practic.mes.admin.domanin.material.MaterialEntity;
import com.fii.practic.mes.admin.domanin.material.MaterialService;
import com.fii.practic.mes.admin.general.AbstractCRUDService;
import com.fii.practic.mes.admin.general.AbstractRepository;
import com.fii.practic.mes.admin.general.dto.CreateArtificialDto;
import com.fii.practic.mes.admin.general.dto.UpdateArtificialDto;
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
        ProcessStepEntity entity = super.updateEntityWithDto(dto, updateArtificialDto);

        updateEquipments(entity, dto);
        updateInputMaterials(entity, dto);
        updateSuccessOutputMaterials(entity, dto);
        updateFailOutputMaterials(entity, dto);

        return entity;
    }

    private void updateEquipments(ProcessStepEntity entity, ProcessStepDTO dto) {
        entity.getEquipments().removeIf(equipment -> equipmentNotFoundInDtoRefs(equipment, dto));

        for (IdentityDTO equipmentReference : dto.getEquipments()) {
            boolean refNotFound = entity.getEquipments().stream()
                    .noneMatch(equipment -> equipment.getUuid().equals(equipmentReference.getUuid()));
            if (refNotFound) {
                entity.getEquipments().add(
                        toolService.getByIdentity(equipmentReference));
            }
        }
    }

    private static boolean equipmentNotFoundInDtoRefs(ToolEntity equipment, ProcessStepDTO dto) {
        return dto.getEquipments().stream().map(IdentityDTO::getUuid)
                .noneMatch(uuid -> equipment.getUuid().equals(uuid));
    }

    private void updateInputMaterials(ProcessStepEntity entity, ProcessStepDTO dto) {
        List<ProcessStepInputMaterialEntity> inputMaterialEntities = new ArrayList<>(dto.getInputMaterials().size());
        for (ProcessStepMaterialDTO inputMaterialDto : dto.getInputMaterials()) {
            ProcessStepInputMaterialEntity processStepInputMaterialEntity = entity.getProcessStepInputMaterial().stream()
                    .filter(processStepInputMaterial -> processStepInputMaterial.getMaterial().getUuid().equals(inputMaterialDto.getUuid()))
                    .findFirst()
                    .orElseGet((() -> getNewProcessStepMaterial(entity, inputMaterialDto, ProcessStepInputMaterialEntity.class)));
            processStepInputMaterialEntity.setQuantity(inputMaterialDto.getQuantity());
            inputMaterialEntities.add(processStepInputMaterialEntity);
        }
        entity.getProcessStepInputMaterial().clear();
        entity.getProcessStepInputMaterial().addAll(inputMaterialEntities);
    }

    private void updateSuccessOutputMaterials(ProcessStepEntity entity, ProcessStepDTO dto) {
        List<ProcessStepMaterialSuccessEntity> materialSuccessEntities = new ArrayList<>(dto.getSuccessOutputMaterials().size());

        for (ProcessStepMaterialDTO successOutputMaterialDto : dto.getSuccessOutputMaterials()) {
            ProcessStepMaterialSuccessEntity processStepMaterialSuccessEntity = entity.getSuccessOutputMaterials().stream()
                    .filter(successMaterial -> successMaterial.getMaterial().getUuid().equals(successOutputMaterialDto.getUuid()))
                    .findFirst()
                    .orElseGet(() -> getNewProcessStepMaterial(entity, successOutputMaterialDto, ProcessStepMaterialSuccessEntity.class));
            processStepMaterialSuccessEntity.setQuantity(successOutputMaterialDto.getQuantity());
            materialSuccessEntities.add(processStepMaterialSuccessEntity);
        }

        entity.getSuccessOutputMaterials().clear();
        entity.getSuccessOutputMaterials().addAll(materialSuccessEntities);
    }
    private void updateFailOutputMaterials(ProcessStepEntity entity, ProcessStepDTO dto) {
        List<ProcessStepMaterialFailEntity> materialFailEntities = new ArrayList<>(dto.getFailOutputMaterials().size());

        for (ProcessStepMaterialDTO failOutputMaterialDto : dto.getFailOutputMaterials()) {
            ProcessStepMaterialFailEntity processStepMaterialFailEntity = entity.getFailOutputMaterials().stream()
                    .filter(failMaterial -> failMaterial.getMaterial().getUuid().equals(failOutputMaterialDto.getUuid()))
                    .findFirst()
                    .orElseGet(() -> getNewProcessStepMaterial(entity, failOutputMaterialDto, ProcessStepMaterialFailEntity.class));
            processStepMaterialFailEntity.setQuantity(failOutputMaterialDto.getQuantity());
            materialFailEntities.add(processStepMaterialFailEntity);
        }

        entity.getFailOutputMaterials().clear();
        entity.getFailOutputMaterials().addAll(materialFailEntities);
    }

    private <E extends  ProcessStepMaterialEntity> E getNewProcessStepMaterial(ProcessStepEntity entity,
                                                                               ProcessStepMaterialDTO processStepMaterialDTO,
                                                                               Class<E> materialEntityType) {
        ProcessStepMaterialEntity processStepMaterialEntity = new ProcessStepMaterialEntity();
        processStepMaterialEntity.setProcessStep(entity);
        processStepMaterialEntity.setMaterial(
                materialService.getByIdentity(new IdentityDTO()
                        .name(processStepMaterialDTO.getName())
                        .uuid(processStepMaterialDTO.getUuid()))
        );
        processStepMaterialEntity.setQuantity(processStepMaterialDTO.getQuantity());
        return materialEntityType.cast(processStepMaterialEntity);
    }
}
