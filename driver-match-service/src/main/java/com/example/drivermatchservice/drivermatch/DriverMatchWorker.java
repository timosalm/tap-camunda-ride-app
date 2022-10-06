package com.example.drivermatchservice.drivermatch;

import io.camunda.zeebe.client.api.response.ActivatedJob;
import io.camunda.zeebe.client.api.worker.JobClient;
import io.camunda.zeebe.spring.client.annotation.ZeebeVariable;
import io.camunda.zeebe.spring.client.annotation.ZeebeWorker;
import io.camunda.zeebe.spring.client.exception.ZeebeBpmnError;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.*;

@Component
public class DriverMatchWorker {

    private static final Logger log = LoggerFactory.getLogger(DriverMatchWorker.class);

    @ZeebeWorker(type = "ChooseDriver", autoComplete = true, forceFetchAllVariables = true)
    public Map<String, String> driverMatchService(final JobClient client, final ActivatedJob job,
                                                  @ZeebeVariable Map<String, Object> driverMatchData) {
        log.info("Invoking driverMatchService with variables: " + job.getVariablesAsMap() + " " + driverMatchData);

        var location = (Map<String,Double>)driverMatchData.get("location");
        var match = getBestMatch((List<String>) driverMatchData.get("drivers"),
                new Location(location.get("latitude"), location.get("longitude")));
        return Map.of("matchedDriver", match);
    }

    private String getBestMatch(List<String> drivers, Location location) {
        Optional<String> match = drivers.stream().findFirst();
        if (match.isPresent()) {
            return match.get();
        }
        throw new ZeebeBpmnError("err_noDriver", "No match found1");
    }
}
