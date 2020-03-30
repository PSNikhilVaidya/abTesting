package com.abtesting.database.service;
/*
 *	 Nikhil Vaidya
 */
import java.sql.SQLException;

import org.apache.ibatis.session.SqlSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.abtesting.database.mapper.ABTestingMapper;
import com.abtesting.database.mapper.MyBatis;
import com.abtesting.models.ABTesting;


public class ABTestingSvc {

	final static Logger logger = LoggerFactory.getLogger(ABTestingSvc.class);

	public Integer insert(ABTesting abTesting)  throws SQLException{

		SqlSession sqlSession = MyBatis.getSqlSessionFactory().openSession();
		Integer lead_id = null;
		try {
			ABTestingMapper mapper = sqlSession.getMapper(ABTestingMapper.class);
			mapper.insert(abTesting);
			sqlSession.commit();
		}finally {
			sqlSession.close();
		}
		return lead_id;
	}
	
	public void update()  throws SQLException{

		SqlSession sqlSession = MyBatis.getSqlSessionFactory().openSession();
		try {
			ABTestingMapper mapper = sqlSession.getMapper(ABTestingMapper.class);
			mapper.update();
			sqlSession.commit();
		}finally {
			sqlSession.close();
		}
	}
}