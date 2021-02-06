package by.ibn.alexamqttbridge.resources;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

@JsonInclude(Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public class Directive {
	
	public Header header;
	
	public Endpoint endpoint;
	
	public PayloadRequest payload;

}
