package com.yans.source.controller;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.alibaba.druid.util.StringUtils;
import com.yans.source.base.BaseController;
import com.yans.source.base.DataRes;
import com.yans.source.base.Page;
import com.yans.source.dao.CheckPointDao;
import com.yans.source.domain.CheckPoint;
import com.yans.source.dto.CheckGroupDto;
import com.yans.source.dto.CheckPointDto;
import com.yans.source.dto.CheckPointInfoDto;
import com.yans.source.service.CheckPointService;

@RestController
@RequestMapping("checkPoint")
public class CheckPointController extends BaseController {

	@Autowired
	private CheckPointService checkPointService;
	
	@Autowired
	private CheckPointDao checkPointDao;
	
	@RequestMapping("create")
	public Object create(CheckPoint c){
	
		if(c == null || c.getName() == null || c.getCondition() == null ||
				 c.getGroup_id() == null || c.getCp_collector() == null ||
				 c.getCp_etl_batch() == null
				){
			return new DataRes(400,"参数错误");
		}
		
		try{
			
			int create = checkPointService.create(c);
			
			if(create > 0){
				return new DataRes(1000,"success");
			}else{
				return new DataRes(1001,"fail");
			}
			
		}catch(Exception e){
			logger.error("创建检查点出错：",e);
			return new DataRes(500,"error");
		}
	}
		
	
	
	@RequestMapping("data")
	public Object data(@RequestBody CheckPoint c,@RequestBody Page p){
		
		try {
			List<CheckPointDto> queryData = checkPointDao.queryData(c, p);
			
			if(queryData == null){
				return new DataRes(1001,"无数据");
			}else {
				return new DataRes(1000,"success",queryData);
			}
			
			
		} catch (Exception e) {
			logger.error("查询检查点错误：",e);
			return new DataRes(500,"系统错误");
		}
		
	}
	
	
	@RequestMapping("mydata")
	public Object owenerdata(HttpServletRequest request){
		
		String userid = request.getParameter("userid");
		
		if(StringUtils.isEmpty(userid)){
			return new DataRes(400,"参数缺失");
		}
		
		try {
			List<CheckPointDto> queryData = checkPointDao.queryOwenerData(userid);
			
			if(queryData == null){
				return new DataRes(1001,"无数据");
			}else {
				return new DataRes(1000,"success",queryData);
			}
			
			
		} catch (Exception e) {
			logger.error("查询错误：",e);
			return new DataRes(500,"系统错误");
		}
		
	}
	
	
	@RequestMapping("info")
	public Object info(HttpServletRequest request){
		
		String id = request.getParameter("id");
		
		if(StringUtils.isEmpty(id)){
			return new DataRes(400,"参数缺失");
		}
		
		try{
			
			CheckPointInfoDto queryAll = checkPointService.queryOne(id);
			
			if(queryAll == null  ) {
				return new DataRes(1001,"暂无数据");
			}else {
				return new DataRes(1000,"success",queryAll);
			}
			
		}catch(Exception e){
			logger.error("查询失败",e);
			
			return new DataRes(500,"error");
		}
	}
	
	
	@RequestMapping("delete")
	public Object delete(HttpServletRequest request){
		
		String id = request.getParameter("id");
		
		if(StringUtils.isEmpty(id)){
			return new DataRes(400,"参数缺失");
		}
		
		try{
			
			int queryAll = checkPointDao.delete(Long.valueOf(id));
			
			if(queryAll <= 0  ) {
				return new DataRes(1001,"删除失败");
			}else {
				return new DataRes(1000,"success");
			}
			
		}catch(Exception e){
			logger.error("查询失败",e);
			
			return new DataRes(500,"error");
		}
	}
	
	
	@RequestMapping("update")
	public Object update(CheckPoint c){
		
		
		if(c == null || c.getId() == null){
			return new DataRes(400,"参数缺失");
		}
		
		try{
			int queryAll = checkPointService.update(c);
			
			if(queryAll == 1) {
				return new DataRes(1000,"success",queryAll);
			}else{
				return new DataRes(1001,"未知错误");
			}
			
		}catch(Exception e){
			logger.error("查询失败",e);
			return new DataRes(500,"error");
		}
	}
	
	
	
	@RequestMapping("listAfter")
	public Object listAfter(){
		
		try {
			List<CheckPoint> queryData = checkPointDao.queryList();
			
			if(queryData == null){
				return new DataRes(1001,"无数据");
			}else {
				
				
				
				Set<String> set = new HashSet<String>();
				
				for(CheckPoint  cp : queryData){
					set.add(cp.getGroup_name());
				}
				
				Iterator<String> iterator = set.iterator();
				List<CheckGroupDto> arr = new ArrayList<CheckGroupDto>();
				
				while(iterator.hasNext()){
					
					String next = iterator.next();
					CheckGroupDto cgd = new CheckGroupDto();
					List<CheckPoint> cplist = new ArrayList<CheckPoint>();
					for(CheckPoint  cp : queryData){
						
						if(next.equals(cp.getGroup_name()) ){
							cgd.setGroup_id(cp.getGroup_id().toString());
							cgd.setGroup_name(cp.getGroup_name());
							if(cp.getName() != null){
								cplist.add(cp);
							}
						}
						
					}
					cgd.setCheckPointList(cplist);
					
					arr.add(cgd);
				}
				
				
				
				return new DataRes(1000,"success",arr);
			}
			
			
		} catch (Exception e) {
			logger.error("查询检查点错误：",e);
			return new DataRes(500,"系统错误");
		}
		
	}
	
	
	
}
