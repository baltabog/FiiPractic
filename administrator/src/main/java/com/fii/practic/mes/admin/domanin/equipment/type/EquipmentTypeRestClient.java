package com.fii.practic.mes.admin.domanin.equipment.type;

import com.fii.practic.mes.api.EquipmentTypeApi;
import com.fii.practic.mes.admin.general.AbstractResource;
import com.fii.practic.mes.models.EquipmentTypeDTO;
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
@Path("/administrator/equipments/types")
@Consumes({"application/json"})
@Produces({"application/json"})
public class EquipmentTypeRestClient extends AbstractResource implements EquipmentTypeApi {

    private final EquipmentTypeService service;
    private final HttpServerResponse response;

    @Inject
    public EquipmentTypeRestClient(EquipmentTypeService service,
                                   HttpServerResponse response) {
        this.service = service;
        this.response = response;
    }

    @Override
    @POST
    public EquipmentTypeDTO createEquipmentType(@Valid @NotNull EquipmentTypeDTO equipmentTypeDTO) {
        return registerTimer("createEquipmentType")
                .record(() -> service.create(equipmentTypeDTO));
    }

    @Override
    @POST
    @Path("/search")
    public List<EquipmentTypeDTO> searchEquipmentTypes(@Valid SearchType searchType) {
        return registerTimer("searchEquipmentTypes")
                .record(() -> service.read(searchType, response));
    }

    @Override
    @PUT
    public EquipmentTypeDTO updateEquipmentType(@Valid @NotNull EquipmentTypeDTO toolEquipmentTypeDTO) {
        return registerTimer("updateEquipmentType")
                .record(() -> service.update(toolEquipmentTypeDTO));
    }

    @Override
    @DELETE
    public void deleteEquipmentTypes(@QueryParam("uuid") @NotNull @Size(min=1,max=36) String uuid,
                                     @QueryParam("version") @NotNull Integer version) {
        registerTimer("deleteEquipmentTypes")
                .record(() -> service.delete(uuid, version));
    }


}
