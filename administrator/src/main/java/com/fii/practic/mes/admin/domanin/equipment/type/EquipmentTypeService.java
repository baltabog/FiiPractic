package com.fii.practic.mes.admin.domanin.equipment.type;

import com.fii.practic.mes.admin.general.AbstractCRUDService;
import com.fii.practic.mes.admin.general.AbstractRepository;
import com.fii.practic.mes.models.EquipmentTypeDTO;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

@ApplicationScoped
public class EquipmentTypeService extends AbstractCRUDService<EquipmentTypeDTO, EquipmentTypeEntity> {

    private final EquipmentTypeRepository repository;
    private final EquipmentTypeMapper mapper;

    @Inject
    public EquipmentTypeService(EquipmentTypeRepository repository, EquipmentTypeMapper mapper) {
        this.repository = repository;
        this.mapper = mapper;
    }

    @Override
    protected EquipmentTypeDTO mapToDto(EquipmentTypeEntity entity) {
        return mapper.mapToDto(entity);
    }

    @Override
    protected EquipmentTypeEntity mapToEntity(EquipmentTypeDTO dto) {
        return mapper.mapToEntity(dto);
    }

    @Override
    protected EquipmentTypeEntity mapToEntity(EquipmentTypeEntity entity, EquipmentTypeDTO dto) {
        return mapper.updateEntityWithDtoInfo(entity, dto);
    }

    @Override
    protected String getDtoName() {
        return "EquipmentTypeDTO";
    }

    @Override
    protected String getEntityName() {
        return EquipmentTypeEntity.ENTITY_NAME;
    }

    @Override
    protected AbstractRepository<EquipmentTypeEntity> getRepository() {
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
        return false;
    }

    @Override
    protected boolean isDeleteAllowed() {
        return true;
    }
}
