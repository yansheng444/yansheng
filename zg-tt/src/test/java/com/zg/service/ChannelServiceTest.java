package com.zg.service;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.zg.BaseTest;
import com.zg.domain.Channel;
import com.zg.domain.ContentConfig;
import com.zg.dto.ChannelDto;

public class ChannelServiceTest extends BaseTest {

	@Autowired
	private ChannelService channelService;
	
	@Test
	public void test() {
		
//		ChannelDto cd = new ChannelDto();
//		
//		Channel c = new Channel();
//		c.setName("bb");
//		
//		ContentConfig cc = new ContentConfig();
//		cc.setChannel_name("小米");
//		cc.setSource_name("百度");
//		cc.setPercent(20);
//		cc.setType(2);;
//		
//		List<ContentConfig> arr = new ArrayList<ContentConfig>();
//		ContentConfig cc2 = new ContentConfig();
//		cc2.setChannel_name("小米");
//		cc2.setSource_name("百度");
//		cc2.setType(1);
//		cc2.setPercent(80);
//		arr.add(cc2);
//		
//		cd.setBackupConfig(cc);
//		cd.setChannel(c);
//		cd.setContentConfig(arr);
//		int create = channelService.create(cd);
//		System.out.println(create);
	}

}
