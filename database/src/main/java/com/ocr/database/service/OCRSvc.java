package com.ocr.database.service;
/*
 *	 Nikhil Vaidya
 */
import java.sql.SQLException;
import java.sql.Timestamp;

import org.apache.ibatis.session.SqlSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.ocr.database.mapper.MyBatis;
import com.ocr.database.mapper.OCRMapper;
import com.ocr.models.OCR;


public class OCRSvc {

	final static Logger logger = LoggerFactory.getLogger(OCRSvc.class);

	public Integer insertLead(OCR ocr)  throws SQLException{

		SqlSession sqlSession = MyBatis.getSqlSessionFactory().openSession();
		Integer lead_id = null;
		try {
			OCRMapper leadInterface = sqlSession.getMapper(OCRMapper.class);
			leadInterface.insertOCR(ocr);
			sqlSession.commit();
		}finally {
			sqlSession.close();
		}
		return lead_id;
	}
	
	public void updateLeadStatus(int lead_id, String status, String status_details, Timestamp updated_at)  throws SQLException{

		SqlSession sqlSession = MyBatis.getSqlSessionFactory().openSession();
		try {
			OCRMapper leadInterface = sqlSession.getMapper(OCRMapper.class);
			leadInterface.updateOCRStatus(lead_id, status, status_details, updated_at);
			sqlSession.commit();
		}finally {
			sqlSession.close();
		}
	}
}