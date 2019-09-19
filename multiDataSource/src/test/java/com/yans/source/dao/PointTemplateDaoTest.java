package com.yans.source.dao;

import static org.junit.Assert.*;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.yans.source.BaseTest;
import com.yans.source.domain.PointTemplate;

public class PointTemplateDaoTest extends BaseTest {

	@Autowired
	private PointTemplateDao pointTemplateDao;
	
	@Test
	public void testCreate() {
		PointTemplate p = new PointTemplate();
		p.setMessage("1");
		p.setTrigger_id(22l);
		int create = pointTemplateDao.create(p);
		System.err.println(create);
		assertTrue(create > 0 );
	}

}
