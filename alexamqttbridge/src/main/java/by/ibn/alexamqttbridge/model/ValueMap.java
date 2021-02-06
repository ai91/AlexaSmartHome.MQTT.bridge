package by.ibn.alexamqttbridge.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonSubTypes.Type;
import com.fasterxml.jackson.annotation.JsonTypeInfo;

@JsonIgnoreProperties(ignoreUnknown = true)
@JsonTypeInfo(
		use = JsonTypeInfo.Id.NAME,
		include = JsonTypeInfo.As.PROPERTY,
		property = "type"
)
@JsonSubTypes({
	@Type(value = ValueMapValue.class, name = "value"),
})
public abstract class ValueMap {
	
	public abstract boolean isApplicableToAlexa(String value);
	
	public abstract boolean isApplicableToMqtt(String value);
	
	public abstract String mapToAlexa(String value);
	
	public abstract String mapToMqtt(String value);
	
	public abstract boolean isValidConfig();

}
