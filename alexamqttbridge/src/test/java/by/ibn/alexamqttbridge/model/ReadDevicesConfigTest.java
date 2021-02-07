package by.ibn.alexamqttbridge.model;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

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
		assertEquals("Smart Device Company", devicesConfig.devices.get(0).manufacturerName);
		assertEquals("Bedroom Outlet", devicesConfig.devices.get(0).friendlyName);
		assertEquals("Smart Device Switch", devicesConfig.devices.get(0).description);
		assertNotNull(devicesConfig.devices.get(0).additionalAttributes);
		assertEquals("Smart Device Manufacturer", devicesConfig.devices.get(0).additionalAttributes.manufacturer);
		assertEquals("Super Model", devicesConfig.devices.get(0).additionalAttributes.model);
		assertEquals("0001", devicesConfig.devices.get(0).additionalAttributes.serialNumber);
		assertEquals("1.0", devicesConfig.devices.get(0).additionalAttributes.firmwareVersion);
		assertEquals("1.0", devicesConfig.devices.get(0).additionalAttributes.softwareVersion);
		assertEquals("id0001", devicesConfig.devices.get(0).additionalAttributes.customIdentifier);
		assertNotNull(devicesConfig.devices.get(0).displayCategories);
		assertEquals(1, devicesConfig.devices.get(0).displayCategories.size());
		assertEquals("SWITCH", devicesConfig.devices.get(0).displayCategories.get(0));
		assertNotNull(devicesConfig.devices.get(0).cookie);
		assertEquals("arbitrary key/value pairs for skill to reference this endpoint.", devicesConfig.devices.get(0).cookie.key1);
		assertEquals("There can be multiple entries", devicesConfig.devices.get(0).cookie.key2);
		assertEquals("but they should only be used for reference purposes.", devicesConfig.devices.get(0).cookie.key3);
		assertEquals("This is not a suitable place to maintain current endpoint state.", devicesConfig.devices.get(0).cookie.key4);
		assertNotNull(devicesConfig.devices.get(0).capabilities);
		assertEquals(2, devicesConfig.devices.get(0).capabilities.size());
		assertEquals("AlexaInterface", devicesConfig.devices.get(0).capabilities.get(0).type);
		assertEquals("Alexa", devicesConfig.devices.get(0).capabilities.get(0).interFace);
		assertEquals("3", devicesConfig.devices.get(0).capabilities.get(0).version);
		assertEquals("AlexaInterface", devicesConfig.devices.get(0).capabilities.get(1).type);
		assertEquals("Alexa.PowerController", devicesConfig.devices.get(0).capabilities.get(1).interFace);
		assertNotNull(devicesConfig.devices.get(0).capabilities.get(1).properties);
		assertEquals(Boolean.FALSE, devicesConfig.devices.get(0).capabilities.get(1).properties.proactivelyReported);
		assertEquals(Boolean.TRUE, devicesConfig.devices.get(0).capabilities.get(1).properties.retrievable);
		assertNotNull(devicesConfig.devices.get(0).capabilities.get(1).properties.supported);
		assertEquals(1, devicesConfig.devices.get(0).capabilities.get(1).properties.supported.size());
		assertEquals("powerState", devicesConfig.devices.get(0).capabilities.get(1).properties.supported.get(0).name);
		assertNotNull(devicesConfig.devices.get(0).rules);
		assertEquals(1, devicesConfig.devices.get(0).rules.size());
		assertNotNull(devicesConfig.devices.get(0).rules.get(0).alexa);
		assertEquals("Alexa.PowerController", devicesConfig.devices.get(0).rules.get(0).alexa.interFace);
		assertEquals("powerState", devicesConfig.devices.get(0).rules.get(0).alexa.propertyName);
		assertNotNull(devicesConfig.devices.get(0).rules.get(0).mqtt);
		assertEquals("house/outlet/bedroom/command", devicesConfig.devices.get(0).rules.get(0).mqtt.commands);
		assertEquals("house/outlet/bedroom/state", devicesConfig.devices.get(0).rules.get(0).mqtt.state);
		assertNotNull(devicesConfig.devices.get(0).rules.get(0).valueMapsToAlexa);
		assertEquals(2, devicesConfig.devices.get(0).rules.get(0).valueMapsToAlexa.size());
		assertEquals("ON", ((ValueMapValue)devicesConfig.devices.get(0).rules.get(0).valueMapsToAlexa.get(0)).alexa);
		assertEquals("1", ((ValueMapValue)devicesConfig.devices.get(0).rules.get(0).valueMapsToAlexa.get(0)).mqtt);
		assertEquals("OFF", ((ValueMapValue)devicesConfig.devices.get(0).rules.get(0).valueMapsToAlexa.get(1)).alexa);
		assertEquals("0", ((ValueMapValue)devicesConfig.devices.get(0).rules.get(0).valueMapsToAlexa.get(1)).mqtt);
		assertNotNull(devicesConfig.devices.get(0).rules.get(0).valueMapsToMqtt);
		assertEquals(2, devicesConfig.devices.get(0).rules.get(0).valueMapsToMqtt.size());
		assertEquals("TurnOn", ((ValueMapValue)devicesConfig.devices.get(0).rules.get(0).valueMapsToMqtt.get(0)).alexa);
		assertEquals("1", ((ValueMapValue)devicesConfig.devices.get(0).rules.get(0).valueMapsToMqtt.get(0)).mqtt);
		assertEquals("TurnOff", ((ValueMapValue)devicesConfig.devices.get(0).rules.get(0).valueMapsToMqtt.get(1)).alexa);
		assertEquals("0", ((ValueMapValue)devicesConfig.devices.get(0).rules.get(0).valueMapsToMqtt.get(1)).mqtt);
	}

}
