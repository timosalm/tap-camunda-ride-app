package com.example.camundaprocessdeployer;

import io.camunda.zeebe.spring.client.EnableZeebeClient;
import io.camunda.zeebe.spring.client.annotation.ZeebeDeployment;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@ZeebeDeployment(resources = "classpath*:/models/*.*")
@EnableZeebeClient
public class CamundaProcessDeployerApplication {

	public static void main(String[] args) {
		SpringApplication.run(CamundaProcessDeployerApplication.class, args);
	}

}
