package com.external.docsumo;
import java.util.concurrent.Callable;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.context.support.SpringBeanAutowiringSupport;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.ocr.database.service.OCRSvc;

public class DocsumoHelper implements Callable<Boolean>{
	
	private OCRSvc leadSvc;
	
	private Integer leadId;
	
	private static final Logger logger = LoggerFactory.getLogger(DocsumoHelper.class);

	Gson gson = (new GsonBuilder()).create();

	public DocsumoHelper(int leadId) {
		super();
		SpringBeanAutowiringSupport.processInjectionBasedOnCurrentContext(this);
		
		this.leadId = leadId;
		leadSvc = new OCRSvc();
	}
	
	@Override
	public Boolean call() throws Exception {
		return somemethod();
	}
	
	private boolean somemethod() {
		return true;
	}
	
	public static void main(String[] args) {
		try {
			DocsumoHelper amlHelper = new DocsumoHelper(24);
			amlHelper.somemethod();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}