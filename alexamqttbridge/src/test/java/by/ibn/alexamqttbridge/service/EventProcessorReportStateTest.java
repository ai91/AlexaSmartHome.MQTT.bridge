package by.ibn.alexamqttbridge.service;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.HashMap;

import org.junit.jupiter.api.Test;

class EventProcessorReportStateTest {

	@Test
	void castValue() {
		
		EventProcessorReportState testee = new EventProcessorReportState();
		
		assertEquals(null, testee.castValue(null));
		assertEquals("test", testee.castValue("test"));
		
		HashMap<String, Object> expectedMap = new HashMap<String, Object>();
		expectedMap.put("h", Integer.valueOf(100));
		expectedMap.put("s", Double.valueOf(20.1d));
		expectedMap.put("v", "13");
		assertEquals(expectedMap, testee.castValue("{\"h\":100,\"s\":20.1,\"v\":\"13\"}"));
		
	}

}
