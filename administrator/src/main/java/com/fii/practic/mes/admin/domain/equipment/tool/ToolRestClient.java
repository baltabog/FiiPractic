package com.fii.practic.mes.admin.domain.equipment.tool;

import com.fii.practic.mes.api.ToolApi;
import com.fii.practic.mes.admin.general.AbstractResource;
import com.fii.practic.mes.models.SearchType;
import com.fii.practic.mes.models.ToolDTO;
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
@Path("/administrator/equipments/tools")
@Consumes({"application/json"})
@Produces({"application/json"})
public class ToolRestClient extends AbstractResource implements ToolApi {

    private final ToolService service;
    private final HttpServerResponse response;

    @Inject
    public ToolRestClient(ToolService service, HttpServerResponse response) {
        this.service = service;
        this.response = response;
    }

    @Override
    @POST
    public ToolDTO createTool(@Valid @NotNull ToolDTO toolToolDTO) {
        return registerTimer("createTool")
                .record(() -> service.create(toolToolDTO));
    }

    @Override
    @POST
    @Path("/search")
    public List<ToolDTO> searchTools(@Valid SearchType searchType) {
        return registerTimer("searchTools")
                .record(() -> service.read(searchType, response));
    }

    @Override
    @PUT
    public ToolDTO updateTool(@Valid @NotNull ToolDTO toolToolDTO) {
        return registerTimer("updateTool")
                .record(() -> service.update(toolToolDTO));
    }

    @Override
    @DELETE
    public void deleteTools(@QueryParam("uuid") @NotNull @Size(min=1,max=36) String uuid,
                            @QueryParam("version") @NotNull Integer version) {
        registerTimer("deleteTools")
                .record(() -> service.delete(uuid, version));
    }


}
