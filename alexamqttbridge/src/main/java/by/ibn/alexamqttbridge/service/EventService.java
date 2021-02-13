package by.ibn.alexamqttbridge.service;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import by.ibn.alexamqttbridge.resources.Request;
import by.ibn.alexamqttbridge.resources.Response;

@Service
public class EventService {

	Logger log = LoggerFactory.getLogger(EventService.class);
	
	@Autowired
	private List<? extends EventProcessor> eventProcessors; 

	public Response processEvent(Request request) {

		for (EventProcessor eventProcessor: eventProcessors)
		{
			if (eventProcessor.isProcessable(request))
			{
				log.trace("Processor {}: processable", eventProcessor.getClass().getName());
				
				return eventProcessor.process(request);
			}
			
			log.trace("Processor {}: not processable", eventProcessor.getClass().getName());
		}
		
		if (request != null && request.directive != null && request.directive.header != null) {
			log.warn("Received request for {} interface with {} directive with payloadVersion {}, but no appropriate processor has been found. "
					+ "Either ignore, or implement support.", request.directive.header.namespace, request.directive.header.name, request.directive.header.payloadVersion);
		} else {
			log.warn("Unsupported request. Ignoring.");
		}

		return null;
	}

}
