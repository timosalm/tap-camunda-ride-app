package com.example.rideservice;

import com.rabbitmq.stream.Environment;
import org.springframework.amqp.rabbit.listener.RabbitListenerContainerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.amqp.RabbitProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.rabbit.stream.config.StreamRabbitListenerContainerFactory;
import org.springframework.rabbit.stream.listener.StreamListenerContainer;
import org.springframework.web.reactive.HandlerMapping;
import org.springframework.web.reactive.config.EnableWebFlux;
import org.springframework.web.reactive.handler.SimpleUrlHandlerMapping;
import org.springframework.web.reactive.socket.WebSocketHandler;

import java.util.HashMap;

@EnableWebFlux
@SpringBootApplication
public class RideServiceApplication {

	@Bean
	public HandlerMapping webSocketHandlerMapping(WebSocketHandler webSocketHandler) {
		var urlMap = new HashMap<String, WebSocketHandler>();
		urlMap.put("/events", webSocketHandler);
		return new SimpleUrlHandlerMapping(urlMap, -1);
	}

	@Bean
	Environment rabbitStreamEnvironment(RabbitProperties properties) {
		Environment environment = Environment.builder().host(properties.getHost()).port(properties.getStream().getPort())
				.username(properties.getUsername()).password(properties.getPassword()).build();
		environment.streamCreator().stream(properties.getStream().getName()).create();
		return environment;
	}

	@Bean
	RabbitListenerContainerFactory<StreamListenerContainer> rabbitListenerContainerFactory(Environment environment) {
		return new StreamRabbitListenerContainerFactory(environment);
	}

	public static void main(String[] args) {
		SpringApplication.run(RideServiceApplication.class, args);
	}

}
