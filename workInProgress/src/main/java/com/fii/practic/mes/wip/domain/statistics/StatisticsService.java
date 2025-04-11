package com.fii.practic.mes.wip.domain.statistics;

import com.fii.practic.mes.admin.api.ApiException;
import com.fii.practic.mes.admin.models.OrderDTO;
import com.fii.practic.mes.models.IdentityDTO;
import com.fii.practic.mes.models.OrderStatusType;
import com.fii.practic.mes.models.StatusTimeStatisticDTO;
import com.fii.practic.mes.models.TimeStatisticDTO;
import com.fii.practic.mes.wip.domain.equipment.EquipmentStatusRepository;
import com.fii.practic.mes.wip.domain.order.OrderStatusEntity;
import com.fii.practic.mes.wip.domain.order.OrderStatusRepository;
import com.fii.practic.mes.wip.general.error.ApplicationRuntimeException;
import com.fii.practic.mes.wip.general.error.ServerErrorEnum;
import com.fii.practic.mes.wip.general.external.AdminClientService;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.validation.constraints.NotNull;
import org.apache.commons.collections4.CollectionUtils;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@ApplicationScoped
public class StatisticsService {
    private final OrderStatusRepository orderStatusRepository;
    private final EquipmentStatusRepository equipmentStatusRepository;
    private final AdminClientService adminClientService;

    @Inject
    public StatisticsService(OrderStatusRepository orderStatusRepository,
                             EquipmentStatusRepository equipmentStatusRepository,
                             AdminClientService adminClientService) {

        this.orderStatusRepository = orderStatusRepository;
        this.equipmentStatusRepository = equipmentStatusRepository;
        this.adminClientService = adminClientService;
    }

    /**
     * @param orderName - used to get order
     * @return order time statistics for ON_HOLD if order wasn't started or for the other order statuses if started
     */
    public TimeStatisticDTO getOrderStatistics(@NotNull String orderName) {
        TimeStatisticDTO timeStatisticDTO = new TimeStatisticDTO();

        List<OrderStatusEntity> orderStatusEntities = orderStatusRepository.list(
                "orderName = ?1 order by timestamp asc", orderName);
        if (CollectionUtils.isEmpty(orderStatusEntities)) {
            return getTimeStatisticForUnstartedOrder(orderName, timeStatisticDTO);
        }

        OrderStatusEntity orderStatusEntity = orderStatusEntities.get(0);
        timeStatisticDTO.setInterrogationItem(new IdentityDTO()
                .name(orderStatusEntity.getOrderName())
                .uuid(orderStatusEntity.getOrderUuid()));
        timeStatisticDTO.timeStatistics(getStatusTimeStatistics(orderStatusEntities));

        return timeStatisticDTO;
    }

    private TimeStatisticDTO getTimeStatisticForUnstartedOrder(String orderName, TimeStatisticDTO timeStatisticDTO) {
        OrderDTO optionalOrderDTO = adminClientService.getByName(
                searchType -> {
                    try {
                        return adminClientService.getAdminOrderApi().searchOrders(searchType);
                    } catch (ApiException e) {
                        throw new ApplicationRuntimeException(ServerErrorEnum.FIND_ERROR, "OrderDTO");
                    }
                },
                orderName, OrderDTO.class);

        timeStatisticDTO.setInterrogationItem(new IdentityDTO()
                .name(optionalOrderDTO.getName())
                .uuid(optionalOrderDTO.getUuid()));

        long duration = ChronoUnit.MILLIS.between(optionalOrderDTO.getUpdated(), LocalDateTime.now());
        timeStatisticDTO.setTimeStatistics(List.of(new StatusTimeStatisticDTO()
                .status(OrderStatusType.ON_HOLD.name())
                .totalTime(duration)
                .shortestPeriod(duration)
                .longestPeriod(duration)));

        return timeStatisticDTO;
    }

    private List<StatusTimeStatisticDTO> getStatusTimeStatistics(List<OrderStatusEntity> orderStatusEntities) {
        Map<OrderStatusType, StatusTimeStatisticDTO> statisticMap = Map.of(
                OrderStatusType.STARTED, getDefaultStatusTimeStatisticForStatus(OrderStatusType.STARTED.name()),
                OrderStatusType.PAUSED, getDefaultStatusTimeStatisticForStatus(OrderStatusType.PAUSED.name()),
                OrderStatusType.COMPLETED, getDefaultStatusTimeStatisticForStatus(OrderStatusType.COMPLETED.name())
        );

        for (int idx = 0; idx < orderStatusEntities.size() - 1; ++idx) {
            OrderStatusEntity currentEqStatusEntity = orderStatusEntities.get(idx);
            StatusTimeStatisticDTO statisticForCurrentEqStatus = statisticMap.get(currentEqStatusEntity.getStatus());
            long durationMs = ChronoUnit.MILLIS.between(currentEqStatusEntity.getTimestamp(),
                    orderStatusEntities.get(idx + 1).getTimestamp());

            statisticForCurrentEqStatus.setTotalTime(statisticForCurrentEqStatus.getTotalTime() + durationMs);
            statisticForCurrentEqStatus.setLongestPeriod(Math.max(statisticForCurrentEqStatus.getLongestPeriod(), durationMs));
            statisticForCurrentEqStatus.setShortestPeriod(
                    statisticForCurrentEqStatus.getShortestPeriod() == 0L ? durationMs
                            : Math.min(statisticForCurrentEqStatus.getShortestPeriod(), durationMs));
        }

        return new ArrayList<>(statisticMap.values());
    }

    /**
     * @param equipmentName - used to get equipment
     * @return equipment time statistics for each status
     */
    public TimeStatisticDTO getEquipmentStatistics(@NotNull String equipmentName) {
        // This will be implemented during FiiPractic 3rd meeting
        return new TimeStatisticDTO();
    }

    private StatusTimeStatisticDTO getDefaultStatusTimeStatisticForStatus(String status) {
        return new StatusTimeStatisticDTO()
                .status(status)
                .totalTime(0L)
                .longestPeriod(0L)
                .shortestPeriod(0L);
    }
}
