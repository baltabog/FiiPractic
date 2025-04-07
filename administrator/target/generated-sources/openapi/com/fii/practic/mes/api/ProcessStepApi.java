package com.fii.practic.mes.api;

import com.fii.practic.mes.models.ProcessStepDTO;
import com.fii.practic.mes.models.SearchType;

import jakarta.ws.rs.*;
import jakarta.ws.rs.core.Response;




import java.io.InputStream;
import java.util.Map;
import java.util.List;
import jakarta.validation.constraints.*;
import jakarta.validation.Valid;


@Path("/administrator/processes/steps")
@jakarta.annotation.Generated(value = "org.openapitools.codegen.languages.JavaJAXRSSpecServerCodegen", date = "2025-04-07T11:57:29.761686900+03:00[Europe/Bucharest]", comments = "Generator version: 7.9.0")
public interface ProcessStepApi {

    @POST
    @Consumes({ "application/json" })
    @Produces({ "application/json" })
    ProcessStepDTO createProcessStep(@Valid @NotNull ProcessStepDTO processStepDTO);

    @DELETE
    void deleteProcessStep(@QueryParam("uuid") @NotNull @Size(min=1,max=36)   String uuid,@QueryParam("version") @NotNull   Integer version);

    @POST
    @Path("/search")
    @Consumes({ "application/json" })
    @Produces({ "application/json" })
    List<ProcessStepDTO> searchProcessSteps(@Valid SearchType searchType);

    @PUT
    @Consumes({ "application/json" })
    @Produces({ "application/json" })
    ProcessStepDTO updateProcessStep(@Valid @NotNull ProcessStepDTO processStepDTO);
}
