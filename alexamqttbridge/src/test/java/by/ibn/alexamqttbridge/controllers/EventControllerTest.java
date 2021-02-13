package by.ibn.alexamqttbridge.controllers;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import org.eclipse.paho.client.mqttv3.IMqttClient;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import by.ibn.alexamqttbridge.MQTTConfiguration;
import by.ibn.alexamqttbridge.resources.Event;
import by.ibn.alexamqttbridge.resources.Header;
import by.ibn.alexamqttbridge.resources.Request;
import by.ibn.alexamqttbridge.resources.Response;
import by.ibn.alexamqttbridge.service.EventService;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
class EventControllerTest {

	// just to disable
	@MockBean MQTTConfiguration mqtt;
	@MockBean IMqttClient mqttClient;
	@MockBean MqttConnectOptions mqttConnectOptions;

	@Autowired
	private MockMvc mvc;
	
	@MockBean
	private EventService service;
	
	@Test
	void testPost() throws Exception {

		Response expectedResponse = new Response();
		expectedResponse.event = new Event();
		expectedResponse.event.header = new Header();
		expectedResponse.event.header.name = "_name";
		ArgumentCaptor<Request> requestCaptor = ArgumentCaptor.forClass(Request.class);
		Mockito.when(service.processEvent(requestCaptor.capture())).thenReturn(expectedResponse);
		
		mvc
		.perform(MockMvcRequestBuilders.post("/api/events")
				.with( SecurityMockMvcRequestPostProcessors.jwt().authorities(new SimpleGrantedAuthority("SCOPE_api")) )
				.contentType(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON)
				.content("{\"directive\":{\"header\":{\"name\":\"requestname\"}}}") 
		)
		.andExpect(MockMvcResultMatchers.status().isOk())
		.andExpect(MockMvcResultMatchers.content().json("{\"event\":{\"header\":{\"name\":\"_name\"}}}"));

		Mockito.verify(service, Mockito.times(1)).processEvent(Mockito.any());

		Request capturedRequest = requestCaptor.getValue();
		assertNotNull(capturedRequest);
		assertNotNull(capturedRequest.directive);
		assertNotNull(capturedRequest.directive.header);
		assertEquals("requestname", capturedRequest.directive.header.name);

	}

	@Test
	void testPostUnscoped() throws Exception {
		
		mvc
		.perform(MockMvcRequestBuilders.post("/api/events")
				.with( SecurityMockMvcRequestPostProcessors.jwt().authorities(new SimpleGrantedAuthority("SCOPE_read")) )
				.contentType(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON)
				.content("{\"directive\":{\"header\":{\"name\":\"requestname\"}}}") 
		)
		.andExpect(MockMvcResultMatchers.status().isForbidden());
		
		Mockito.verify(service, Mockito.times(0)).processEvent(Mockito.any());
		
	}
	
	@Test
	void testPostForbidden() throws Exception {
		
		mvc
		.perform(MockMvcRequestBuilders.post("/api/events")
				.with( SecurityMockMvcRequestPostProcessors.jwt() )
				.contentType(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON)
				.content("{\"directive\":{\"header\":{\"name\":\"requestname\"}}}") 
		)
		.andExpect(MockMvcResultMatchers.status().isForbidden());
		
		Mockito.verify(service, Mockito.times(0)).processEvent(Mockito.any());
		
	}
	
	@Test
	void testPostNoAuth() throws Exception {
		
		mvc
		.perform(MockMvcRequestBuilders.post("/api/events")
				.contentType(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON)
				.content("{\"directive\":{\"header\":{\"name\":\"requestname\"}}}") 
				)
		.andExpect(MockMvcResultMatchers.status().isForbidden());
		
		Mockito.verify(service, Mockito.times(0)).processEvent(Mockito.any());
		
	}

	@Test
	void testInternalServerError() throws Exception {

		Mockito.when(service.processEvent(Mockito.any())).thenThrow(new RuntimeException("boa"));
		
		mvc
		.perform(MockMvcRequestBuilders.post("/api/events")
				.with( SecurityMockMvcRequestPostProcessors.jwt().authorities(new SimpleGrantedAuthority("SCOPE_api")) )
				.contentType(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON)
				.content("{\"directive\":{\"header\":{\"name\":\"requestname\"}}}") 
		)
		.andExpect(MockMvcResultMatchers.status().isInternalServerError());

		Mockito.verify(service, Mockito.times(1)).processEvent(Mockito.any());

	}

	@Test
	void testBadRequestNoBody() throws Exception {
		
		Mockito.when(service.processEvent(Mockito.any())).thenCallRealMethod();
		
		mvc
		.perform(MockMvcRequestBuilders.post("/api/events")
				.with( SecurityMockMvcRequestPostProcessors.jwt().authorities(new SimpleGrantedAuthority("SCOPE_api")) )
				.contentType(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON)
		)
		.andExpect(MockMvcResultMatchers.status().isBadRequest());
		
		Mockito.verify(service, Mockito.times(0)).processEvent(Mockito.any());
		
	}
	
	@Test
	void testBadRequestEmptyEvent() throws Exception {
		
		Mockito.when(service.processEvent(Mockito.any())).thenReturn(null);
		
		mvc
		.perform(MockMvcRequestBuilders.post("/api/events")
				.with( SecurityMockMvcRequestPostProcessors.jwt().authorities(new SimpleGrantedAuthority("SCOPE_api")) )
				.contentType(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON)
				.content("{}") 
		)
		.andExpect(MockMvcResultMatchers.status().isBadRequest());
		
		Mockito.verify(service, Mockito.times(1)).processEvent(Mockito.any());
		
	}
	
	
}
