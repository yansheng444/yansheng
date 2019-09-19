package com.yans.source.controller;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.yans.source.base.BaseController;
import com.yans.source.base.DataRes;
import com.yans.source.dao.GroupDao;
import com.yans.source.domain.Group;

@RestController
@RequestMapping("group")
public class GroupController extends BaseController {

	@Autowired
	private GroupDao groupDao;
	
	@RequestMapping("data")
	public Object data(HttpServletRequest request){
		
		try{
			
			List<Group> queryAll = groupDao.queryAll();
			
			if(queryAll == null || queryAll.size() == 0 ) {
				return new DataRes(1001,"暂无数据");
			}else {
				return new DataRes(1000,"success",queryAll);
			}
			
		}catch(Exception e){
			logger.error("查询失败",e);
			return new DataRes(500,"error");
		}
	}
	
	
	
}
