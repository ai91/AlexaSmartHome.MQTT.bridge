package by.ibn.alexamqttbridge.resources;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

@JsonInclude(Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public class EndpointDiscovery {
	
	public String endpointId;
	
	public String manufacturerName;
	
	public String friendlyName;
	
	public String description;
	
	public EndpointAdditionalAttributes additionalAttributes;
	
	public List<String> displayCategories;
	
	public Cookie cookie;
	
	public List<Capability> capabilities;

}
