package com.example.rideservice;

import com.example.rideservice.ride.RideApplicationService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.socket.WebSocketHandler;
import org.springframework.web.reactive.socket.WebSocketMessage;
import org.springframework.web.reactive.socket.WebSocketSession;
import reactor.core.Exceptions;
import reactor.core.publisher.Mono;
import reactor.core.publisher.Sinks;

@Component
public class RideServiceWebSocketHandler implements WebSocketHandler {

    private static final Logger log = LoggerFactory.getLogger(RideServiceWebSocketHandler.class);

    private final Sinks.Many<BusinessEvent> sink;
    private final RideApplicationService rideApplicationService;

    public RideServiceWebSocketHandler(RideApplicationService rideApplicationService) {
        this.rideApplicationService = rideApplicationService;
        this.sink = Sinks.many().multicast().directBestEffort();
    }

    @Override
    public Mono<Void> handle(WebSocketSession session) {
        var objectMapper = new ObjectMapper();
        var input = session.receive()
                .map(WebSocketMessage::getPayloadAsText)
                .doOnNext(in -> {
                    BusinessEvent event;
                    try {
                        event = objectMapper.readValue(in, BusinessEvent.class);
                    } catch (JsonProcessingException e) {
                        log.error("Unable to convert business event from client to JSON: " + e.getMessage());
                        return;
                    }
                    log.info("Received new business event from client: " + event.toString());
                    this.rideApplicationService.handleEvent(event);
                })
                .then();

        var source = sink.asFlux();
        var output = session.send(source.map(event -> {
            try {
                return objectMapper.writeValueAsString(event);
            } catch (JsonProcessingException e) {
                throw Exceptions.propagate(e);
            }
        }).map(session::textMessage));

        return Mono.zip(input, output).then();
    }

    @RabbitListener(queues = "${spring.rabbitmq.stream.name}")
    public void listen(String in) {
        var objectMapper = new ObjectMapper();
        BusinessEvent event;
        try {
           event = objectMapper.readValue(in, BusinessEvent.class);
        } catch (JsonProcessingException e) {
           log.error("Unable to convert business event to JSON: " + e.getMessage());
           return;
        }

        log.info("Forwarding business event to client: " + event);
        var result = sink.tryEmitNext(event);

        if (result.isFailure()) {
            log.error("Forwarding message failed");
        }
    }
}
