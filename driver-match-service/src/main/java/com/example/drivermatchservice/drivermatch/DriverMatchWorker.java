package com.example.drivermatchservice.drivermatch;

import io.camunda.zeebe.client.api.response.ActivatedJob;
import io.camunda.zeebe.client.api.worker.JobClient;
import io.camunda.zeebe.spring.client.annotation.ZeebeVariablesAsType;
import io.camunda.zeebe.spring.client.annotation.ZeebeWorker;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.Collections;

@Component
public class DriverMatchWorker {

    private static final Logger log = LoggerFactory.getLogger(DriverMatchWorker.class);

    @ZeebeWorker(type = "ChooseDriver", autoComplete = true, forceFetchAllVariables = true)
    public DriverMatch driverMatchService(final JobClient client, final ActivatedJob job) {
        log.info("Invoking driverMatchService with variables: " + job.getVariablesAsMap());

        return new DriverMatch((String) job.getVariablesAsMap().get("userId"), "1HMD11338H4E954D9");
    }
}
