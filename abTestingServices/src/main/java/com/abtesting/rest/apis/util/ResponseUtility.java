package com.abtesting.rest.apis.util;
/*
 *	 Nikhil Vaidya
 */
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import com.google.gson.Gson;

public class ResponseUtility {
	
	public static Response constructBadRequestResponse(String reason, int reason_code) {
		GenericResponse response = new GenericResponse();
		response.setStatus(false);
		response.setReason(reason);
		response.setReasonCode(reason_code);
		Gson gson = new Gson();
		String respJson = gson.toJson(response);
		return Response.status(Status.BAD_REQUEST).entity(respJson).build();
	}
	
}
