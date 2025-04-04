package com.fii.practic.mes.admin.order;

import com.fii.practic.mes.admin.general.AbstractResource;
import com.fii.practic.mes.api.OrderApi;
import com.fii.practic.mes.models.OrderDTO;
import com.fii.practic.mes.models.SearchType;
import io.vertx.core.http.HttpServerResponse;
import jakarta.inject.Inject;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.DELETE;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.PUT;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.QueryParam;
import org.eclipse.microprofile.rest.client.inject.RegisterRestClient;

import java.util.List;

@RegisterRestClient
@Path("/administrator/orders")
@Consumes({"application/json"})
@Produces({"application/json"})
public class OrderRestClient extends AbstractResource implements OrderApi {
    private final OrderService service;
    private final HttpServerResponse response;

    @Inject
    public OrderRestClient(OrderService service, HttpServerResponse response) {
        this.service = service;
        this.response = response;
    }

    @Override
    @POST
    public OrderDTO createOrder(@Valid @NotNull OrderDTO orderDTO) {
        return registerTimer("createOrder")
                .record(() -> service.create(orderDTO));
    }

    @Override
    @DELETE
    public void deleteOrder(@QueryParam("uuid") @NotNull @Size(min=1,max=36) String uuid,
                            @QueryParam("version") @NotNull Integer version) {
        registerTimer("deleteOrder")
                .record(() -> service.delete(uuid, version));
    }

    @Override
    @POST
    @Path("/search")
    public List<OrderDTO> searchOrders(@Valid SearchType searchType) {
        return registerTimer("readOrders")
                .record(() -> service.read(searchType, response));
    }

    @Override
    @PUT
    public OrderDTO updateOrder(@Valid @NotNull OrderDTO orderDTO) {
        return registerTimer("updateOrder")
                .record(() -> service.update(orderDTO));
    }
}
