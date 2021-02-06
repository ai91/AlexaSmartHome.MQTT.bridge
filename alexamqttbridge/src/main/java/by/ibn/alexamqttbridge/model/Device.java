package by.ibn.alexamqttbridge.model;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonProperty.Access;

import by.ibn.alexamqttbridge.resources.EndpointDiscovery;

@JsonIgnoreProperties(ignoreUnknown = true)
public class Device extends EndpointDiscovery {
	
	@JsonProperty(access = Access.WRITE_ONLY)
	public List<DeviceBridgingRule> rules;

	@JsonIgnore
	public List<DeviceState> states;
	
}
