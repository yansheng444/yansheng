package com.zg.dao;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.zg.BaseTest;
import com.zg.domain.ContentConfig;
import com.zg.domain.Source;

public class ContentConfigDaoTest extends BaseTest {

	@Autowired
	private ContentConfigDao contentConfigDao;
	
	
	@Test
	public void test() {
		
		List<ContentConfig> arr = new ArrayList<>();
		
		ContentConfig cc = new ContentConfig();
		cc.setChannel_id(1);
		cc.setChannel_name("小米");
		cc.setSource_name("百度");
		cc.setPercent(40);
		cc.setType(1);
		arr.add(cc);
		int batchCreate = contentConfigDao.batchCreate(arr);
		System.out.println(batchCreate);
		
		List<Source> queryFromChannel = contentConfigDao.queryFromChannel("1");
		
		System.out.println(queryFromChannel.size());
		
		
		
	}

}
