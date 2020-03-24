package com.ocr.rest.apis.serviceInterfaces;
/*
 *	 Nikhil Vaidya
 */
import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Response;

import org.apache.cxf.jaxrs.ext.multipart.MultipartBody;

import com.wordnik.swagger.annotations.Api;
import com.wordnik.swagger.annotations.ApiOperation;
import com.wordnik.swagger.annotations.ApiResponse;
import com.wordnik.swagger.annotations.ApiResponses;

@Path("/document")

@Api(value="/document", description = "All APIs related to document")
public interface DocumentService {

	@Path("/upload")
	@POST
	@Produces("application/json")
	@Consumes("multipart/form-data")	
	@ApiOperation(value = "Upload documents API", notes = "This API is to upload documents")
	@ApiResponses(value = { @ApiResponse(code = 200, message = "OK"),
	@ApiResponse(code = 400, message = "Bad Request"),
	@ApiResponse(code = 404, message = "Resource Not Found"),
	@ApiResponse(code = 500, message = "Internal Server Error") })
	Response uploadDocuments(MultipartBody multipartBody);
	
}
