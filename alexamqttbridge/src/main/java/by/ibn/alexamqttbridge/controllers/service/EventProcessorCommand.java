package by.ibn.alexamqttbridge.controllers.service;

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
public class EventProcessorCommand extends EventProcessor {
	
	@Autowired
	private DeviceRepository devicesRepository;
	
	@Autowired
	private MQTTService mqttService;

	public EventProcessorCommand() {
		super(
				new String[]{"Alexa.PowerController", "Alexa.RangeController", "Alexa.ToggleController", "Alexa.ModeController",
						"Alexa.BrightnessController", "Alexa.ColorController", "Alexa.ColorTemperatureController",
						"Alexa.PercentageController", "Alexa.PowerLevelController", "Alexa.SceneController", "Alexa.ThermostatController",
						"Alexa.TimeHoldController"}, 
				new String[]{"TurnOn", "TurnOff", "SetRangeValue", "AdjustRangeValue", "SetMode", "AdjustMode",
						"SetBrightness", "AdjustBrightness", "SetColor", "SetColorTemperature", "IncreaseColorTemperature",
						"DecreaseColorTemperature", "SetPercentage", "AdjustPercentage", "SetPowerLevel", "AdjustPowerLevel",
						"Activate", "Deactivate", "SetTargetTemperature", "AdjustTargetTemperature", "SetThermostatMode",
						"ResumeSchedule", "Hold", "Resume"}
		);
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

				String alexaValue = request.directive.header.name;
				String mqttValue = null;
				
				// send to all applicable rules
				for (DeviceBridgingRule rule: device.rules) {
					
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
				
				response = new Response();
				
				response.context = new Context();
				response.context.properties = new ArrayList<>();
				
				for (DeviceState deviceState: device.states) {
					ContextProperty property = new ContextProperty();
					property.namespace = deviceState.interFace;
					property.name = deviceState.propertyName;
					property.instance = deviceState.instance;
					// convert the value back (the last known device state, converted via first matching rule)
					alexaValue = deviceState.state;
					for (DeviceBridgingRule rule: device.rules) {
						if (rule.valueMapsToAlexa != null) {
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
					if (deviceState.lastChange != null) {
						property.timeOfSample = DateTimeFormatter.ISO_OFFSET_DATE_TIME.format(deviceState.lastChange);
						
						if (deviceState.lastRepeat == null) {
							property.uncertaintyInMilliseconds = Instant.now().toEpochMilli() - deviceState.lastChange.toInstant().toEpochMilli();
						} else {
							property.uncertaintyInMilliseconds = deviceState.lastRepeat.toInstant().toEpochMilli() - deviceState.lastChange.toInstant().toEpochMilli();
						}
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
