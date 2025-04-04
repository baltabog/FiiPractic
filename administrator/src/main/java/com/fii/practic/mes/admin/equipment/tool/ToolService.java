package com.fii.practic.mes.admin.equipment.tool;

import com.fii.practic.mes.admin.equipment.type.EquipmentTypeService;
import com.fii.practic.mes.admin.general.AbstractCRUDService;
import com.fii.practic.mes.admin.general.AbstractRepository;
import com.fii.practic.mes.admin.general.dto.CreateArtificialDto;
import com.fii.practic.mes.admin.general.dto.UpdateArtificialDto;
import com.fii.practic.mes.admin.general.error.ApplicationRuntimeException;
import com.fii.practic.mes.admin.general.error.ServerErrorEnum;
import com.fii.practic.mes.models.ToolDTO;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

import java.util.Objects;

@ApplicationScoped
public class ToolService extends AbstractCRUDService<ToolDTO, ToolEntity> {

    private final ToolRepository repository;
    private final ToolMapper mapper;
    private final EquipmentTypeService equipmentTypeService;

    @Inject
    public ToolService(ToolRepository repository,
                       ToolMapper mapper,
                       EquipmentTypeService equipmentTypeService) {
        this.repository = repository;
        this.mapper = mapper;
        this.equipmentTypeService = equipmentTypeService;
    }

    @Override
    protected ToolDTO mapToDto(ToolEntity entity) {
        return mapper.mapToDto(entity);
    }

    @Override
    protected ToolEntity mapToEntity(ToolDTO dto) {
        return mapper.mapToEntity(dto);
    }

    @Override
    protected ToolEntity mapToEntity(ToolEntity entity, ToolDTO dto) {
        return mapper.updateEntityWithDtoInfo(entity, dto);
    }

    @Override
    protected String getDtoName() {
        return "ToolDTO";
    }

    @Override
    protected String getEntityName() {
        return ToolEntity.ENTITY_NAME;
    }

    @Override
    protected AbstractRepository<ToolEntity> getRepository() {
        return repository;
    }

    @Override
    protected boolean isCreateAllowed() {
        return true;
    }

    @Override
    protected ToolEntity createEntityFromDto(ToolDTO dto, CreateArtificialDto createArtificialDto) {
        ToolEntity entity = super.createEntityFromDto(dto, createArtificialDto);

        entity.setEquipmentType(equipmentTypeService.getByIdentity(dto.getEquipmentTypeIdentity()));

        return entity;
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
    protected ToolEntity updateEntityWithDto(ToolDTO dto, UpdateArtificialDto updateArtificialDto) {
        ToolEntity entity = super.updateEntityWithDto(dto, updateArtificialDto);

        if (!Objects.equals(entity.getEquipmentType().getUuid(), dto.getEquipmentTypeIdentity().getUuid())) {
            throw new ApplicationRuntimeException(ServerErrorEnum.UPDATE_FIELD_NOT_ALLOWED, getDtoName(), "equipmentType");
        }

        return entity;
    }

    @Override
    protected boolean isNameUpdateAllowed() {
        return false;
    }

    @Override
    protected boolean isDeleteAllowed() {
        return true;
    }
}
