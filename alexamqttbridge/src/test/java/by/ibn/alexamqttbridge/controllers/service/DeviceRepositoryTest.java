package by.ibn.alexamqttbridge.controllers.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.List;
import java.util.Optional;

import org.eclipse.paho.client.mqttv3.IMqttClient;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestPropertySource;

import by.ibn.alexamqttbridge.MQTTConfiguration;
import by.ibn.alexamqttbridge.model.Device;
import by.ibn.alexamqttbridge.resources.Endpoint;
import by.ibn.alexamqttbridge.service.DeviceRepository;

@SpringBootTest
@ActiveProfiles("test")
@TestPropertySource(properties = {"devicespath=/eventprocessordiscoverytest", "devicespathprefix=classpath:"})
class DeviceRepositoryTest {

	// just to disable
	@MockBean MQTTConfiguration mqtt;
	@MockBean IMqttClient mqttClient;
	@MockBean MqttConnectOptions mqttConnectOptions;

	@Autowired
	private DeviceRepository testee;
	
	@Test
	void loadConfig() {
		
		List<Endpoint> endpoints = testee.getEndpoints();
		
		assertNotNull(endpoints);
		assertEquals(1, endpoints.size());
		assertEquals("_endpoint_id", endpoints.get(0).endpointId);
		
	}

	@Test
	void getDeviceByEndpointId() {
		
		Optional<Device> device = testee.getDeviceByEndpointId("_endpoint_id");
		
		assertTrue(device.isPresent());
		assertEquals("_endpoint_id", device.get().endpointId);
		
	}
	
	@Test
	void getDeviceByStateTopic() {
		
		Optional<Device> device = testee.getDeviceByStateTopic("_state");
		
		assertTrue(device.isPresent());
		assertEquals("_endpoint_id", device.get().endpointId);
		
	}
	
}
