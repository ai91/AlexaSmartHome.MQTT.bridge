package by.ibn.alexamqttbridge.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
public class DeviceBridgingConfigAlexa {
	
	@JsonProperty("interface")
	public String interFace;
	
	@JsonProperty("instance")
	public String instance;
	
	@JsonProperty("propertyName")
	public String propertyName;
	
	@JsonProperty("directiveName")
	public String directiveName;
	
	@JsonProperty("payloadValue")
	public String payloadValue;
	
}
