package by.ibn.alexamqttbridge.service;

import java.time.Instant;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Optional;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import by.ibn.alexamqttbridge.model.Device;
import by.ibn.alexamqttbridge.model.DeviceBridgingRule;
import by.ibn.alexamqttbridge.model.DeviceState;
import by.ibn.alexamqttbridge.model.ValueMap;
import by.ibn.alexamqttbridge.resources.Context;
import by.ibn.alexamqttbridge.resources.ContextProperty;
import by.ibn.alexamqttbridge.resources.Event;
import by.ibn.alexamqttbridge.resources.PayloadResponse;
import by.ibn.alexamqttbridge.resources.Request;
import by.ibn.alexamqttbridge.resources.Response;

@Service
public class EventProcessorCommand extends EventProcessor {
	
	Logger log = LoggerFactory.getLogger(EventProcessorCommand.class);
	
	@Autowired
	private DeviceRepository devicesRepository;
	
	@Autowired
	private MQTTService mqttService;

	public EventProcessorCommand() {
		super(new String[0], new String[0]);
	}
	
	@Override
	public boolean isProcessable(Request request) {
		
		if (request != null && 
				request.directive != null && 
				request.directive.header != null &&
				StringUtils.equals(request.directive.header.payloadVersion, "3")) {
			
			String namespace = request.directive.header.namespace;
			String directiveName = request.directive.header.name;
			String instance = request.directive.header.instance;
			
			for (Device device: devicesRepository.getDevices()) {
				for (DeviceBridgingRule rule: device.rules) {
					if (StringUtils.equals(namespace, rule.alexa.interFace) &&
							StringUtils.equals(directiveName, rule.alexa.directiveName) &&
							StringUtils.equals(instance, rule.alexa.instance)) {
						
						return true;
						
					}
				}
			}

			
		}
		return false;
	}
	
	@Override
	public Response process(Request request) {

		Response response = null;

		if (request.directive != null && 
				request.directive.endpoint != null && 
				StringUtils.isNotBlank(request.directive.endpoint.endpointId)) {
			
			Optional<Device> deviceOpt = devicesRepository.getDeviceByEndpointId(request.directive.endpoint.endpointId);
			if (deviceOpt.isPresent()) {
				Device device = deviceOpt.get();

				String namespace = request.directive.header.namespace;
				String directiveName = request.directive.header.name;
				String instance = request.directive.header.instance;
				
				String mqttValue = null;
				
				// send to all applicable rules
				for (DeviceBridgingRule rule: device.rules) {
					
					if (StringUtils.equals(namespace, rule.alexa.interFace) &&
							StringUtils.equals(directiveName, rule.alexa.directiveName) &&
							StringUtils.equals(instance, rule.alexa.instance)) {
						
						String alexaValue = "";
						if (!StringUtils.isBlank(rule.alexa.payloadValue) && request.directive.payload != null && request.directive.payload.dynamicProperties != null) {
							Object payloadValue = request.directive.payload.dynamicProperties.get(rule.alexa.payloadValue);
							if (payloadValue != null) {
log.trace("cmd payload class: {}, string value: {}", payloadValue.getClass(), payloadValue.toString());
								alexaValue = payloadValue.toString();
							}
						}
					
						if (rule.valueMapsToMqtt != null && StringUtils.isNotBlank(rule.mqtt.commands)) {
							for (ValueMap valueMap: rule.valueMapsToMqtt) {
								if (valueMap.isApplicable(alexaValue)) {
									mqttValue = valueMap.map(alexaValue);
									mqttService.sendMessage(rule.mqtt.commands, mqttValue);
									break;
								}
							}
						}
						
					}
					
				}
				
				response = new Response();
				
				response.context = new Context();
				response.context.properties = new ArrayList<>();
				
				for (DeviceState deviceState: device.states) {
					ContextProperty property = new ContextProperty();
					response.context.properties.add(property);
					property.namespace = deviceState.interFace;
					property.name = deviceState.propertyName;
					property.instance = deviceState.instance;
					// convert the value back (the last known device state, converted via first matching rule)
					String alexaValue = deviceState.state;
					for (DeviceBridgingRule rule: device.rules) {
						if (StringUtils.equals(deviceState.interFace, rule.alexa.interFace) &&
								StringUtils.equals(deviceState.instance, rule.alexa.instance) && 
								StringUtils.equals(deviceState.propertyName, rule.alexa.propertyName) &&
								rule.valueMapsToAlexa != null) {
							
							boolean matched = false;
							for (ValueMap valueMap: rule.valueMapsToAlexa) {
								if (valueMap.isApplicable(deviceState.state)) {
									alexaValue = valueMap.map(deviceState.state);
									matched = true;
									break;
								}
							}
							if (matched) {
								break;
							}
						}
					}
					property.value = castValue(alexaValue);
					
					if (deviceState.lastUpdate != null) {
						property.timeOfSample = DateTimeFormatter.ISO_OFFSET_DATE_TIME.format(deviceState.lastUpdate);
						property.uncertaintyInMilliseconds = Instant.now().toEpochMilli() - deviceState.lastUpdate.toInstant().toEpochMilli();
					}
					
				}
				
				response.event = new Event();
				response.event.header = request.directive.header;
				response.event.header.namespace = "Alexa";
				response.event.header.name = "Response";
				response.event.payload = new PayloadResponse();;
				
				response.event.endpoint = request.directive.endpoint; 
			}
			
		}
		

		return response;
	}
	
}
