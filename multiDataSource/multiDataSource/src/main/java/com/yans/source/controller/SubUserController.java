package com.yans.source.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.alibaba.druid.util.StringUtils;
import com.google.gson.Gson;
import com.yans.source.base.BaseController;
import com.yans.source.base.DataRes;
import com.yans.source.base.DataResponse;
import com.yans.source.dao.SubscrUserDao;
import com.yans.source.domain.SubscrUser;
import com.yans.source.redis.redisUtil;
import com.yans.source.util.HttpUtil;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

@RestController
@RequestMapping("subuser")
public class SubUserController extends BaseController {
	
	@Autowired
	private SubscrUserDao subscrUserDao;
	

	
	@RequestMapping("delete")
	public Object delete(HttpServletRequest request){
		
		
		String userid = request.getParameter("userid");
		String point_id = request.getParameter("point_id");
		
		if(StringUtils.isEmpty(userid) || StringUtils.isEmpty(point_id)){
			return new DataRes(400,"参数缺失");
		}
		
		try{
			
			SubscrUser su = new SubscrUser();
			su.setTrigger_id(Long.valueOf(point_id));
			su.setUser_id(userid);
			int create = subscrUserDao.create(su);
			
			if(create == 0 ) {
				return new DataRes(1001,"未知失败");
			}else {
				return new DataRes(1000,"success");
			}
			
		}catch(Exception e){
			logger.error("创建失败",e);
			return new DataRes(500,"error");
		}
		
		
	}
	
	//申请消息通知
	@RequestMapping("apply")
	public Object create(HttpServletRequest request){
		
		String userid = request.getParameter("userid");
		String point_id = request.getParameter("point_id");
		String point_name = request.getParameter("point_name");
		
		if(StringUtils.isEmpty(userid) || StringUtils.isEmpty(point_id) || StringUtils.isEmpty(point_name)
				|| userid.equals("null") || userid.equals("NULL")
				){
			return new DataRes(400,"参数缺失");
		}
		
		Gson json = new Gson();
		Jedis resource = null;
		try{
			
			SubscrUser su = new SubscrUser();
			su.setTrigger_id(Long.valueOf(point_id));
			su.setUser_id(userid);
			SubscrUser check = subscrUserDao.check(su);
			
			if(check != null){
				return new DataRes(1001,"已经存在的订阅关系，请不要频繁点击");
			}
			
			Map<String, String> maps = new HashMap<>();
			
			JedisPool jedisPool = redisUtil.getJedisPool();
			resource = jedisPool.getResource();
			
			//高管免审批
			List<String> lrange = resource.lrange("top_manager", 0, -1);
			
			if(lrange == null || lrange.size() == 0){
				
			}else {
				
				for(String s : lrange){
					
					if(s.equals(userid)){
						su.setStatus(2);
						int create = subscrUserDao.create(su);
				
						if(create < 0 ){
							return new DataRes(1001,"fail");
						}else {
							maps.put("userid", "30947");
							maps.put("msg", userid + ":高管申请订阅了");
							String httpGetRequest = HttpUtil.HttpGetRequest("http://auth.esaypnp.com/dd_auth/message/sendMessage", maps);
							logger.info("高管订阅结果：" + httpGetRequest);
							return new DataRes(1000,"success");
						}
						
					}
					
				}
				
			}
			
			
			//首先获取刘明峰的接口数据
		
			maps.put("userid", userid);
			maps.put("point_name", point_name);
			
			String httpPostRequest = HttpUtil.HttpPostRequest("http://auth.easypnp.com/dd_auth/process/create", maps);
			
			logger.info("审批接口：" + httpPostRequest);
			
			DataResponse dr = json.fromJson(httpPostRequest, DataResponse.class);
			
			if(dr.getCode() == 1000){
				
				
				su.setProcess_id(dr.getData().toString());
				su.setStatus(1);
				int create = subscrUserDao.create(su);
				
				if(create == 0 ) {
					return new DataRes(1001,"未知失败");
				}else {
					return new DataRes(1000,"success");
				}
				
			}else {
				return new DataRes(400,"创建审批失败");
			}
			
			
			
			
		}catch(Exception e){
			logger.error("创建失败",e);
			return new DataRes(500,"error");
		}finally{
			resource.close();
		}
		
		
	}
	
	
	
}
