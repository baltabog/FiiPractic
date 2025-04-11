package com.fii.practic.mes.wip.domain.equipment;

import com.fii.practic.mes.api.EquipmentApi;
import com.fii.practic.mes.models.UpdateEquipmentStatusRequest;
import com.fii.practic.mes.models.UpdateEquipmentStatusResponse;
import com.fii.practic.mes.wip.general.AbstractResource;
import jakarta.inject.Inject;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import org.eclipse.microprofile.rest.client.inject.RegisterRestClient;

@RegisterRestClient
@Path("/workInProgress/equipment/status")
@Consumes({"application/json"})
@Produces({"application/json"})
public class EquipmentStatusRestClient extends AbstractResource implements EquipmentApi {
    private final EquipmentStatusService service;

    @Inject
    public EquipmentStatusRestClient(EquipmentStatusService service) {
        this.service = service;
    }

    @Override
    public void checkChangeEqStatus(@Valid @NotNull UpdateEquipmentStatusRequest updateEquipmentStatusRequest) {
        registerTimer("checkChangeEqStatus")
                .record(() -> service.checkIfEquipmentStatusCanBeChanged(updateEquipmentStatusRequest));
    }

    @Override
    @POST
    public UpdateEquipmentStatusResponse changeEqStatus(@Valid @NotNull UpdateEquipmentStatusRequest updateEquipmentStatusRequest) {
        return registerTimer("changeEqStatus")
                .record(() -> service.changeEqStatus(updateEquipmentStatusRequest));
    }
}
