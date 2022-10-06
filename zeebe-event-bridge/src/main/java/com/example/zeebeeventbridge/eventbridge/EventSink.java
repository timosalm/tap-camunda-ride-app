package com.example.zeebeeventbridge.eventbridge;

import io.camunda.zeebe.client.ZeebeClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

import java.time.Duration;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.function.Consumer;

@Component
public class EventSink {

    private static final Logger log = LoggerFactory.getLogger(EventSink.class);
    private final ZeebeClient zeebeClient;

    @Value("${zeebe-event-bridge.bpmn-process-id}")
    private String bpmnProcessId;

    public EventSink(ZeebeClient zeebeClient) {
        this.zeebeClient = zeebeClient;
    }

    @Bean
    public Consumer<BusinessEvent> handleEvent() {
        return event -> {
            log.info("handleEvent called for event: " + event);
            if (BusinessEvent.RIDE_REQUESTED.equals(event.getType())) {
                startProcessInstance((RideRequestData) event.getData());
            } else if (BusinessEvent.DRIVER_ACCEPTED.equals(event.getType())) {
                publishMessage("msg_accept", "af659827-db47-4111-a255-8a3516fa70a4",
                        Map.of("vin", "1HMD11338H4E954D9"));
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

    private void publishMessage(String messageName, String correlationKey, Object variables) {
        this.zeebeClient.newPublishMessageCommand()
                .messageName(messageName)
                .correlationKey(correlationKey)
                .variables(variables)
                .timeToLive(Duration.ofSeconds(15)).send();
    }
}
