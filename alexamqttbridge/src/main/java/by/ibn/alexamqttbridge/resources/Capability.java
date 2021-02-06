package by.ibn.alexamqttbridge.resources;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

@JsonInclude(Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public class Capability {
	
	@JsonProperty("type")
	public String type;
	
	@JsonProperty("version")
	public String version;
	
	@JsonProperty("interface")
	public String interFace;

	@JsonProperty("properties")
	public CapabilityProperties properties;
}
