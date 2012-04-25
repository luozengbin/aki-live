package com.appspot.piiman.client;

import java.io.IOException;

import net.arnx.jsonic.JSON;
import net.arnx.jsonic.JSONException;

import com.appspot.piment.api.tqq.model.MessageResponse;
import com.appspot.piment.api.tqq.model.TimelineResponse;

public class JSONTest001 {

	/**
	 * @param args
	 * @throws IOException
	 * @throws JSONException
	 */
	public static void main(String[] args) throws JSONException, IOException {

		// MessageResponse messageResponse =
		// JSON.decode(JSONTest001.class.getClassLoader().getResourceAsStream("com/appspot/piiman/client/tqq_status.json"),
		// MessageResponse.class);
		// System.out.println(JSON.encode(messageResponse, true));

		// TimelineResponse timelineResponse =
		// JSON.decode(JSONTest001.class.getClassLoader().getResourceAsStream("com/appspot/piiman/client/222.json"),
		// TimelineResponse.class);
		// System.out.println(JSON.encode(timelineResponse, true));

		// TimelineResponse timelineResponse =
		// JSON.decode("{\"data\":null,\"errcode\":9,\"msg\":\"check sign error\",\"ret\":3}",TimelineResponse.class);
		// System.out.println(JSON.encode(timelineResponse, true));


	}
}
