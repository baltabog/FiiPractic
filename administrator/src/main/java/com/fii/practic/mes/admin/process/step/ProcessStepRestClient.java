package com.fii.practic.mes.admin.process.step;

import com.fii.practic.mes.admin.general.AbstractResource;
import com.fii.practic.mes.api.ProcessStepApi;
import com.fii.practic.mes.models.ProcessStepDTO;
import com.fii.practic.mes.models.SearchType;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import org.eclipse.microprofile.rest.client.inject.RegisterRestClient;

import java.util.List;

@RegisterRestClient
@Path("/administrator/processes/steps")
@Consumes({"application/json"})
@Produces({"application/json"})
public class ProcessStepRestClient extends AbstractResource implements ProcessStepApi {

    @Override
    public ProcessStepDTO createProcessStep(ProcessStepDTO processStepDTO) {
        return null;
    }

    @Override
    public void deleteProcessStep(String uuid, Integer version) {

    }

    @Override
    public List<ProcessStepDTO> searchProcessSteps(SearchType searchType) {
        return null;
    }

    @Override
    public ProcessStepDTO updateProcessStep(ProcessStepDTO processStepDTO) {
        return null;
    }
}
