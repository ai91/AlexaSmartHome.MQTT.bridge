package by.ibn.alexamqttbridge.model;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import by.ibn.alexamqttbridge.TestUtil;

class ValueMapRegexTest {

	@Test
	void test() throws JsonMappingException, JsonProcessingException {
		
		String jsonString = TestUtil.readResource("/valuemapstest/regex.json");
		
		ValueMapRegex valueMap = new ObjectMapper().readerFor(ValueMapRegex.class).readValue(jsonString);
		
		assertFalse(valueMap.isApplicable("1"));
		assertFalse(valueMap.isApplicable("on"));
		assertTrue(valueMap.isApplicable("cmd23"));
		
		assertEquals("mva23", valueMap.map("cmd23"));
		
	}

}
