package by.ibn.alexamqttbridge.model;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class DeviceBridgingRule {

	public DeviceBridgingConfigAlexa alexa;
	
	public DeviceBridgingConfigMqtt mqtt;
	
	public List<ValueMap> valueMapsToAlexa;
	
	public List<ValueMap> valueMapsToMqtt;
	
}
