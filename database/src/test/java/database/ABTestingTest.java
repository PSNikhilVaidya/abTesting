package database;
/*
 *	 Nikhil Vaidya
 */
import java.sql.Timestamp;
import java.util.TimeZone;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

import com.abtesting.database.service.ABTestingSvc;

public class ABTestingTest {

	private static ABTestingSvc abTestingSvc;
	
	@BeforeClass
	public static void setup() {
		abTestingSvc = new ABTestingSvc();
	}
	
	@AfterClass
	public static void teardown() {
		abTestingSvc = null;
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
