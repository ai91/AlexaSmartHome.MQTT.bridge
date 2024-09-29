package by.ibn.alexamqttbridge;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

import by.ibn.alexamqttbridge.service.DeviceRepository;
import by.ibn.alexamqttbridge.service.MQTTService;

@Component
public class ApplicationStartup {

	@Autowired
	private DeviceRepository deviceRepository;
	
	@Autowired
	private MQTTService mqttService;

	@EventListener(ApplicationReadyEvent.class)
	public void onApplicationReady() {

		mqttService.subscribeOnTopics(deviceRepository);
		
	}
}