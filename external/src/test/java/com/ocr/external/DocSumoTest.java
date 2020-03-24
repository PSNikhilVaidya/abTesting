package com.ocr.external;
import com.external.docsumo.DocsumoHelper;

public class DocSumoTest {

	public static void main(String[] args) {
		try {
			DocsumoHelper amlHelper = new DocsumoHelper(17);
			amlHelper.call();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
