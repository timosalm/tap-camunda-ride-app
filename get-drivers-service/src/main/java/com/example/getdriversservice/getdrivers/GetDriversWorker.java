package com.example.getdriversservice.getdrivers;

import io.camunda.zeebe.client.api.response.ActivatedJob;
import io.camunda.zeebe.client.api.worker.JobClient;
import io.camunda.zeebe.spring.client.annotation.ZeebeVariablesAsType;
import io.camunda.zeebe.spring.client.annotation.ZeebeWorker;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.Collections;

@Component
public class GetDriversWorker {

    private static final Logger log = LoggerFactory.getLogger(GetDriversWorker.class);

    @ZeebeWorker(type = "CloseDrivers", autoComplete = true, forceFetchAllVariables = true)
    public VinList getDriversService(final JobClient client, final ActivatedJob job, @ZeebeVariablesAsType Location location) {
        log.info("Invoking getDriversService with variables: " + location + " " + job.getVariablesAsMap() + " " + job.getVariablesAsMap().get("location"));

        return new VinList(Collections.singletonList("1HMD11338H4E954D9"));
    }
}
