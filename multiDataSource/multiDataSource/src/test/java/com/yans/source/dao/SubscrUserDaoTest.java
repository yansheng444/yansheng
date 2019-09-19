package com.yans.source.dao;

import static org.junit.Assert.*;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.google.gson.Gson;
import com.yans.source.BaseTest;
import com.yans.source.domain.SubscrUser;

public class SubscrUserDaoTest extends BaseTest {

	
	@Autowired
	private SubscrUserDao subscrUserDao;
	
	@Test
	public void testQueryOne() {
		SubscrUser queryOne = subscrUserDao.queryOne("8ad7d1e8-de5a-477a-a92c-97b923ae5f93");
		
		Gson json = new Gson();
		System.out.println(json.toJson(queryOne));
	}

}
