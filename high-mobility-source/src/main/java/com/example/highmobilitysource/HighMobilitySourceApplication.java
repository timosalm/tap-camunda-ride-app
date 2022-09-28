package com.example.highmobilitysource;

import com.rabbitmq.stream.Environment;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.amqp.RabbitProperties;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Primary;
import org.springframework.rabbit.stream.listener.StreamMessageListener;
import org.springframework.rabbit.stream.producer.RabbitStreamTemplate;
import org.springframework.web.client.RestTemplate;

@ConfigurationPropertiesScan
@SpringBootApplication
public class HighMobilitySourceApplication {

	@Bean
	RestTemplate restTemplate(RestTemplateBuilder restTemplateBuilder) {
		return restTemplateBuilder.build();
	}

	@Bean
	Environment rabbitStreamEnvironment(RabbitProperties properties) {
		Environment environment = Environment.builder().host(properties.getHost()).port(properties.getStream().getPort())
				.username(properties.getUsername()).password(properties.getPassword()).build();
		environment.streamCreator().stream(properties.getStream().getName()).create();
		return environment;
	}

	@Primary
	@Bean
	public MessageConverter messageConverter() {
		return new Jackson2JsonMessageConverter();
	}

	@Bean
	RabbitStreamTemplate rabbitStreamTemplate(Environment environment, RabbitProperties properties,
											  MessageConverter messageConverter) {
		var rabbitStreamTemplate = new RabbitStreamTemplate(environment, properties.getStream().getName());
		rabbitStreamTemplate.setMessageConverter(messageConverter);
		return rabbitStreamTemplate;
	}

	public static void main(String[] args) {
		SpringApplication.run(HighMobilitySourceApplication.class, args);
	}

}
