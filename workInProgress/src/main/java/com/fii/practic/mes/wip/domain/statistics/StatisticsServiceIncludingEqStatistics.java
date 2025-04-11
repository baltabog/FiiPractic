package com.fii.practic.mes.wip.domain.statistics;

import com.fii.practic.mes.admin.api.ApiException;
import com.fii.practic.mes.admin.models.FilterParamCriteriaType;
import com.fii.practic.mes.admin.models.FilterParamType;
import com.fii.practic.mes.admin.models.SearchType;
import com.fii.practic.mes.admin.models.ToolDTO;
import com.fii.practic.mes.models.EqStatusType;
import com.fii.practic.mes.models.IdentityDTO;
import com.fii.practic.mes.models.OrderStatusDTO;
import com.fii.practic.mes.models.OrderStatusType;
import com.fii.practic.mes.models.StatusTimeStatisticDTO;
import com.fii.practic.mes.models.TimeStatisticDTO;
import com.fii.practic.mes.wip.domain.equipment.EquipmentStatusEntity;
import com.fii.practic.mes.wip.domain.equipment.EquipmentStatusRepository;
import com.fii.practic.mes.wip.domain.order.OrderStatusService;
import com.fii.practic.mes.wip.general.error.ApplicationRuntimeException;
import com.fii.practic.mes.wip.general.error.ServerErrorEnum;
import com.fii.practic.mes.wip.general.external.AdminClientService;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.validation.constraints.NotNull;
import org.apache.commons.collections4.CollectionUtils;

import java.time.OffsetDateTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@ApplicationScoped
public class StatisticsServiceIncludingEqStatistics {
    private final OrderStatusService orderStatusService;
    private final EquipmentStatusRepository equipmentStatusRepository;
    private final AdminClientService adminClientService;

    @Inject
    public StatisticsServiceIncludingEqStatistics(OrderStatusService orderStatusService,
                                                  EquipmentStatusRepository equipmentStatusRepository,
                                                  AdminClientService adminClientService) {

        this.orderStatusService = orderStatusService;
        this.equipmentStatusRepository = equipmentStatusRepository;
        this.adminClientService = adminClientService;
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
                statusDuration = ChronoUnit.MILLIS.between(orderStatusDTO.getTimestamp(), OffsetDateTime.now());
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

    /**
     *
     * @param equipmentName - used to get equipment
     * @return equipment time statistics for each status
     */
    public TimeStatisticDTO getEquipmentStatistics(@NotNull String equipmentName) {
        TimeStatisticDTO timeStatisticDTO = new TimeStatisticDTO();

        List<EquipmentStatusEntity> equipmentStatuses = equipmentStatusRepository.list(
                "equipmentName = ?1 order by timestamp asc", equipmentName);
        if (CollectionUtils.isEmpty(equipmentStatuses)) {
            return getTimeStatisticForUnusedEquipmentDTO(equipmentName, timeStatisticDTO);
        }

        EquipmentStatusEntity equipmentStatusEntity = equipmentStatuses.get(0);
        timeStatisticDTO.setInterrogationItem(new IdentityDTO()
                .name(equipmentStatusEntity.getEquipmentName())
                .uuid(equipmentStatusEntity.getEquipmentUuid()));
        timeStatisticDTO.timeStatistics(getStatusTimeStatistics(equipmentStatuses));

        return timeStatisticDTO;
    }

    private ToolDTO getToolDtoFromAdmin(String toolName) {
        SearchType searchType = new SearchType()
                .filter(new FilterParamType()
                        .addCriteriaItem(new FilterParamCriteriaType()
                                .propertyName("name")
                                .operator(FilterParamCriteriaType.OperatorEnum.EQ)
                                .values(List.of(toolName))));
        try {
            List<ToolDTO> toolDTOs = adminClientService.getAdminEquipmentApi().searchTools(searchType);
            if (CollectionUtils.size(toolDTOs) != 1) {
                throw new ApplicationRuntimeException(ServerErrorEnum.FIND_ERROR, "ToolDTO");
            }

            return toolDTOs.get(0);
        } catch (ApiException e) {
            throw new ApplicationRuntimeException(ServerErrorEnum.FIND_ERROR, "ToolDTO");
        }
    }

    private TimeStatisticDTO getTimeStatisticForUnusedEquipmentDTO(String equipmentName, TimeStatisticDTO timeStatisticDTO) {
        ToolDTO optionalToolDTO = getToolDtoFromAdmin(equipmentName);
        timeStatisticDTO.setInterrogationItem(new IdentityDTO()
                .name(optionalToolDTO.getName())
                .uuid(optionalToolDTO.getUuid()));

        timeStatisticDTO.setTimeStatistics(List.of(new StatusTimeStatisticDTO()
                .status(EqStatusType.STOPPED.name())
                .totalTime(0L)
                .shortestPeriod(0L)
                .longestPeriod(0L)));

        return timeStatisticDTO;
    }

    private List<StatusTimeStatisticDTO> getStatusTimeStatistics(List<EquipmentStatusEntity> equipmentStatuses) {
        Map<EqStatusType, StatusTimeStatisticDTO> statisticMap = Map.of(
                EqStatusType.STOPPED, getDefaultStatusTimeStatisticForStatus(EqStatusType.STOPPED.name()),
                EqStatusType.ON_REPAIR, getDefaultStatusTimeStatisticForStatus(EqStatusType.ON_REPAIR.name()),
                EqStatusType.WAIT_FOR_MATERIALS, getDefaultStatusTimeStatisticForStatus(EqStatusType.WAIT_FOR_MATERIALS.name()),
                EqStatusType.PROCESSING, getDefaultStatusTimeStatisticForStatus(EqStatusType.PROCESSING.name())
        );

        for (int idx=0; idx<equipmentStatuses.size()-1; ++idx) {
            EquipmentStatusEntity currentEqStatusEntity = equipmentStatuses.get(idx);
            StatusTimeStatisticDTO statisticForCurrentEqStatus = statisticMap.get(currentEqStatusEntity.getStatus());
            long durationMs =  ChronoUnit.MILLIS.between(currentEqStatusEntity.getTimestamp(),
                    equipmentStatuses.get(idx+1).getTimestamp());

            statisticForCurrentEqStatus.setTotalTime(statisticForCurrentEqStatus.getTotalTime() + durationMs);
            statisticForCurrentEqStatus.setLongestPeriod(Math.max(statisticForCurrentEqStatus.getLongestPeriod(), durationMs));
            statisticForCurrentEqStatus.setShortestPeriod(
                    statisticForCurrentEqStatus.getShortestPeriod() == 0L ? durationMs
                            : Math.min(statisticForCurrentEqStatus.getShortestPeriod(), durationMs));
        }

        return new ArrayList<>(statisticMap.values());
    }
}
