package com.example.camundaprocessdeployer;

import io.camunda.zeebe.spring.client.lifecycle.ZeebeClientLifecycle;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

@SpringBootTest
class CamundaProcessDeployerApplicationTests {

	@MockBean
	ZeebeClientLifecycle zeebeClientLifecycle;

	@Test
	void contextLoads() {
	}

}
