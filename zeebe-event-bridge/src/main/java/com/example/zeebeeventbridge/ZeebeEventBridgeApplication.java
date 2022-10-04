package com.example.zeebeeventbridge;

import io.camunda.zeebe.spring.client.EnableZeebeClient;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@EnableZeebeClient
@SpringBootApplication
public class ZeebeEventBridgeApplication {

	public static void main(String[] args) {
		SpringApplication.run(ZeebeEventBridgeApplication.class, args);
	}

}
