package com.fii.practic.mes.api;

import com.fii.practic.mes.models.SearchType;
import com.fii.practic.mes.models.ToolDTO;

import jakarta.ws.rs.*;
import jakarta.ws.rs.core.Response;




import java.io.InputStream;
import java.util.Map;
import java.util.List;
import jakarta.validation.constraints.*;
import jakarta.validation.Valid;


@Path("/administrator/equipment/tools")
@jakarta.annotation.Generated(value = "org.openapitools.codegen.languages.JavaJAXRSSpecServerCodegen", date = "2025-04-07T11:57:29.761686900+03:00[Europe/Bucharest]", comments = "Generator version: 7.9.0")
public interface ToolApi {

    @POST
    @Consumes({ "application/json" })
    @Produces({ "application/json" })
    ToolDTO createTool(@Valid @NotNull ToolDTO toolDTO);

    @DELETE
    void deleteTools(@QueryParam("uuid") @NotNull @Size(min=1,max=36)   String uuid,@QueryParam("version") @NotNull   Integer version);

    @POST
    @Path("/search")
    @Consumes({ "application/json" })
    @Produces({ "application/json" })
    List<ToolDTO> searchTools(@Valid SearchType searchType);

    @PUT
    @Consumes({ "application/json" })
    @Produces({ "application/json" })
    ToolDTO updateTool(@Valid @NotNull ToolDTO toolDTO);
}
