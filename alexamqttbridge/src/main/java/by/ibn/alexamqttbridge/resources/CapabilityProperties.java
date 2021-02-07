package by.ibn.alexamqttbridge.resources;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

@JsonInclude(Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public class CapabilityProperties {
	
	public List<CapabilityProperty> supported;
	
	public Boolean proactivelyReported;
	
	public Boolean retrievable;

}
