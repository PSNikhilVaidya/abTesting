package com.abtesting.rest.apis.serviceImpl;
/*
 *	 Nikhil Vaidya
 */
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.abtesting.rest.apis.serviceInterfaces.ABTestingService;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

@Service("abtestingservices")
public class ABTestingServiveImpl implements ABTestingService{

	
	Gson gson = (new GsonBuilder()).create();
	
	private static final Logger logger = LoggerFactory.getLogger(ABTestingServiveImpl.class);
	
	
}
