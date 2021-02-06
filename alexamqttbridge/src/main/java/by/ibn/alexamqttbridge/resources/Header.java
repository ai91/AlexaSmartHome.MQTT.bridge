package by.ibn.alexamqttbridge.resources;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

@JsonInclude(Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public class Header {
	
	public String namespace;
	
	public String name;
	
	public String payloadVersion;
	
	public String messageId;
	
	public String correlationToken;

}
