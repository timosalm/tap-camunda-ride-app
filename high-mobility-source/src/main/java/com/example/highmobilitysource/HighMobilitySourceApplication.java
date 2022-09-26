package com.example.highmobilitysource;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.web.client.RestTemplate;

@ConfigurationPropertiesScan
@SpringBootApplication
public class HighMobilitySourceApplication {

	@Bean
	RestTemplate restTemplate(RestTemplateBuilder restTemplateBuilder) {
		return restTemplateBuilder.build();
	}

	public static void main(String[] args) {
		SpringApplication.run(HighMobilitySourceApplication.class, args);
	}

}
