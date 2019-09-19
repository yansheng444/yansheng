package com.yans.source.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.yans.source.base.BaseService;
import com.yans.source.dao.CheckPointDao;
import com.yans.source.dao.TriggerDao;
import com.yans.source.domain.CheckPoint;
import com.yans.source.domain.Trigger;
import com.yans.source.dto.CheckPointInfoDto;

@Service
public class CheckPointService extends BaseService {

	
	@Autowired
	private CheckPointDao checkPointDao ;
	
	@Autowired
	private TriggerDao triggerDao;
	
	
	/**
	 * 检查点创建
	 * @param c
	 * @return
	 */
	public int create(CheckPoint c){
		
		return checkPointDao.create(c);
		
	}

	
	/**
	 * 检查点创建
	 * @param c
	 * @return
	 */
	public int update(CheckPoint c){
		
		return checkPointDao.update(c);
			
	}
	
	
	public CheckPointInfoDto queryOne(String id) {
		
		
		CheckPointInfoDto queryOne = checkPointDao.queryOne(id);
		
		if(queryOne == null){
			return null;
		}
		
		List<Trigger> queryData = triggerDao.queryData(queryOne.getId().toString(), null, null, null);
		
		
		if(queryData != null){
			queryOne.setTriggers(queryData);
		}
		
		
		return queryOne;
	}
	
	
	
		
	
	
	
	
}
