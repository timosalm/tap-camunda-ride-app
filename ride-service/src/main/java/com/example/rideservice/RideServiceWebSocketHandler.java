package com.example.rideservice;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.socket.WebSocketHandler;
import org.springframework.web.reactive.socket.WebSocketMessage;
import org.springframework.web.reactive.socket.WebSocketSession;
import reactor.core.publisher.Mono;
import reactor.core.publisher.Sinks;

@Component
public class RideServiceWebSocketHandler implements WebSocketHandler {

    private static final Logger log = LoggerFactory.getLogger(RideServiceWebSocketHandler.class);

    private final Sinks.Many<String> sink = Sinks.many().multicast().directBestEffort();

    @Override
    public Mono<Void> handle(WebSocketSession session) {
        var input = session.receive()
                .map(WebSocketMessage::getPayloadAsText)
                .doOnNext(log::info)
                .then();

        var source = sink.asFlux();
        var output = session.send(source.map(session::textMessage));

        return Mono.zip(input, output).then();
    }

    @RabbitListener(queues = "${spring.rabbitmq.stream.name}")
    public void listen(String in) {
        log.info("Forwarding message: " + in);
        var result = sink.tryEmitNext(in);

        if (result.isFailure()) {
            log.error("Forwarding message failed");
        }
    }
}
