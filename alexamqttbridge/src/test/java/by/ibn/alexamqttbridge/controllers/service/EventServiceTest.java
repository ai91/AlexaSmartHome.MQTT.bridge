package by.ibn.alexamqttbridge.controllers.service;

import static org.junit.jupiter.api.Assertions.*;

import java.util.ArrayList;

import org.eclipse.paho.client.mqttv3.IMqttClient;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ActiveProfiles;

import by.ibn.alexamqttbridge.MQTTConfiguration;
import by.ibn.alexamqttbridge.resources.Context;
import by.ibn.alexamqttbridge.resources.ContextProperty;
import by.ibn.alexamqttbridge.resources.Request;
import by.ibn.alexamqttbridge.resources.Response;

@SpringBootTest
@ActiveProfiles("test")
class EventServiceTest {
	
	// just to disable
	@MockBean MQTTConfiguration mqtt;
	@MockBean IMqttClient mqttClient;
	@MockBean MqttConnectOptions mqttConnectOptions;

	@Autowired
	private EventService testee;
	
	@MockBean
	private EventProcessorDiscovery mockProcessor; 

	@Test
	void test() {
		
		Mockito.when(mockProcessor.isProcessable(Mockito.any())).thenReturn(Boolean.TRUE);
		
		Response expectedResponse = new Response();
		expectedResponse.context = new Context();
		expectedResponse.context.properties = new ArrayList<>();
		expectedResponse.context.properties.add(new ContextProperty());
		expectedResponse.context.properties.get(0).name = "whoa";
		Mockito.when(mockProcessor.process(Mockito.any())).thenReturn(expectedResponse);
		
		Response response = testee.processEvent(new Request());
		
		assertNotNull(response);
		assertNotNull(response.context);
		assertNotNull(response.context.properties);
		assertEquals(1, response.context.properties.size());
		assertEquals("whoa", response.context.properties.get(0).name);
	}

}
