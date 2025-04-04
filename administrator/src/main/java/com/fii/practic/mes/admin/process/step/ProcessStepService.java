package com.fii.practic.mes.admin.process.step;

import com.fii.practic.mes.admin.equipment.tool.ToolEntity;
import com.fii.practic.mes.admin.equipment.tool.ToolService;
import com.fii.practic.mes.admin.general.AbstractCRUDService;
import com.fii.practic.mes.admin.general.AbstractRepository;
import com.fii.practic.mes.admin.general.dto.CreateArtificialDto;
import com.fii.practic.mes.models.IdentityDTO;
import com.fii.practic.mes.models.ProcessStepDTO;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import org.apache.commons.collections4.CollectionUtils;

import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

@ApplicationScoped
public class ProcessStepService extends AbstractCRUDService<ProcessStepDTO, ProcessStepEntity> {
    private final ProcessStepMapper mapper;
    private final ProcessStepRepository repository;
    private final ToolService toolService;

    @Inject
    public ProcessStepService(ProcessStepMapper mapper, ProcessStepRepository repository, ToolService toolService) {
        this.mapper = mapper;
        this.repository = repository;
        this.toolService = toolService;
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

        entity.getSuccessOutputMaterials()
                .forEach(outputMaterial -> {
                    outputMaterial.setProcessStep(entity);
                    outputMaterial.setUuid(UUID.randomUUID().toString());
                });
        entity.getFailOutputMaterials()
                .forEach(outputMaterial -> {
                    outputMaterial.setProcessStep(entity);
                    outputMaterial.setUuid(UUID.randomUUID().toString());
                });
        entity.getEquipments().addAll(getEquipmentEntities(dto.getEquipments()));
        return entity;
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
}
