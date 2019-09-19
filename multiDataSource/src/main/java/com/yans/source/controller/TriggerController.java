package com.yans.source.controller;

import java.io.File;
import java.util.List;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.alibaba.druid.util.StringUtils;
import com.yans.source.base.BaseController;
import com.yans.source.base.DataRes;
import com.yans.source.dao.TriggerDao;
import com.yans.source.domain.Trigger;
import com.yans.source.redis.redisUtil;
import com.yans.source.service.TriggerService;
import com.yans.source.util.Base64Test;
import com.yans.source.util.Function;
import com.yans.source.util.TxtWriterUtil;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

@RestController
@RequestMapping("trigger")
public class TriggerController extends BaseController {

	@Autowired
	private TriggerDao triggerDao;
	
	@Autowired
	private TriggerService triggerService;
	
	@Autowired
	private RedisTemplate<String,String> redisTemplate;

	
	@Value("${filepath}")
	private String filepath;
	
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
		
		Long start = System.nanoTime();
		
		if(StringUtils.isEmpty(userid) ){
			return new DataRes(400,"参数缺失");
		}
		
		Jedis jedis = null;
		
		try {
//			
//			JedisPool jedisPool = redisUtil.getJedisPool();
//			
//			jedis = jedisPool.getResource();
//			logger.info("获取链接的时间：" + (System.nanoTime() - start ) / 1000000 );
//			Set<String> zrevrange = jedis.zrevrange("trigger_hot", 1, 4);
//			
//			logger.info("获取最热的时间：" + (System.nanoTime() - start ) / 1000000 );
//			
//			if( zrevrange == null ||  zrevrange.size() == 0 ){
//				return new DataRes(1001,"无数据");
//			}
			
			Set<String> zrevrange = redisTemplate.opsForZSet().reverseRange("trigger_hot", 1, 4);
			
			System.out.println(zrevrange.size());
			
			
			List<Trigger> queryData = triggerDao.hotData(zrevrange, userid);
			
			logger.info("执行接口时间：" + (System.nanoTime() - start ) / 1000000 );
			
			if(queryData != null){
				return new DataRes(1000,"success",queryData);
			}else {
				return new DataRes(1001,"无数据");
			}
			
		} catch (Exception e) {
			logger.error("查询触发器失败：",e);
			return new DataRes(500,"系统错误");
		}finally{
			if(jedis != null){
				jedis.quit();
				jedis.disconnect();
			}
		}
		
	}
	
	
	@RequestMapping("infoFront")
	public Object info(String id,String userid){
		
		if(StringUtils.isEmpty(id) || StringUtils.isEmpty(userid)){
			return new DataRes(400,"参数缺失");
		}
		
		try {
			
			Trigger queryData = triggerDao.queryOne(Long.valueOf(id),userid);
			
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
				|| t.getImg_url() == null
				){
			return new DataRes(400,"参数错误");
		}
		
		try {
			
			String uploadPhoto = uploadPhoto(t.getImg_url());
			System.err.println(uploadPhoto);
			t.setImg_url(uploadPhoto);
			
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
		
		Jedis jedis = null;
		try {
			JedisPool jedisPool = redisUtil.getJedisPool();
			jedis = jedisPool.getResource();
			int queryData = triggerDao.delete(Long.valueOf(parameter));
			if(queryData > 0){
				jedis.zrem("trigger_hot", parameter);
				return new DataRes(1000,"success",queryData);
			}else {
				return new DataRes(1001,"fail");
			}
		} catch (Exception e) {
			logger.error("delete触发器失败：",e);
			return new DataRes(500,"系统错误");
		}finally{
			if(jedis != null){
				jedis.quit();
				jedis.disconnect();
			}
		}
		
	}
	
	
	@RequestMapping("update")
	public Object update(Trigger t){
		
		if(t == null || t.getId() == null
				){
			return new DataRes(400,"参数错误");
		}
		
		try {
			
			if(!org.springframework.util.StringUtils.isEmpty(t.getImg_url())){
				String uploadPhoto = uploadPhoto(t.getImg_url());
				t.setImg_url(uploadPhoto);
			}
			
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
	
	public String uploadPhoto(String base64) throws Exception{
		
		//要保存的地址
		String realPath =  filepath + Function.todayString() + "/";
		
		File f = new File(realPath);
		if(!f.exists()){
			f.mkdirs();
		}
		
		if(base64 != null ){
				
			Long now = System.currentTimeMillis();
			 
			//获取后缀
			String suffic = base64.substring(11, base64.indexOf(";"));
			
			String url = realPath +"/"+ now + "." + suffic;
				
			Base64Test.GenerateImage2(base64.substring(base64.indexOf(",")+1), url);
			
			String domain = TxtWriterUtil.getDomain(realPath,  now + "." + suffic);
				
			return domain;
		}
		
		return null;
	}
	
	public static void main(String[] args) throws InterruptedException {
		
		
		Long start = System.nanoTime();
		
		
		Thread.sleep(1000);
		
		System.out.println((System.nanoTime() - start ) / 1000000);
		
		
	}
	
	
}
