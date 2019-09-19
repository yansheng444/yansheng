package com.yans.source.controller;

import java.util.List;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.alibaba.druid.util.StringUtils;
import com.yans.source.base.BaseController;
import com.yans.source.base.DataRes;
import com.yans.source.dao.TriggerDao;
import com.yans.source.domain.Trigger;
import com.yans.source.redis.redisUtil;
import com.yans.source.service.TriggerService;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

@RestController
@RequestMapping("trigger")
public class TriggerController extends BaseController {

	@Autowired
	private TriggerDao triggerDao;
	
	@Autowired
	private TriggerService triggerService;
	
	@RequestMapping("data")
	public Object data(String id){
		
		try {
			List<Trigger> queryData = triggerDao.queryData(id, null, null, null);
			if(queryData != null){
				return new DataRes(1000,"success",queryData);
			}else {
				return new DataRes(1001,"无数据");
			}
		} catch (Exception e) {
			logger.error("查询触发器失败：",e);
			return new DataRes(500,"系统错误");
		}
		
	}
	
	
	
	@RequestMapping("hotFront")
	public Object hotFront(String userid){
		
		if(StringUtils.isEmpty(userid) ){
			return new DataRes(400,"参数缺失");
		}
		
		Jedis jedis = null;
		
		try {
			
			JedisPool jedisPool = redisUtil.getJedisPool();
			
			jedis = jedisPool.getResource();
			
			Set<String> zrevrange = jedis.zrevrange("trigger_hot", 0, 3);
			
			if( zrevrange == null ||  zrevrange.size() == 0 ){
				return new DataRes(1001,"无数据");
			}
			
			List<Trigger> queryData = triggerDao.hotData(zrevrange, userid);
			
			if(queryData != null){
				return new DataRes(1000,"success",queryData);
			}else {
				return new DataRes(1001,"无数据");
			}
			
		} catch (Exception e) {
			logger.error("查询触发器失败：",e);
			return new DataRes(500,"系统错误");
		}finally{
			jedis.close();
		}
		
	}
	
	
	@RequestMapping("infoFront")
	public Object info(String id,String userid){
		
		if(StringUtils.isEmpty(id) || StringUtils.isEmpty(userid)){
			return new DataRes(400,"参数缺失");
		}
		
		Jedis jedis = null;
		
		try {
			
			JedisPool jedisPool = redisUtil.getJedisPool();
			
			jedis = jedisPool.getResource();
			
			Trigger queryData = triggerDao.queryOne(Long.valueOf(id),userid);
			
			if(queryData != null){
				
				//增加点击量
				jedis.zincrby("trigger_hot", 1, queryData.getId().toString());
				
				return new DataRes(1000,"success",queryData);
			}else {
				return new DataRes(1001,"无数据");
			}
		} catch (Exception e) {
			logger.error("查询触发器失败：",e);
			return new DataRes(500,"系统错误");
		} finally {
			jedis.close();
		}
		
	}
	
	/**
	 * 查询所有消息项
	 * @param id
	 * @return
	 */
	@RequestMapping("dataFront")
	public Object dataFront(String userid,String search,String groupid){
		
		if(StringUtils.isEmpty(userid) ){
			return new DataRes(400,"参数缺失");
		}
		
		try {
			List<Trigger> queryData = triggerDao.queryDataFromGroup(groupid, null, search, userid);
			if(queryData == null || queryData.size() == 0) {
				return new DataRes(1001,"无数据");
			}else { 
				return new DataRes(1000,"success",queryData);
			}
		} catch (Exception e) {
			logger.error("查询触发器失败：",e);
			return new DataRes(500,"系统错误");
		}
		
	}
	
	
	
	/**
	 * 查询所有消息项
	 * @param id
	 * @return
	 */
	@RequestMapping("selfDataFront")
	public Object selfDataFront(String userid){
		
		if(StringUtils.isEmpty(userid)){
			return new DataRes(400,"参数缺失");
		}
		
		try {
			List<Trigger> queryData = triggerDao.querySelfData(userid);
			if(queryData == null ||  queryData.size() == 0){
				return new DataRes(1001,"无数据");
			}else {
				return new DataRes(1000,"success",queryData);
			}
		} catch (Exception e) {
			logger.error("查询触发器失败：",e);
			return new DataRes(500,"系统错误");
		}
		
	}
	
	
	@RequestMapping("create")
	public Object create(Trigger t){
		
		
		if(t == null || t.getName() == null || t.getCondition_detail() == null ||
				t.getCheck_id() == null || t.getJs_script() == null || t.getIs_right() == null
				|| t.getTemplate() == null || t.getIs_file() == null || t.getDescript() == null
				|| t.getGroup_id() == null || t.getTg_follow() == null || t.getTg_max_redeliv() == null 
				){
			return new DataRes(400,"参数错误");
		}
		
		try {
			int queryData = triggerService.create(t);
			if(queryData > 0){
				return new DataRes(1000,"success",queryData);
			}else {
				return new DataRes(1001,"fail");
			}
		} catch (Exception e) {
			logger.error("update触发器失败：",e);
			return new DataRes(500,"系统错误");
		}
		
	}
	
	@RequestMapping("delete")
	public Object delete(HttpServletRequest request){
		
		String parameter = request.getParameter("id");
		
		if(parameter == null){
			return new DataRes(400,"参数错误");
		}
		
		try {
			int queryData = triggerDao.delete(Long.valueOf(parameter));
			if(queryData > 0){
				return new DataRes(1000,"success",queryData);
			}else {
				return new DataRes(1001,"fail");
			}
		} catch (Exception e) {
			logger.error("delete触发器失败：",e);
			return new DataRes(500,"系统错误");
		}
		
	}
	
	
	@RequestMapping("update")
	public Object update(Trigger t){
		
		if(t == null || t.getId() == null
				){
			return new DataRes(400,"参数错误");
		}
		
		try {
			int queryData = triggerService.update(t);
			if(queryData > 0){
				return new DataRes(1000,"success",queryData);
			}else {
				return new DataRes(1001,"fail");
			}
		} catch (Exception e) {
			logger.error("add触发器失败：",e);
			return new DataRes(500,"系统错误");
		}
		
	}
	
}
