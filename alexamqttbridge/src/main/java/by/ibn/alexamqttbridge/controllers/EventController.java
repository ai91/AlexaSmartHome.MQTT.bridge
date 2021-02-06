package by.ibn.alexamqttbridge.controllers;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import by.ibn.alexamqttbridge.controllers.service.EventService;
import by.ibn.alexamqttbridge.resources.Request;
import by.ibn.alexamqttbridge.resources.Response;

/**
 * Entry point for events from AWS Lambda
 *
 * @author Anar Ibragimoff
 *
 */
@RestController
public class EventController {

	Logger log = LoggerFactory.getLogger(EventController.class);

	@Autowired
	private EventService service;

	@RequestMapping(
			path = "/api/events", 
			method = RequestMethod.POST, 
			consumes = MediaType.APPLICATION_JSON_VALUE, 
			produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<Response> event(@RequestBody Request request) {

		try {

			log.trace("Processing request");

			Response response = service.processEvent(request);
			if (response == null) {
				return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
			}

			return new ResponseEntity<Response>(response, HttpStatus.OK);
		} catch (Exception e) {

			log.error(e.toString(), e);

			return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

}
