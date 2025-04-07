package com.fii.practic.mes.admin.domanin.material;

import com.fii.practic.mes.admin.general.AbstractResource;
import com.fii.practic.mes.api.MaterialApi;
import com.fii.practic.mes.models.MaterialDTO;
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
@Path("/administrator/materials")
@Consumes({"application/json"})
@Produces({"application/json"})
public class MaterialRestClient extends AbstractResource implements MaterialApi {
    private final MaterialService service;
    private final HttpServerResponse response;

    @Inject
    public MaterialRestClient(MaterialService service, HttpServerResponse response) {
        this.service = service;
        this.response = response;
    }

    @Override
    @POST
    public MaterialDTO createMaterial(@Valid @NotNull MaterialDTO materialDTO) {
        return registerTimer("createMaterial")
                .record(() -> service.create(materialDTO));
    }

    @Override
    @DELETE
    public void deleteMaterials(@QueryParam("uuid") @NotNull @Size(min=1,max=36) String uuid,
                                @QueryParam("version") @NotNull Integer version) {
        registerTimer("deleteMaterials")
                .record(() -> service.delete(uuid, version));

    }

    @Override
    @POST
    @Path("/search")
    public List<MaterialDTO> searchMaterials(@Valid SearchType searchType) {
        return registerTimer("searchMaterials")
                .record(() -> service.read(searchType, response));

    }

    @Override
    @PUT
    public MaterialDTO updateMaterial(@Valid @NotNull MaterialDTO materialDTO) {
        return registerTimer("updateMaterial")
                .record(() -> service.update(materialDTO));
    }
}
