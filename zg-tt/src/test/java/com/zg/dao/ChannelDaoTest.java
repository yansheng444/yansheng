package com.zg.dao;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.zg.BaseTest;
import com.zg.domain.Channel;


public class ChannelDaoTest extends BaseTest {

	@Autowired
	private ChannelDao channelDao;
	
	@Test
	public void testCreate() {
		
		Channel c = new Channel();
//		c.setName("test");
//		c.setSort(1);
//		int create = channelDao.create(c);
//		System.out.println(create);
		
//		List<Channel> queryAll = channelDao.queryAll();
//		
//		System.out.println(queryAll.size());
		
//		c.setId(1);
//		c.setName("aaa");
//		
//		int update = channelDao.update(c);
//		System.out.println(update);
		
//		int queryMaxSort = channelDao.queryMaxSort();
//		
//		System.out.println(queryMaxSort);
//		
//		int updateSort = channelDao.updateSort(-1, -1);
//		
//		System.out.println(updateSort);
		
//		c.setId(1);
//		c.setSort(2);
//		
//		List<Channel> arr = new ArrayList<>();
//		
//		arr.add(c);
//		
//		int batchUpdate = channelDao.batchUpdate(arr);
//		System.out.println(batchUpdate);
		
		
	}


}
