package by.ibn.alexamqttbridge.model;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class DevicesConfig {
	
	public List<Device> devices;
	
}
