package by.ibn.alexamqttbridge.model;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;

import org.junit.jupiter.api.Test;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import by.ibn.alexamqttbridge.TestUtil;

class ReadDevicesConfigTest {

	@Test
	void testConfig() throws JsonMappingException, JsonProcessingException {
		
		String jsonString = TestUtil.readResource("/devices.json");
		
		DevicesConfig devicesConfig = new ObjectMapper().readerFor(DevicesConfig.class).readValue(jsonString);
		
		assertNotNull(devicesConfig);
		assertNotNull(devicesConfig.devices);
		assertEquals(1, devicesConfig.devices.size());
		assertEquals("demo_id", devicesConfig.devices.get(0).endpointId);
		assertEquals("Smart Device Company", devicesConfig.devices.get(0).dynamicProperties.get("manufacturerName"));
		assertEquals("Bedroom Outlet", devicesConfig.devices.get(0).dynamicProperties.get("friendlyName"));
		assertEquals("Smart Device Switch", devicesConfig.devices.get(0).dynamicProperties.get("description"));
		assertNotNull(devicesConfig.devices.get(0).dynamicProperties.get("additionalAttributes"));
		assertNotNull(devicesConfig.devices.get(0).rules);
		assertEquals(3, devicesConfig.devices.get(0).rules.size());
		assertNotNull(devicesConfig.devices.get(0).rules.get(0).alexa);
		assertEquals("Alexa.PowerController", devicesConfig.devices.get(0).rules.get(0).alexa.interFace);
		assertEquals("powerState", devicesConfig.devices.get(0).rules.get(0).alexa.propertyName);
		assertNotNull(devicesConfig.devices.get(0).rules.get(0).mqtt);
		assertEquals("house/outlet/bedroom/state", devicesConfig.devices.get(0).rules.get(0).mqtt.state);
		assertNotNull(devicesConfig.devices.get(0).rules.get(0).valueMapsToAlexa);
		assertEquals(2, devicesConfig.devices.get(0).rules.get(0).valueMapsToAlexa.size());
		assertEquals("ON", ((ValueMapValue)devicesConfig.devices.get(0).rules.get(0).valueMapsToAlexa.get(0)).to);
		assertEquals("1", ((ValueMapValue)devicesConfig.devices.get(0).rules.get(0).valueMapsToAlexa.get(0)).from);
		assertEquals("OFF", ((ValueMapValue)devicesConfig.devices.get(0).rules.get(0).valueMapsToAlexa.get(1)).to);
		assertEquals("0", ((ValueMapValue)devicesConfig.devices.get(0).rules.get(0).valueMapsToAlexa.get(1)).from);
		assertNull(devicesConfig.devices.get(0).rules.get(0).valueMapsToMqtt);
	}

}
