package by.ibn.alexamqttbridge.service;

import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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

	private Map<String, List<DeviceState>> subscriptions = new HashMap<>();
	
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

		String topic = deviceRule.mqtt.state;
		
		if (StringUtils.isNotBlank(topic)) {
			
			List<DeviceState> stateListeners = subscriptions.get(topic);
			if (stateListeners == null) {
				
				log.trace("Subscribing on topic {}", topic);
				
				stateListeners = new ArrayList<>();
				subscriptions.put(topic, stateListeners);
				
				stateListeners.add(deviceState);
				
				try {
					
					mqttClient.subscribeWithResponse(topic, (tpic, msg) -> {
						String value = new String(msg.getPayload());
						log.trace("Received message on topic {}: {}", tpic, value );
						
						for(DeviceState state: subscriptions.get(topic)) {
							state.state = value;
							state.lastUpdate = ZonedDateTime.now();
						}
						
					});
					
				} catch (MqttException e) {
					
					log.error("Error while subscribing on topic {} for property {}", topic, deviceRule.alexa.propertyName);
					
				}
				
			} else if (stateListeners.size() > 0) {
				log.trace("Already subscribed on topic {}. Reusing existing subscription.", topic);
				
				// clone previously received state
				deviceState.state = stateListeners.get(0).state;
				stateListeners.add(deviceState);
			}
			
		}
	}	
}
