package by.ibn.alexamqttbridge.controllers.service;

import java.time.ZonedDateTime;

import org.apache.commons.lang3.StringUtils;
import org.eclipse.paho.client.mqttv3.IMqttClient;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import by.ibn.alexamqttbridge.model.Device;
import by.ibn.alexamqttbridge.model.DeviceBridgingRule;
import by.ibn.alexamqttbridge.model.DeviceState;

@Service
public class MQTTService {

	private Logger log = LoggerFactory.getLogger(MQTTService.class);
	
	@Autowired
	private IMqttClient mqttClient;
	
	public void sendMessage(String topic, String value) {

		MqttMessage mqttMessage = new MqttMessage();
		mqttMessage.setPayload(value.getBytes());
		mqttMessage.setQos(2);
		mqttMessage.setRetained(false);

		try {
			
			mqttClient.publish(topic, mqttMessage);
			
		} catch (MqttException e) {
			
			log.error("Error while publishing message to the MQTT", e);
			
		}
	}
	
	public void subscribeOnTopics(DeviceRepository deviceRepository) {
		
		for (Device device: deviceRepository.getDevices()) {
			
			for (DeviceBridgingRule rule: device.rules) {
				
				for (DeviceState state: device.states) {
					
					if (StringUtils.equals(state.propertyName, rule.alexa.propertyName )) {
						
						subscribe(rule, state);
					}
					
				}
				
			}
			
		}
		
	}
	
	private void subscribe(DeviceBridgingRule deviceRule, DeviceState deviceState) {

		if (StringUtils.isNotBlank(deviceRule.mqtt.state)) {
			
			log.trace("Subscribing on topic {}", deviceRule.mqtt.state);
			
			try {
				
				mqttClient.subscribeWithResponse(deviceRule.mqtt.state, (tpic, msg) -> {
					String value = new String(msg.getPayload());
					log.trace("Received message on topic {}: {}", tpic, value );
					
					deviceState.lastUpdate = ZonedDateTime.now();
					deviceState.state = value;
					
				});
				
			} catch (MqttException e) {
				
				log.error("Error while subscribing on topic {} for property {}", deviceRule.mqtt.state, deviceRule.alexa.propertyName);
				
			}
		}
	}	
}
