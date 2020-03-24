package database;
/*
 *	 Nikhil Vaidya
 */
import java.sql.Timestamp;
import java.util.TimeZone;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

import com.ocr.database.service.OCRSvc;

public class OCRTest {

	private static OCRSvc ocrSvc;
	
	@BeforeClass
	public static void setup() {
		ocrSvc = new OCRSvc();
	}
	
	@AfterClass
	public static void teardown() {
		ocrSvc = null;
	}
	
	private Timestamp getTimeStamp () {
		java.util.Date date = new java.util.Date();
		java.text.SimpleDateFormat sdf = new java.text.SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSSSSS");
		sdf.setTimeZone(TimeZone.getTimeZone("UTC"));
		String time = sdf.format(date);
		return Timestamp.valueOf(time);
	}
	
	@Test
	public void testInsert() {
		try {
			System.out.println();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
