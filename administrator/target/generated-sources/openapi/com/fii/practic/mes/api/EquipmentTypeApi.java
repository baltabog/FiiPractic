package com.fii.practic.mes.api;

import com.fii.practic.mes.models.EquipmentTypeDTO;
import com.fii.practic.mes.models.SearchType;

import jakarta.ws.rs.*;
import jakarta.ws.rs.core.Response;




import java.io.InputStream;
import java.util.Map;
import java.util.List;
import jakarta.validation.constraints.*;
import jakarta.validation.Valid;


@Path("/administrator/equipment/types")
@jakarta.annotation.Generated(value = "org.openapitools.codegen.languages.JavaJAXRSSpecServerCodegen", date = "2025-04-07T11:57:29.761686900+03:00[Europe/Bucharest]", comments = "Generator version: 7.9.0")
public interface EquipmentTypeApi {

    @POST
    @Consumes({ "application/json" })
    @Produces({ "application/json" })
    EquipmentTypeDTO createEquipmentType(@Valid @NotNull EquipmentTypeDTO equipmentTypeDTO);

    @DELETE
    void deleteEquipmentTypes(@QueryParam("uuid") @NotNull @Size(min=1,max=36)   String uuid,@QueryParam("version") @NotNull   Integer version);

    @POST
    @Path("/search")
    @Consumes({ "application/json" })
    @Produces({ "application/json" })
    List<EquipmentTypeDTO> searchEquipmentTypes(@Valid SearchType searchType);

    @PUT
    @Consumes({ "application/json" })
    @Produces({ "application/json" })
    EquipmentTypeDTO updateEquipmentType(@Valid @NotNull EquipmentTypeDTO equipmentTypeDTO);
}
