package com.ocr.rest.apis.serviceImpl;
/*
 *	 Nikhil Vaidya
 */
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.sql.Timestamp;
import java.util.List;
import java.util.ListIterator;
import java.util.concurrent.atomic.AtomicBoolean;

import javax.activation.DataHandler;
import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import org.apache.cxf.helpers.IOUtils;
import org.apache.cxf.jaxrs.ext.multipart.Attachment;
import org.apache.cxf.jaxrs.ext.multipart.ContentDisposition;
import org.apache.cxf.jaxrs.ext.multipart.MultipartBody;
import org.apache.cxf.message.Message;
import org.apache.cxf.phase.PhaseInterceptorChain;
import org.apache.cxf.transport.http.AbstractHTTPDestination;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.los.document.DocumentConstants;
import com.los.document.DocumentHelper;
import com.los.time.TimeStamp;
import com.los.utility.AWSS3Helper;
import com.los.utility.Constants;
import com.los.utility.EnvCheck;
import com.ocr.rest.apis.serviceInterfaces.DocumentService;
import com.ocr.rest.apis.util.GenericResponse;
import com.ocr.rest.apis.util.ResponseUtility;

@Service("documentservices")
public class DocumentServiveImpl implements DocumentService{

	
	Gson gson = (new GsonBuilder()).create();
	
	private static final Logger logger = LoggerFactory.getLogger(DocumentServiveImpl.class);
	
