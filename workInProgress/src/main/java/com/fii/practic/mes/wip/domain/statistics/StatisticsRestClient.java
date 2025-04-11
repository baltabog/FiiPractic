package com.fii.practic.mes.wip.domain.statistics;

import com.fii.practic.mes.api.TimeStatisticsApi;
import com.fii.practic.mes.models.TimeStatisticDTO;
import com.fii.practic.mes.wip.general.AbstractResource;
import jakarta.inject.Inject;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;

@Path("/workInProgress/statistics/time")
public class StatisticsRestClient extends AbstractResource implements TimeStatisticsApi {
    private final StatisticsService statisticsService;

    @Inject
    public StatisticsRestClient(StatisticsService statisticsService) {
        this.statisticsService = statisticsService;
    }

    @Override
    @GET
    @Path("/equipment/{equipmentName}")
    public TimeStatisticDTO getEquipmentTimeStatistics(String equipmentName) {
        return registerTimer("getEquipmentTimeStatistics")
                .record(() -> statisticsService.getEquipmentStatistics(equipmentName));
    }

    @Override
    @GET
    @Path("/orders/{name}")
    public TimeStatisticDTO getOrderTimeStatistics(String name) {
        return registerTimer("getOrderTimeStatistics")
                .record(() -> statisticsService.getOrderStatistics(name));
    }
}
