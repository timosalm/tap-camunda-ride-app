package com.example.rideservice;

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

    private final Sinks.Many<BusinessEvent> sink = Sinks.many().multicast().directBestEffort();

    @Override
    public Mono<Void> handle(WebSocketSession session) {
        var objectMapper = new ObjectMapper();
        var input = session.receive()
                .map(WebSocketMessage::getPayloadAsText)
                .map(a -> {
                    try {
                        return objectMapper.readValue(a, BusinessEvent.class);
                    } catch (JsonProcessingException e) {
                        throw Exceptions.propagate(e);
                    }
                })
                .doOnNext(event -> log.info("Received new business event: " + event.toString()))
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
    public void listen(BusinessEvent event) {
        log.info("Forwarding event: " + event);

        var result = sink.tryEmitNext(event);

        if (result.isFailure()) {
            log.error("Forwarding message failed");
        }
    }
}
