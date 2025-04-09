package com.fii.practic.mes.admin.domain.material;

import com.fii.practic.mes.admin.general.AbstractCRUDService;
import com.fii.practic.mes.admin.general.AbstractRepository;
import com.fii.practic.mes.models.MaterialDTO;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

@ApplicationScoped
public class MaterialService extends AbstractCRUDService<MaterialDTO, MaterialEntity> {
    private final MaterialRepository repository;
    private final MaterialMapper mapper;

    @Inject
    public MaterialService(MaterialRepository repository, MaterialMapper mapper) {
        this.repository = repository;
        this.mapper = mapper;
    }

    @Override
    protected MaterialDTO mapToDto(MaterialEntity entity) {
        return mapper.mapToDto(entity);
    }

    @Override
    protected MaterialEntity mapToEntity(MaterialDTO dto) {
        return mapper.mapToEntity(dto);
    }

    @Override
    protected MaterialEntity mapToEntity(MaterialEntity entity, MaterialDTO dto) {
        return mapper.updateEntityWithDtoInfo(entity, dto);
    }

    @Override
    protected String getDtoName() {
        return "MaterialDTO";
    }

    @Override
    protected String getEntityName() {
        return MaterialEntity.ENTITY_NAME;
    }

    @Override
    protected AbstractRepository<MaterialEntity> getRepository() {
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
}
