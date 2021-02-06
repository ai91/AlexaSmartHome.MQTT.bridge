package by.ibn.alexamqttbridge.resources;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

@JsonInclude(Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public class Cookie {
	
	public String key1;
	
	public String key2;
	
	public String key3;
	
	public String key4;
	
	public String key5;
	
	public String key6;
	
	public String key7;
	
	public String key8;
	
	public String key9;
	
	public String key10;

}
