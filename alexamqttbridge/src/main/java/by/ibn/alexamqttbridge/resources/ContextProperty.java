package by.ibn.alexamqttbridge.resources;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

@JsonInclude(Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public class ContextProperty {
	
	public String namespace;
	
	public String name;
	
	public String instance;
	
	public Object value;
	
	public String timeOfSample;
	
	public Long uncertaintyInMilliseconds;

}
