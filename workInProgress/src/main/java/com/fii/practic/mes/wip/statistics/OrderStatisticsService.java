package com.fii.practic.mes.wip.statistics;

import com.fii.practic.mes.models.OrderStatusDTO;
import com.fii.practic.mes.models.OrderStatusType;
import com.fii.practic.mes.models.StatusTimeStatisticDTO;
import com.fii.practic.mes.models.TimeStatisticDTO;
import com.fii.practic.mes.wip.domain.order.OrderStatusService;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.validation.constraints.NotNull;

import java.time.Duration;
import java.time.OffsetDateTime;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@ApplicationScoped
public class OrderStatisticsService {
    private OrderStatusService orderStatusService;

    @Inject
    public OrderStatisticsService(OrderStatusService orderStatusService) {
        this.orderStatusService = orderStatusService;
    }

    public TimeStatisticDTO getOrderStatistics(@NotNull String orderName) {
        List<OrderStatusDTO> allOrderStatusByOrderName = orderStatusService.getAllOrderStatusByOrderName(orderName);

        TimeStatisticDTO timeStatisticDTO = new TimeStatisticDTO();
        timeStatisticDTO.setInterrogationItem(allOrderStatusByOrderName.get(0).getIdentity());

        List<StatusTimeStatisticDTO> statusTimeStatisticDTOS = allOrderStatusByOrderName.stream()
                .collect(Collectors.groupingBy(OrderStatusDTO::getStatus))
                .entrySet().stream()
                .map(this::getStatusTimeStatistics)
                .toList();
        timeStatisticDTO.setTimeStatistics(statusTimeStatisticDTOS);
        return timeStatisticDTO;
    }

    private StatusTimeStatisticDTO getStatusTimeStatistics(Map.Entry<OrderStatusType, List<OrderStatusDTO>> groupedOrderStatusListByStatus) {
        StatusTimeStatisticDTO statusTimeStatisticDTO = new StatusTimeStatisticDTO();
        statusTimeStatisticDTO.setStatus(groupedOrderStatusListByStatus.getKey().toString());

        long totalTime = 0L;
        long shortestPeriod = Long.MAX_VALUE;
        long longestPeriod = -1L;
        for (OrderStatusDTO orderStatusDTO : groupedOrderStatusListByStatus.getValue()) {
            Long statusDuration = orderStatusDTO.getDuration();
            if (statusDuration == null) {
                statusDuration = Duration.between(orderStatusDTO.getTimestamp(), OffsetDateTime.now()).getSeconds();
            }
            totalTime += statusDuration;
            if (statusDuration < shortestPeriod) {
                shortestPeriod = statusDuration;
            }
            if (statusDuration > longestPeriod) {
                longestPeriod = statusDuration;
            }
        }
        statusTimeStatisticDTO.setTotalTime(totalTime);
        statusTimeStatisticDTO.setShortestPeriod(shortestPeriod);
        statusTimeStatisticDTO.setLongestPeriod(longestPeriod);

        return statusTimeStatisticDTO;
    }

}
