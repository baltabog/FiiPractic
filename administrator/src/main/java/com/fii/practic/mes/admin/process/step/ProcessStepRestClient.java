package com.fii.practic.mes.admin.process.step;

import com.fii.practic.mes.admin.general.AbstractResource;
import com.fii.practic.mes.api.ProcessStepApi;
import com.fii.practic.mes.models.ProcessStepDTO;
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
@Path("/administrator/processes/steps")
@Consumes({"application/json"})
@Produces({"application/json"})
public class ProcessStepRestClient extends AbstractResource implements ProcessStepApi {
    private final ProcessStepService service;
    private final HttpServerResponse response;

    @Inject
    public ProcessStepRestClient(ProcessStepService service, HttpServerResponse response) {
        this.service = service;
        this.response = response;
    }

    @Override
    @POST
    public ProcessStepDTO createProcessStep(@Valid @NotNull ProcessStepDTO processStepDTO) {
        return registerTimer("createProcessStep")
                .record(() -> service.create(processStepDTO));
    }

    @Override
    @DELETE
    public void deleteProcessStep(@QueryParam("uuid") @NotNull @Size(min=1,max=36) String uuid,
                                  @QueryParam("version") @NotNull Integer version) {
        registerTimer("deleteProcessStep")
                .record(() -> service.delete(uuid, version));
    }

    @Override
    @POST
    @Path("/search")
    public List<ProcessStepDTO> searchProcessSteps(@Valid SearchType searchType) {
        return registerTimer("searchProcessSteps")
                .record(() -> service.read(searchType, response));
    }

    @Override
    @PUT
    public ProcessStepDTO updateProcessStep(@Valid @NotNull ProcessStepDTO processStepDTO) {
        return registerTimer("updateProcessStep")
                .record(() -> service.update(processStepDTO));
    }
}
