package by.ibn.alexamqttbridge;

import org.eclipse.paho.client.mqttv3.IMqttClient;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ActiveProfiles;

@SpringBootTest
@ActiveProfiles("test")
class AlexamqttbridgeApplicationTests {
	
	// just to disable
	@MockBean MQTTConfiguration mqtt;
	@MockBean IMqttClient mqttClient;
	@MockBean MqttConnectOptions mqttConnectOptions;

	@Test
	void contextLoads() {
	}

}
