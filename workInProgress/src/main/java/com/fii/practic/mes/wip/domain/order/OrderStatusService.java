package com.fii.practic.mes.wip.domain.order;

import com.fii.practic.mes.admin.api.ApiException;
import com.fii.practic.mes.admin.models.FilterParamCriteriaType;
import com.fii.practic.mes.admin.models.FilterParamType;
import com.fii.practic.mes.admin.models.IdentityDTO;
import com.fii.practic.mes.admin.models.OrderDTO;
import com.fii.practic.mes.admin.models.SearchType;
import com.fii.practic.mes.models.OrderStatusDTO;
import com.fii.practic.mes.models.OrderStatusType;
import com.fii.practic.mes.wip.general.error.ApplicationRuntimeException;
import com.fii.practic.mes.wip.general.error.ServerErrorEnum;
import com.fii.practic.mes.wip.general.external.AdminClientService;
import io.quarkus.narayana.jta.QuarkusTransaction;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.validation.constraints.NotNull;

import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.function.Function;

@ApplicationScoped
public class OrderStatusService {
    private final OrderStatusRepository repository;
    private final AdminClientService adminClientService;
    private final OrderStatusMapper mapper;

    @Inject
    public OrderStatusService(OrderStatusRepository repository,
                              AdminClientService adminClientService,
                              OrderStatusMapper mapper) {
        this.repository = repository;
        this.adminClientService = adminClientService;
        this.mapper = mapper;
    }

    public OrderStatusDTO changeOrderStatus(@NotNull String orderUuid, @NotNull OrderStatusType newStatus) {
        if (newStatus.equals(OrderStatusType.ON_HOLD)) {
            throw new ApplicationRuntimeException(ServerErrorEnum.NEW_ORDER_STATUS_NOT_PERMITTED, OrderStatusType.ON_HOLD.toString());
        }
        OrderDTO orderDTO = adminClientService.getByIdentity(
                getSearchOrdersFunction(),
                new IdentityDTO().uuid(orderUuid),
                OrderDTO.class);

        Optional<OrderStatusEntity> optionalOrderStatusEntity = repository.findByUuid(orderUuid);

        if (optionalOrderStatusEntity.isPresent()) {
            changeStatusOnExistingEntity(optionalOrderStatusEntity.get(), newStatus);
            return mapper.toOrderStatusDto(optionalOrderStatusEntity.get());
        } else {
            OrderStatusEntity newOrderStatusEntity = createNewOrderStatusEntity(orderDTO, newStatus);
            return mapper.toOrderStatusDto(newOrderStatusEntity);
        }
    }

    private OrderStatusEntity createNewOrderStatusEntity(OrderDTO orderDTO, OrderStatusType newStatus) {
        if (!newStatus.equals(OrderStatusType.STARTED)) {
            throw new ApplicationRuntimeException(ServerErrorEnum.WRONG_FIRST_ORDER_STATUS, orderDTO.getName());
        }
        checkStartedOrderExists();
        QuarkusTransaction.begin();

        OrderStatusEntity newOrderStatus = mapper.getNewOrderStatus(orderDTO, newStatus);
        repository.persist(newOrderStatus);

        QuarkusTransaction.commit();
        return newOrderStatus;
    }

    private void checkStartedOrderExists() {
        Optional<OrderStatusEntity> optionalStartedOrder = repository.stream("status", OrderStatusType.STARTED)
                .findAny();
        if (optionalStartedOrder.isPresent()) {
            throw new ApplicationRuntimeException(ServerErrorEnum.ORDER_ALREADY_STARTED, optionalStartedOrder.get().getOrderName());
        }
    }

    private void changeStatusOnExistingEntity(OrderStatusEntity orderStatusEntity, OrderStatusType newStatus) {
        checkStateTransition(orderStatusEntity.getStatus(), newStatus);
        QuarkusTransaction.begin();

        orderStatusEntity.setStatus(newStatus);
        repository.persist(orderStatusEntity);

        QuarkusTransaction.commit();
    }

    private void checkStateTransition(OrderStatusType oldStatus, OrderStatusType newStatus) {
        switch (oldStatus) {
            case STARTED -> {
                if ((Set.of(OrderStatusType.COMPLETED, OrderStatusType.PAUSED).contains(newStatus))) {
                    throw getStatusTransitionException(oldStatus, newStatus);
                }
            }
            case PAUSED -> {
                if (!newStatus.equals(OrderStatusType.STARTED)) {
                    throw getStatusTransitionException(oldStatus, newStatus);
                }
                checkStartedOrderExists();
            }
            case COMPLETED -> throw getStatusTransitionException(oldStatus, newStatus);
        }
    }

    private ApplicationRuntimeException getStatusTransitionException(OrderStatusType oldStatus, OrderStatusType newStatus) {
        return new ApplicationRuntimeException(ServerErrorEnum.ORDER_STATUS_TRANSITION_NOT_PERMITTED,
                oldStatus.toString(), newStatus.toString());
    }

    private Function<SearchType, List<OrderDTO>> getSearchOrdersFunction() {
        return (searchType) -> {
            try {
                return adminClientService.getAdminOrderApi().searchOrders(searchType);
            } catch (ApiException e) {
                throw new ApplicationRuntimeException(ServerErrorEnum.ADMIN_SERVICE_CALL_FAILED, e.getMessage());
            }
        };
    }

    public OrderStatusDTO getOrderStatusByName(String name) {
        Optional<OrderStatusEntity> optionalOrderStatus = repository.findOneIfExist("orderName", name);

        if (optionalOrderStatus.isPresent()) {
            return mapper.toOrderStatusDto(optionalOrderStatus.get());
        }
        List<OrderDTO> adminOrders = adminClientService.getBySearchType(getSearchOrdersFunction(),
                getSearchOrderByNameObject(name));

        if (adminOrders.isEmpty()) {
            throw new ApplicationRuntimeException(ServerErrorEnum.FIND_ERROR_NAMED, OrderStatusEntity.ENTITY_NAME, name);
        }
        return mapper.toOrderStatusDto(adminOrders.get(0), OrderStatusType.ON_HOLD);
    }

    private SearchType getSearchOrderByNameObject(String name) {
        return new SearchType().filter(
                new FilterParamType().addCriteriaItem(
                        new FilterParamCriteriaType()
                                .propertyName("name")
                                .operator(FilterParamCriteriaType.OperatorEnum.EQ)
                                .addValuesItem(name)
                ));
    }
}
