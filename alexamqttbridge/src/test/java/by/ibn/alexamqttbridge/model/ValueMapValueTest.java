package by.ibn.alexamqttbridge.model;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import by.ibn.alexamqttbridge.TestUtil;

class ValueMapValueTest {

	@Test
	void test() throws JsonMappingException, JsonProcessingException {
		
		String jsonString = TestUtil.readResource("/valuemapstest/value.json");
		
		ValueMapValue valueMap = new ObjectMapper().readerFor(ValueMapValue.class).readValue(jsonString);
		
		assertTrue(valueMap.isApplicableToAlexa("1"));
		assertFalse(valueMap.isApplicableToAlexa("on"));
		assertFalse(valueMap.isApplicableToAlexa("TurnOn"));
		
		assertFalse(valueMap.isApplicableToMqtt("1"));
		assertFalse(valueMap.isApplicableToMqtt("on"));
		assertTrue(valueMap.isApplicableToMqtt("TurnOn"));
		
		assertEquals("TurnOn", valueMap.mapToAlexa("1"));
		assertEquals("1", valueMap.mapToMqtt("TurnOn"));
		
	}

}
