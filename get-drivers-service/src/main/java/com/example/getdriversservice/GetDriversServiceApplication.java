package com.example.getdriversservice;

import com.rabbitmq.stream.Environment;
import io.camunda.zeebe.spring.client.EnableZeebeClient;
import org.springframework.amqp.rabbit.listener.RabbitListenerContainerFactory;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.amqp.RabbitProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Primary;
import org.springframework.rabbit.stream.config.StreamRabbitListenerContainerFactory;
import org.springframework.rabbit.stream.listener.StreamListenerContainer;

@EnableZeebeClient
@SpringBootApplication
public class GetDriversServiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(GetDriversServiceApplication.class, args);
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

	@Primary
	@Bean
	public MessageConverter messageConverter() {
		return new Jackson2JsonMessageConverter();
	}
}
