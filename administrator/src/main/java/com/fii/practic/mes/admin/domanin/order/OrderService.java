package com.fii.practic.mes.admin.domanin.order;

import com.fii.practic.mes.admin.general.AbstractCRUDService;
import com.fii.practic.mes.admin.general.AbstractRepository;
import com.fii.practic.mes.admin.general.dto.CreateArtificialDto;
import com.fii.practic.mes.admin.general.dto.UpdateArtificialDto;
import com.fii.practic.mes.admin.domanin.process.plan.ProcessPlanService;
import com.fii.practic.mes.models.OrderDTO;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

@ApplicationScoped
public class OrderService extends AbstractCRUDService<OrderDTO, OrderEntity> {
    private final OrderRepository repository;
    private final OrderMapper mapper;
    private ProcessPlanService processPlanService;

    @Inject
    public OrderService(OrderRepository repository, OrderMapper mapper, ProcessPlanService processPlanService) {
        this.repository = repository;
        this.mapper = mapper;
        this.processPlanService = processPlanService;
    }

    @Override
    protected OrderDTO mapToDto(OrderEntity entity) {
        return mapper.mapToDto(entity);
    }

    @Override
    protected OrderEntity mapToEntity(OrderDTO dto) {
        return mapper.mapToEntity(dto);
    }

    @Override
    protected OrderEntity mapToEntity(OrderEntity entity, OrderDTO dto) {
        return mapper.updateEntityWithDtoInfo(entity, dto);
    }

    @Override
    protected String getDtoName() {
        return "OrderDTO";
    }

    @Override
    protected String getEntityName() {
        return OrderEntity.ENTITY_NAME;
    }

    @Override
    protected AbstractRepository<OrderEntity> getRepository() {
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

    @Override
    protected OrderEntity createEntityFromDto(OrderDTO dto, CreateArtificialDto createArtificialDto) {
        OrderEntity entity = super.createEntityFromDto(dto, createArtificialDto);

        entity.setProcessPlan(processPlanService.getByIdentity(dto.getProcess()));

        return entity;
    }

    @Override
    protected OrderEntity updateEntityWithDto(OrderDTO dto, UpdateArtificialDto updateArtificialDto) {
        OrderEntity entity = super.updateEntityWithDto(dto, updateArtificialDto);

        entity.setProcessPlan(processPlanService.getByIdentity(dto.getProcess()));

        return entity;
    }
}
