package com.zg.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.zg.base.BaseController;
import com.zg.base.LogProvider;
import com.zg.dao.SourceDao;
import com.zg.domain.Source;
import com.zg.dto.BaseRes;
import com.zg.dto.DataRes;

@RestController
@RequestMapping("source")
public class SourceController extends BaseController implements LogProvider {
	
	@Autowired
	private SourceDao sourceDao;
	
	@RequestMapping("list")
	public Object queryAll(){
		
		DataRes dr = new DataRes();
		
		try{
			
			List<Source> queryAll = sourceDao.queryAll();
			
			dr.setCode(200);
			dr.setMsg("SUCCESS");
			dr.setData(queryAll);
		
		}catch(Exception e){
			logger.error("获取所有来源：",e);
			return new BaseRes(500,"请求资源处问题，请刷新页面");
		}
		
		return dr;
	}
	
}
