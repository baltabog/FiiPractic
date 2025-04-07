package com.fii.practic.mes.admin.domanin.process.plan;

import com.fii.practic.mes.admin.general.AbstractResource;
import com.fii.practic.mes.api.ProcessPlanApi;
import com.fii.practic.mes.models.ProcessPlanDTO;
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
@Path("/administrator/processes/plans")
@Consumes({"application/json"})
@Produces({"application/json"})
public class ProcessPlanRestClient extends AbstractResource implements ProcessPlanApi {
    private final ProcessPlanService service;
    private final HttpServerResponse response;

    @Inject
    public ProcessPlanRestClient(ProcessPlanService service, HttpServerResponse response) {
        this.service = service;
        this.response = response;
    }

    @Override
    @POST
    public ProcessPlanDTO createProcess(@Valid @NotNull ProcessPlanDTO processPlanDTO) {
        return registerTimer("createProcess")
                .record(() -> service.create(processPlanDTO));
    }

    @Override
    @DELETE
    public void deleteProcess(@QueryParam("uuid") @NotNull @Size(min=1,max=36) String uuid,
                              @QueryParam("version") @NotNull Integer version) {
        registerTimer("deleteProcess")
                .record(() -> service.delete(uuid, version));
    }

    @Override
    @POST
    @Path("/search")
    public List<ProcessPlanDTO> searchProcesses(@Valid SearchType searchType) {
        return registerTimer("searchProcesses")
                .record(() -> service.read(searchType, response));
    }

    @Override
    @PUT
    public ProcessPlanDTO updateProcess(@Valid @NotNull ProcessPlanDTO processPlanDTO) {
        return registerTimer("updateProcess")
                .record(() -> service.update(processPlanDTO));
    }
}
