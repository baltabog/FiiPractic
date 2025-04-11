package com.fii.practic.mes.wip.statistics;

import com.fii.practic.mes.api.TimeStatisticsApi;
import com.fii.practic.mes.models.TimeStatisticDTO;
import com.fii.practic.mes.wip.general.AbstractResource;
import jakarta.inject.Inject;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;

@Path("/workInProgress/statistics/time/orders/{name}")
public class StatisticsRestClient extends AbstractResource implements TimeStatisticsApi {
    private final OrderStatisticsService orderStatisticsService;

    @Inject
    public StatisticsRestClient(OrderStatisticsService orderStatisticsService) {
        this.orderStatisticsService = orderStatisticsService;
    }

    @Override
    public TimeStatisticDTO getEquipmentTimeStatistics(String equipmentId) {
        return null;
    }

    @Override
    @GET
    public TimeStatisticDTO getOrderTimeStatistics(String name) {
        return registerTimer("getOrderTimeStatistics")
                .record(() -> orderStatisticsService.getOrderStatistics(name));
    }
}
