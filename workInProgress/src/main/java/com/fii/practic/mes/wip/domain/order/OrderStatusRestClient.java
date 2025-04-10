package com.fii.practic.mes.wip.domain.order;

import com.fii.practic.mes.api.OrderApi;
import com.fii.practic.mes.models.OrderStatusDTO;
import com.fii.practic.mes.models.OrderStatusType;
import com.fii.practic.mes.models.SearchType;
import com.fii.practic.mes.wip.general.AbstractResource;
import jakarta.inject.Inject;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Size;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;

import java.util.List;

@Path("/workInProgress/orders")
public class OrderStatusRestClient extends AbstractResource implements OrderApi {

    private final OrderStatusService service;

    @Inject
    public OrderStatusRestClient(OrderStatusService service) {
        this.service = service;
    }

    @Override
    @POST
    @Path("/{orderId}/status/{newOrderStatus}")
    public OrderStatusDTO changeOrderStatus(@PathParam("orderId") @Size(min=1,max=36) String orderId,
                                            @PathParam("newOrderStatus") OrderStatusType newOrderStatus) {
        return registerTimer("changeOrderStatus")
                .record(() -> service.changeOrderStatus(orderId, newOrderStatus));
    }

    @Override
    @POST
    @Path("/status/search")
    public List<OrderStatusDTO> searchOrders(@Valid SearchType searchType) {
        return null;
    }
}
