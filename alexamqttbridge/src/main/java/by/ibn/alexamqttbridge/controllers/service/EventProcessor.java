package by.ibn.alexamqttbridge.controllers.service;

import org.apache.commons.lang3.StringUtils;

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
				StringUtils.equals(request.directive.header.payloadVersion, payloadVersion);
	}
	
	public abstract Response process(Request request);
	
}
