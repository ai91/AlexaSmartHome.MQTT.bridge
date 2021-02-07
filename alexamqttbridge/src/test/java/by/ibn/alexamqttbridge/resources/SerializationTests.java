package by.ibn.alexamqttbridge.resources;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;

import java.util.ArrayList;
import java.util.Arrays;

import org.junit.jupiter.api.Test;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import by.ibn.alexamqttbridge.TestUtil;
import by.ibn.alexamqttbridge.model.Device;

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
		assertNotNull(request.directive.payload.scope);
		assertEquals("BearerToken", request.directive.payload.scope.type);
		assertEquals("ToKeN", request.directive.payload.scope.token);
		
	}

	@Test
	void writeDiscoveryResponse() throws JsonMappingException, JsonProcessingException {
		
		String expectedString = TestUtil.readResource("/alexaserializationtest/discovery_response.json");
		
		Response response = new Response();
		response.event = new Event();
		response.event.header = new Header();
		response.event.header.namespace = "Alexa.Discovery";
		response.event.header.name = "Discover.Response";
		response.event.header.payloadVersion = "3";
		response.event.header.messageId = "MeSsAgEiD";
		response.event.payload = new PayloadDiscoveryResponse();
		((PayloadDiscoveryResponse)response.event.payload).endpoints = new ArrayList<EndpointDiscovery>();
		Device endpoint = new Device();
		endpoint.endpointId = "demo_id";
		endpoint.manufacturerName = "Smart Device Company";
		endpoint.friendlyName = "Bedroom Outlet";
		endpoint.description = "Smart Device Switch";
		endpoint.displayCategories = Arrays.asList("SWITCH");
		endpoint.cookie = new Cookie();
		endpoint.cookie.key1 = "arbitrary key/value pairs for skill to reference this endpoint.";
		endpoint.cookie.key2 = "There can be multiple entries";
		endpoint.cookie.key3 = "but they should only be used for reference purposes.";
		endpoint.cookie.key4 = "This is not a suitable place to maintain current endpoint state.";
		endpoint.capabilities = new ArrayList<>();
		endpoint.additionalAttributes = new EndpointAdditionalAttributes();
		endpoint.additionalAttributes.manufacturer = "Smart Device Manufacturer";
		endpoint.additionalAttributes.model = "Super Model";
		endpoint.additionalAttributes.serialNumber = "0001";
		endpoint.additionalAttributes.firmwareVersion = "1.0";
		endpoint.additionalAttributes.softwareVersion = "1.0";
		endpoint.additionalAttributes.customIdentifier = "id0001";
		
		Capability capability1 = new Capability();
		capability1.type = "AlexaInterface";
		capability1.interFace = "Alexa";
		capability1.version = "3";
		endpoint.capabilities.add(capability1);
		Capability capability2 = new Capability();
		capability2.type = "AlexaInterface";
		capability2.interFace = "Alexa.PowerController";
		capability2.version = "3";
		capability2.properties = new CapabilityProperties();
		capability2.properties.retrievable = Boolean.TRUE;
		capability2.properties.proactivelyReported = Boolean.TRUE;
		capability2.properties.supported = new ArrayList<>();
		capability2.properties.supported.add(new CapabilityProperty());
		capability2.properties.supported.get(0).name = "powerState";
		endpoint.capabilities.add(capability2);
		((PayloadDiscoveryResponse)response.event.payload).endpoints.add(endpoint);

		// must be skipped on serialization
		endpoint.rules = new ArrayList<>();
		endpoint.states = new ArrayList<>();
		
		String json = new ObjectMapper().writerFor(Response.class).writeValueAsString(response);
		
		assertEquals(expectedString.replaceAll("\\s+", ""), json.replaceAll("\\s+", ""));
		
	}
	
	@Test
	void readPowerControlRequest() throws JsonMappingException, JsonProcessingException {
		
		String jsonString = TestUtil.readResource("/alexaserializationtest/powercontrol_request.json");
		
		Request request = new ObjectMapper().readerFor(Request.class).readValue(jsonString);
		
		assertNotNull(request);
		assertNotNull(request.directive);
		assertNotNull(request.directive.header);
		assertEquals("Alexa.PowerController", request.directive.header.namespace);
		assertEquals("TurnOn", request.directive.header.name);
		assertEquals("3", request.directive.header.payloadVersion);
		assertEquals("MeSsAgEiD", request.directive.header.messageId);
		assertEquals("ToKeN1", request.directive.header.correlationToken);
		assertNotNull(request.directive.payload);
		assertNull(request.directive.payload.scope);
		assertNotNull(request.directive.endpoint);
		assertNotNull(request.directive.endpoint.scope);
		assertEquals("BearerToken", request.directive.endpoint.scope.type);
		assertEquals("ToKeN2", request.directive.endpoint.scope.token);
		assertEquals("demo_id", request.directive.endpoint.endpointId);
		assertNotNull(request.directive.endpoint.cookie);
		assertEquals("arbitrary key/value pairs for skill to reference this endpoint.", request.directive.endpoint.cookie.key1);
		assertEquals("There can be multiple entries", request.directive.endpoint.cookie.key2);
		assertEquals("but they should only be used for reference purposes.", request.directive.endpoint.cookie.key3);
		assertEquals("This is not a suitable place to maintain current endpoint state.", request.directive.endpoint.cookie.key4);
		
	}
	
	@Test
	void writePowerControlResponse() throws JsonMappingException, JsonProcessingException {
		
		String expectedString = TestUtil.readResource("/alexaserializationtest/powercontrol_response.json");
		
		Response response = new Response();
		response.event = new Event();
		response.context = new Context();
		response.context.properties = new ArrayList<>();
		ContextProperty properties = new ContextProperty();
		properties.namespace = "Alexa.PowerController";
		properties.name = "powerState";
		properties.value = "ON";
		properties.timeOfSample = "2021-02-01T23:36:10.23Z";
		properties.uncertaintyInMilliseconds = 50l;
		response.context.properties.add(properties);
		response.event.header = new Header();
		response.event.header.namespace = "Alexa";
		response.event.header.name = "Response";
		response.event.header.payloadVersion = "3";
		response.event.header.messageId = "MeSsAgEiD";
		response.event.header.correlationToken = "ToKeN1";
		response.event.endpoint = new Endpoint();
		response.event.endpoint.endpointId = "demo_id";
		response.event.endpoint.scope = new Scope();
		response.event.endpoint.scope.type = "BearerToken";
		response.event.endpoint.scope.token = "ToKeN2";
		response.event.payload = new PayloadDiscoveryResponse();
		
		String json = new ObjectMapper().writerFor(Response.class).writeValueAsString(response);
		
		assertEquals(expectedString.replaceAll("\\s+", ""), json.replaceAll("\\s+", ""));
		
	}
	
	@Test
	void readReportStateRequest() throws JsonMappingException, JsonProcessingException {
		
		String jsonString = TestUtil.readResource("/alexaserializationtest/reportstate_request.json");
		
		Request request = new ObjectMapper().readerFor(Request.class).readValue(jsonString);
		
		assertNotNull(request);
		assertNotNull(request.directive);
		assertNotNull(request.directive.header);
		assertEquals("Alexa", request.directive.header.namespace);
		assertEquals("ReportState", request.directive.header.name);
		assertEquals("3", request.directive.header.payloadVersion);
		assertEquals("MeSsAgEiD", request.directive.header.messageId);
		assertEquals("ToKeN1", request.directive.header.correlationToken);
		assertNotNull(request.directive.payload);
		assertNull(request.directive.payload.scope);
		assertNotNull(request.directive.endpoint);
		assertNotNull(request.directive.endpoint.scope);
		assertEquals("BearerToken", request.directive.endpoint.scope.type);
		assertEquals("ToKeN2", request.directive.endpoint.scope.token);
		assertEquals("demo_id", request.directive.endpoint.endpointId);
		assertNotNull(request.directive.endpoint.cookie);
		assertEquals("arbitrary key/value pairs for skill to reference this endpoint.", request.directive.endpoint.cookie.key1);
		assertEquals("There can be multiple entries", request.directive.endpoint.cookie.key2);
		assertEquals("but they should only be used for reference purposes.", request.directive.endpoint.cookie.key3);
		assertEquals("This is not a suitable place to maintain current endpoint state.", request.directive.endpoint.cookie.key4);
		
	}
	
	@Test
	void writeReportStateResponse() throws JsonMappingException, JsonProcessingException {
		
		String expectedString = TestUtil.readResource("/alexaserializationtest/reportstate_response.json");
		
		Response response = new Response();
		response.event = new Event();
		response.context = new Context();
		response.context.properties = new ArrayList<>();
		ContextProperty properties = new ContextProperty();
		properties.namespace = "Alexa.PowerController";
		properties.name = "powerState";
		properties.value = "OFF";
		properties.timeOfSample = "2021-02-01T23:36:10.23Z";
		properties.uncertaintyInMilliseconds = 50l;
		response.context.properties.add(properties);
		response.event.header = new Header();
		response.event.header.namespace = "Alexa";
		response.event.header.name = "StateReport";
		response.event.header.payloadVersion = "3";
		response.event.header.messageId = "MeSsAgEiD";
		response.event.header.correlationToken = "ToKeN1";
		response.event.endpoint = new Endpoint();
		response.event.endpoint.endpointId = "demo_id";
		response.event.endpoint.scope = new Scope();
		response.event.endpoint.scope.type = "BearerToken";
		response.event.endpoint.scope.token = "ToKeN2";
		response.event.payload = new PayloadDiscoveryResponse();
		
		String json = new ObjectMapper().writerFor(Response.class).writeValueAsString(response);
		
		assertEquals(expectedString.replaceAll("\\s+", ""), json.replaceAll("\\s+", ""));
		
	}
	
}
