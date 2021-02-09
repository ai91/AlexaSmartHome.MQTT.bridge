package by.ibn.alexamqttbridge.resources;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;

import org.junit.jupiter.api.Test;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import by.ibn.alexamqttbridge.TestUtil;

class SerializationTests {

	@Test
	void readDiscoveryRequest() throws JsonMappingException, JsonProcessingException {
		
		String jsonString = TestUtil.readResource("/alexaserializationtest/discovery_request.json");
		
		Request request = new ObjectMapper().readerFor(Request.class).readValue(jsonString);
		
		assertNotNull(request);
		assertNotNull(request.directive);
		assertNotNull(request.directive.header);
		assertEquals("Alexa.Discovery", request.directive.header.namespace);
		assertEquals("Discover", request.directive.header.name);
		assertEquals("3", request.directive.header.payloadVersion);
		assertEquals("MeSsAgEiD", request.directive.header.messageId);
		assertNotNull(request.directive.payload);
		assertNotNull(request.directive.payload.dynamicProperties);
		assertNotNull(request.directive.payload.dynamicProperties.get("scope"));
		
	}

	@Test
	void testDiscoveryResponse() throws JsonMappingException, JsonProcessingException {
		
		String expectedString = TestUtil.readResource("/alexaserializationtest/discovery_response.json");

		Response response = new ObjectMapper().readerFor(Response.class).readValue(expectedString);
		
		String json = new ObjectMapper().writerFor(Response.class).writeValueAsString(response);
		
		assertEquals(expectedString.replaceAll("\\s+", ""), json.replaceAll("\\s+", ""));
		
	}
	
	@Test
	void testPowerControlRequest() throws JsonMappingException, JsonProcessingException {
		
		String expectedString = TestUtil.readResource("/alexaserializationtest/powercontrol_request.json");
		
		Request request = new ObjectMapper().readerFor(Request.class).readValue(expectedString);
		
		assertNotNull(request);
		assertNotNull(request.directive);
		assertNotNull(request.directive.header);
		assertEquals("Alexa.PowerController", request.directive.header.namespace);
		assertEquals("TurnOn", request.directive.header.name);
		assertEquals("3", request.directive.header.payloadVersion);
		assertEquals("MeSsAgEiD", request.directive.header.messageId);
		assertEquals("ToKeN1", request.directive.header.correlationToken);
		assertNotNull(request.directive.payload);
		assertNull(request.directive.payload.dynamicProperties);
		assertNotNull(request.directive.endpoint);
		
		String json = new ObjectMapper().writerFor(Request.class).writeValueAsString(request);
		
		assertEquals(expectedString.replaceAll("\\s+", ""), json.replaceAll("\\s+", ""));
		
	}
	
	@Test
	void testPowerControlResponse() throws JsonMappingException, JsonProcessingException {
		
		String expectedString = TestUtil.readResource("/alexaserializationtest/powercontrol_response.json");
		
		Response response = new ObjectMapper().readerFor(Response.class).readValue(expectedString);
		
		String json = new ObjectMapper().writerFor(Response.class).writeValueAsString(response);
		
		assertEquals(expectedString.replaceAll("\\s+", ""), json.replaceAll("\\s+", ""));
		
	}
	
	@Test
	void testReportStateRequest() throws JsonMappingException, JsonProcessingException {
		
		String expectedString = TestUtil.readResource("/alexaserializationtest/reportstate_request.json");
		
		Request request = new ObjectMapper().readerFor(Request.class).readValue(expectedString);
		
		String json = new ObjectMapper().writerFor(Request.class).writeValueAsString(request);
		
		assertEquals(expectedString.replaceAll("\\s+", ""), json.replaceAll("\\s+", ""));
		
	}
	
	@Test
	void testReportStateResponse() throws JsonMappingException, JsonProcessingException {
		
		String expectedString = TestUtil.readResource("/alexaserializationtest/reportstate_response.json");

		Response response = new ObjectMapper().readerFor(Response.class).readValue(expectedString);
		
		String json = new ObjectMapper().writerFor(Response.class).writeValueAsString(response);

		assertEquals(expectedString.replaceAll("\\s+", ""), json.replaceAll("\\s+", ""));
		
	}
	
}