	@Override
	public Response uploadDocuments(MultipartBody multipartBody) {
		if (!validateToken(false)) {
			logger.debug("uploadDocuments: invalid token");
			return ResponseUtility.constructBadRequestResponse(Constants.REASON_INVALID_TOKEN, Constants.REASON_INVALID_TOKEN_CODE);
		}
		
		logger.info(">>>>> uploadDocuments Enter");

		List<Attachment> attachments = multipartBody.getAllAttachments();
		ListIterator<Attachment> listIter = attachments.listIterator();
		String lead_id = null;
		DataHandler dataHandler = null;
		
		Timestamp timeStamp = TimeStamp.getTimeStampUTC();
		
		InputStream docStream = null;
		String doc_name = null;
		String password = null;
		String doc_type_id = null;
		String content_type = null;
		String original_name = null;
		for (int i = 0; i < attachments.size() ; i++) {
			Attachment att =  listIter.next();
			ContentDisposition dis = att.getContentDisposition();
			String paramName = dis.getParameter("name");

			dataHandler = att.getDataHandler();
			try {
				switch (paramName.toLowerCase()) {
				case DocumentConstants.PARAM_LEAD_ID:
					if (dataHandler.getInputStream() != null) {
						lead_id = IOUtils.toString(dataHandler.getInputStream(), DocumentConstants.ENCODING);
						
					}
					break;
				case DocumentConstants.PARAM_DOC_TYPE_ID:
					doc_type_id = IOUtils.toString(dataHandler.getInputStream(), DocumentConstants.ENCODING);
					
					break;
				case DocumentConstants.PARAM_DOCUMENT:
					docStream = att.getObject(InputStream.class);
					if (dataHandler.getName() != null) {
						original_name = dataHandler.getName();
						original_name = original_name.replaceAll("[&\\/\\\\#,+()$~%'^!\\[\\]\":*?<>{}]","");
					}
					if (dataHandler.getContentType() != null) {
						content_type = dataHandler.getContentType();
					}
					break;
				case DocumentConstants.PARAM_DOC_NAME:
					doc_name = IOUtils.toString(dataHandler.getInputStream(), DocumentConstants.ENCODING).toUpperCase();
					break;
				case DocumentConstants.PARAM_DOC_PWD:
					password = IOUtils.toString(dataHandler.getInputStream(), DocumentConstants.ENCODING);
					String pwd = password.replaceAll("[&\\/\\\\#,+()$~%'^!\\[\\]\":*?<>{}]","");
					
					break;
				default:
					break;
				}
			} catch (Exception e) {
				logger.info("Failed to read data from MultiPart body for lead id : " + lead_id);
				logger.error("Exception:",e);
				return Response.status(Status.INTERNAL_SERVER_ERROR).build();
			}
		}

		
		//store inputstream to bytearrayoutputtstream; bcz we need input stream multiple time
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		byte[] buf = new byte[1024];
		int n = 0;
		try {
			while ((n = docStream.read(buf)) >= 0)
				baos.write(buf, 0, n);
		} catch (Exception e) {
			logger.info("Failed to convert inputStream to bytearray for doc : " + doc_name);
			logger.error("Exception:",e);
			return Response.status(Status.INTERNAL_SERVER_ERROR).build();
		}
		byte[] content = baos.toByteArray();

		AtomicBoolean failedToReadMetaData = new AtomicBoolean();
		DocumentHelper docHelper = new DocumentHelper();
		
		failedToReadMetaData.set(false);
		Boolean isValidFile = null;
		if (content != null) {
			try {	
				if (content_type.equalsIgnoreCase(DocumentConstants.CONTENT_TYPE_PDF)) {
					isValidFile = true;//docHelper.openPDF(content, documentData);
				} else {
					// for other content type isValidFile = true
					isValidFile = true;
				}
			} catch (Exception e) {
				logger.info("Failed to open doc : " + doc_name +" for  lead : " + lead_id);
				logger.error("Exception:",e);
				return Response.status(Status.INTERNAL_SERVER_ERROR).build();
			}
		} else {
			logger.error("Invalid file: Null Document");
			return ResponseUtility.constructBadRequestResponse(DocumentConstants.DOCUMENT_UPLOAD_FAILED_NULL_DOCUMENT, DocumentConstants.DOCUMENT_UPLOAD_FAILED_NULL_DOCUMENT_CODE);
		}

		if (isValidFile != null && isValidFile) {
			try {

				AWSS3Helper s3Helper = new AWSS3Helper();
				String filePath = s3Helper.uploadFileToBucket(new ByteArrayInputStream(content), original_name, content_type, content.length, EnvCheck.isSandboxEnv(), lead_id, doc_name);
				if (filePath != null) {
					
				} else {
					logger.info("Unknown: Failed to upload document to S3 : " + doc_name +" for  lead : " + lead_id);
					return Response.status(Status.INTERNAL_SERVER_ERROR).build();
				}
			} catch (Exception e) {
				logger.info("Failed to upload document to S3 : " + doc_name +" for  lead : " + lead_id);
				logger.error("Exception:",e);
				return Response.status(Status.INTERNAL_SERVER_ERROR).build();
			}

			try {
				String checksum = docHelper.calculateHashFile(content, DocumentConstants.HASH_ALOG_MD5);
				if (checksum != null) {
					
				}
			} catch (Exception e) {
				logger.info("Failed while calculating checksum of file for doc : " + doc_name +" for  lead : " + lead_id);
				logger.error("Exception:",e);
			}

			
		} else {
			logger.error("Invalid file: Unable to open file");
			return ResponseUtility.constructBadRequestResponse(DocumentConstants.UPLOAD_DOC_FAILED_INVALID_DOCUMENT, DocumentConstants.UPLOAD_DOC_FAILED_INVALID_DOCUMENT_CODE);
		}

		GenericResponse responseBean = new GenericResponse();
		responseBean.setStatus(Constants.SUCCESS);
		logger.info("<<<<< uploadDocuments Exit");
		String jsonResp = gson.toJson(responseBean);
		return Response.ok().entity(jsonResp).build();
	}

	//repeated code
	//extract and validate token 
	private boolean validateToken(boolean userToken) {
		String token = "";
		try {
			Message message = PhaseInterceptorChain.getCurrentMessage();
			HttpServletRequest request = (HttpServletRequest)message.get(AbstractHTTPDestination.HTTP_REQUEST);
			if (request != null) {
				token = request.getHeader("token");	
			}
		} catch (Exception e) {
			logger.error("failed to read token");
			logger.error("Exception:",e);
		}
		
		return isValidToken(token);
	}

	private boolean isValidToken(String token) {
		boolean isValid = false;
		if (token != null && !token.isEmpty() && token.equals(Constants.PAYSENSE_TOKEN)) {
			isValid = true;
		}
		return isValid;
	}
}
