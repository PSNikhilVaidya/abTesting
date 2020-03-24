package com.ocr.database.mapper;
/*
 *	 Nikhil Vaidya
 */
import java.sql.Timestamp;

import org.apache.ibatis.annotations.Param;

import com.ocr.models.OCR;

public interface OCRMapper {

	public Integer insertOCR(OCR ocr);
	
	public void updateOCRStatus(@Param("lead_id")int lead_id, @Param("status")String status, @Param("status_details")String status_details, 
								 @Param("updated_on")Timestamp updated_on);

}
