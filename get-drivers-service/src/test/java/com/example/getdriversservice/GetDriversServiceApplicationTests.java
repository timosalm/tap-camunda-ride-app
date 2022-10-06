package com.example.getdriversservice;

import com.example.getdriversservice.getdrivers.GetDriversWorker;
import com.rabbitmq.stream.ConsumerBuilder;
import com.rabbitmq.stream.Environment;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.test.mock.mockito.MockBean;

import javax.annotation.PostConstruct;

import static org.mockito.Mockito.mock;

@SpringBootTest
class GetDriversServiceApplicationTests {

	@MockBean
	GetDriversWorker getDriversWorker;

	@TestConfiguration
	public static class TestConfig {
		@MockBean
		Environment rabbitStreamEnvironment;

		@PostConstruct
		public void initMock(){
			Mockito.when(rabbitStreamEnvironment.consumerBuilder()).thenReturn(mock(ConsumerBuilder.class));
		}
	}

	@Test
	void contextLoads() {
	}

}
