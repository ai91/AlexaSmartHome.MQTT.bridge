package by.ibn.alexamqttbridge.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class DeviceBridgingConfigMqtt {
	
	public String commands;
	
	public String state;

}
