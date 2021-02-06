package by.ibn.alexamqttbridge.model;

import org.apache.commons.lang3.StringUtils;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class ValueMapValue extends ValueMap {

	public String mqtt;
	
	public String alexa;

	@Override
	public boolean isApplicableToAlexa(String value) {
		return StringUtils.equalsIgnoreCase(value, mqtt);
	}

	@Override
	public boolean isApplicableToMqtt(String value) {
		return StringUtils.equalsIgnoreCase(value, alexa);
	}

	@Override
	public String mapToAlexa(String value) {
		return alexa;
	}

	@Override
	public String mapToMqtt(String value) {
		return mqtt;
	}

	@Override
	public boolean isValidConfig() {
		return StringUtils.isNoneBlank(mqtt, alexa);
	}

}
