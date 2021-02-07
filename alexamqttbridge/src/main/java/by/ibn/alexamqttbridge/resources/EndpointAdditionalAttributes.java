package by.ibn.alexamqttbridge.resources;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

@JsonInclude(Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public class EndpointAdditionalAttributes {
	
	public String manufacturer;
	
	public String model;
	
	public String serialNumber;
	
	public String firmwareVersion;
	
	public String softwareVersion;
	
	public String customIdentifier;

}
