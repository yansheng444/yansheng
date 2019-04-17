package com.zg.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.zg.base.BaseService;
import com.zg.dao.ChannelDao;
import com.zg.dao.ContentConfigDao;
import com.zg.domain.Channel;
import com.zg.dto.ChannelDto;

@Service
public class ChannelService extends BaseService{
	
	@Autowired
	private ChannelDao channelDao;
	
	@Autowired
	private ContentConfigDao contentConfigDao;
	
	public List<Channel> queryData(){
		return channelDao.queryAll();
	}
	
	@Transactional
	public int create(ChannelDto cd){
		
		int code = 0 ;
		
		try{
			
			int queryByName = channelDao.queryByName(cd.getChannel().getName());
			
			if(queryByName == 0){
				return -2;//重名
			}
			
			int queryMaxSort = channelDao.queryMaxSort();
			cd.getChannel().setSort(queryMaxSort + 1);
			
			int create = channelDao.create(cd.getChannel());
			
			cd.getContentConfig().add(cd.getBackupConfig());
			
			cd.getContentConfig().forEach(f -> f.setChannel_id(create));
			
			int batchCreate = contentConfigDao.batchCreate(cd.getContentConfig());
			
			if(create > 0 && batchCreate > 0){
				code = 1;
			}
			
		}catch(Exception e){
			logger.error("创建channel失败：",e);
			code = -1;
		}
		
		return code;
	}

	@Transactional
	public int updateSort(List<Channel> c){
		return channelDao.batchUpdate(c);
	}
	
	public int update(Channel c){
		return channelDao.update(c);
	}
	
	public int queryByName(String name){
		return channelDao.queryByName(name);
	}
	
	
	
	
	
	
	
	
}
