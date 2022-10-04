package com.example.zeebeeventbridge.eventbridge;

import com.fasterxml.jackson.core.JsonProcessingException;
import io.camunda.zeebe.client.ZeebeClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

import java.util.function.Consumer;

@Component
public class EventSink {

    private static final Logger log = LoggerFactory.getLogger(EventSink.class);
    private final ZeebeClient zeebeClient;

    @Value("${(zeebe-event-bridge.bpmn-process-id}")
    private String bpmnProcessId;

    public EventSink(ZeebeClient zeebeClient) {
        this.zeebeClient = zeebeClient;
    }

    @Bean
    public Consumer<BusinessEvent> handleEvent() {
        return event -> {
            log.info("handleEvent called for event: " + event);
            if (BusinessEvent.RIDE_REQUESTED.equals(event.type())) {
                try {
                    startProcessInstance(event.getDataAsObject(RideRequestData.class));
                } catch (JsonProcessingException e) {
                    log.error("Unable to convert business event data from JSON to RideRequestData" + e.getMessage());
                }
            }
        };
    }

    private void startProcessInstance(RideRequestData rideRequestData) {
        log.info("Starting process " + bpmnProcessId + " with variables: " + rideRequestData);
        this.zeebeClient.newCreateInstanceCommand()
                .bpmnProcessId(bpmnProcessId)
                .latestVersion()
                .variables(rideRequestData)
                .send();
    }
}
