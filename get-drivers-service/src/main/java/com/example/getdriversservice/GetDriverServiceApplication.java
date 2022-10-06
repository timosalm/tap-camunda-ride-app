package com.example.getdriversservice;

import io.camunda.zeebe.spring.client.EnableZeebeClient;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@EnableZeebeClient
@SpringBootApplication
public class GetDriverServiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(GetDriverServiceApplication.class, args);
	}

}
