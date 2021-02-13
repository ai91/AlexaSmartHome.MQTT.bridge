package by.ibn.alexamqttbridge.service;

import java.time.Instant;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Optional;

import org.apache.commons.lang3.StringUtils;
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
public class EventProcessorReportState extends EventProcessor {
	
	@Autowired
	private DeviceRepository devicesRepository;

	public EventProcessorReportState() {
		super(new String[]{"Alexa"}, new String[]{"ReportState"});
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
				
				response = new Response();
				
				response.context = new Context();
				response.context.properties = new ArrayList<>();
				
				for (DeviceState deviceState: device.states) {
					ContextProperty property = new ContextProperty();
					response.context.properties.add(property);
					property.namespace = deviceState.interFace;
					property.name = deviceState.propertyName;
					property.instance = deviceState.instance;
					// convert the value (converted via first matching rule. if none matches - send as it is)
					String alexaValue = deviceState.state;
					for (DeviceBridgingRule rule: device.rules) {
						
						if (StringUtils.equals(deviceState.interFace, rule.alexa.interFace) &&
								StringUtils.equals(deviceState.propertyName, rule.alexa.propertyName) &&
								StringUtils.equals(deviceState.instance, rule.alexa.instance) && 
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
					property.value = alexaValue;
					
					
					if (deviceState.lastUpdate != null) {
						property.timeOfSample = DateTimeFormatter.ISO_OFFSET_DATE_TIME.format(deviceState.lastUpdate);
						property.uncertaintyInMilliseconds = Instant.now().toEpochMilli() - deviceState.lastUpdate.toInstant().toEpochMilli();
					}
					
				}
				
				response.event = new Event();
				response.event.header = request.directive.header;
				response.event.header.name = "StateReport";
				response.event.payload = new PayloadResponse();;
				response.event.endpoint = request.directive.endpoint; 
			}
			
		}
		

		return response;
	}
	
}
