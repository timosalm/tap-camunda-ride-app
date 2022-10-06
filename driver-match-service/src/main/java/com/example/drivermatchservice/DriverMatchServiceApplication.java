package com.example.drivermatchservice;

import io.camunda.zeebe.spring.client.EnableZeebeClient;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@EnableZeebeClient
@SpringBootApplication
public class DriverMatchServiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(DriverMatchServiceApplication.class, args);
	}

}
