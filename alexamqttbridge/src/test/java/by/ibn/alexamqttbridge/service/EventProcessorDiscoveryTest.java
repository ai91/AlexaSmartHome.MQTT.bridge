package by.ibn.alexamqttbridge.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.paho.client.mqttv3.IMqttClient;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ActiveProfiles;

import by.ibn.alexamqttbridge.MQTTConfiguration;
import by.ibn.alexamqttbridge.resources.Directive;
import by.ibn.alexamqttbridge.resources.Endpoint;
import by.ibn.alexamqttbridge.resources.Header;
import by.ibn.alexamqttbridge.resources.Request;
import by.ibn.alexamqttbridge.resources.Response;

@SpringBootTest
@ActiveProfiles("test")
class EventProcessorDiscoveryTest {
	
	// just to disable
	@MockBean MQTTConfiguration mqtt;
	@MockBean IMqttClient mqttClient;
	@MockBean MqttConnectOptions mqttConnectOptions;

	@Autowired
	private EventProcessorDiscovery testee;
	
	@MockBean
	private DeviceRepository deviceRepository;

	@Test
	void test() {
		
		Request request = new Request();
		request.directive = new Directive();
		request.directive.header = new Header();
		request.directive.header.namespace = "Alexa.Discovery";
		request.directive.header.name = "Discover";
		request.directive.header.payloadVersion = "3";
		request.directive.header.messageId = "_messageId";
		
		List<Endpoint> expectedEndpoints = new ArrayList<>();
		Endpoint expectedEndpoint = new Endpoint();
		expectedEndpoint.endpointId = "_endpoint_id";
		expectedEndpoints.add(expectedEndpoint);
		Mockito.when(deviceRepository.getEndpoints()).thenReturn(expectedEndpoints);
		
		assertTrue(testee.isProcessable(request));
		
		Response response = testee.process(request);
		
		assertNotNull(response);
		assertNotNull(response.event);
		assertNotNull(response.event.header);
		assertEquals("Alexa.Discovery", response.event.header.namespace);
		assertEquals("Discover.Response", response.event.header.name);
		assertEquals("_messageId", response.event.header.messageId);
		
		assertNotNull(response.event.payload);
		assertNotNull(response.event.payload.endpoints);
		assertEquals(1, response.event.payload.endpoints.size());
		assertEquals("_endpoint_id", response.event.payload.endpoints.get(0).endpointId);
		
	}

}
