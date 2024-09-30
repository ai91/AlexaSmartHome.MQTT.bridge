package by.ibn.alexamqttbridge.service;

import java.util.HashMap;

import org.apache.commons.lang3.StringUtils;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import by.ibn.alexamqttbridge.resources.Request;
import by.ibn.alexamqttbridge.resources.Response;

public abstract class EventProcessor {
	
	private String[] namespaces;
	private String[] names;
	private String payloadVersion;
	
	public EventProcessor(String[] namespaces, String[] names) {
		this.namespaces = namespaces;
		this.names = names;
		this.payloadVersion = "3";
	}
	
	public boolean isProcessable(Request request) {
		
		return request != null && 
				request.directive != null && 
				request.directive.header != null &&
				StringUtils.equalsAny(request.directive.header.namespace, namespaces) &&
				StringUtils.equalsAny(request.directive.header.name, names) &&
				StringUtils.startsWith(request.directive.header.payloadVersion, payloadVersion);
	}
	
	public abstract Response process(Request request);

	Object castValue(String value)
	{
		try {
			TypeReference<HashMap<String,Object>> typeRef = new TypeReference<HashMap<String,Object>>() {};
			HashMap<String,Object> jsonMap = new ObjectMapper().readValue(value, typeRef);
			if (jsonMap != null) {
				return jsonMap;
			}
		} catch (Exception e) {}
		
		return value;
		
	}

	
}
