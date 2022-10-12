package com.example.zeebeeventbridge.eventbridge;

import io.camunda.zeebe.client.ZeebeClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

import java.time.Duration;
import java.util.Collections;
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
            switch (event.getType()) {
                case BusinessEvent.RIDE_REQUESTED:
                    startProcessInstance((RideRequestData) event.getData());
                    break;
                case BusinessEvent.DRIVER_ACCEPTED:
                     var rideAcceptance = (RideAcceptance) event.getData();
                    publishMessage("msg_accept", rideAcceptance.getUserId().toString(),
                            Map.of("driverId", rideAcceptance.getDriver()));
                    break;
                case BusinessEvent.RIDER_PICKED_UP:
                    publishMessage("msg_met", ((RideProgressData) event.getData()).getUserId().toString(),
                            Map.of("userId", ((RideProgressData) event.getData()).getUserId().toString()));
                    break;
                case BusinessEvent.RIDE_FINISHED:
                    publishMessage("msg_rideFinished", ((RideProgressData) event.getData()).getUserId().toString(),
                            Map.of("userId", ((RideProgressData) event.getData()).getUserId().toString()));
                    break;
                default:
                    log.info("Unhandled event: " + event);
            }
        };
    }

    private void startProcessInstance(RideRequestData rideRequestData) {
        log.info("Starting process " + bpmnProcessId + " with variables: " + rideRequestData);

        var variables = Map.of("destination", rideRequestData.getTo(),
                "startingPoint", rideRequestData.getTo(), "userId", rideRequestData.getUserId());
        this.zeebeClient.newCreateInstanceCommand()
                .bpmnProcessId(bpmnProcessId)
                .latestVersion()
                .variables(variables)
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
