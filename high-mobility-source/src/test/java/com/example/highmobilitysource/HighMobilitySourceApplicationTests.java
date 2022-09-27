package com.example.highmobilitysource;

import com.rabbitmq.stream.Environment;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

@SpringBootTest
class HighMobilitySourceApplicationTests {

	@MockBean
	Environment rabbitStreamEnvironment;

	@Test
	void contextLoads() {
	}

}
