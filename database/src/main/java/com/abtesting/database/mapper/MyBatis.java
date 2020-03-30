package com.abtesting.database.mapper;
/*
 *	 Nikhil Vaidya
 */
import java.io.IOException;
import java.io.Reader;

import org.apache.ibatis.io.Resources;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;

public class MyBatis {

	private static SqlSessionFactory factory;

	private MyBatis() {
	}

	static {
		Reader reader = null;
		try {
			reader = Resources.getResourceAsReader("mybatis-config.xml");
		} catch (IOException e) {
			throw new RuntimeException(e.getMessage());
		}
		try {
			factory = new SqlSessionFactoryBuilder().build(reader);
		} catch (Exception e) {
			System.out.print(e.getMessage());
			throw new RuntimeException(e.getMessage());
		}

	}

	public static SqlSessionFactory getSqlSessionFactory() {
		return factory;
	}
}
