package com.yans.source.schedule;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.yans.source.base.BaseSchedule;
import com.yans.source.dao.PointTemplateDao;
import com.yans.source.dao.TriggerDao;
import com.yans.source.domain.PointTemplate;
import com.yans.source.domain.Trigger;
import com.yans.source.redis.redisUtil;
import com.yans.source.util.CircleJsonUtil;
import com.yans.source.util.HttpUtil;
import com.yans.source.util.RunJsOnJava;
import com.yans.source.util.TxtWriterUtil;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.Pipeline;
import redis.clients.jedis.Response;

@Component
public class SendMessageSchedule extends BaseSchedule {
	
	@Value("${filepath}")
	private String filepath;
	
	@Autowired
	private PointTemplateDao pointTemplateDao;
	
	@Autowired
	private TriggerDao triggerDao;
	
	@Scheduled(cron="*/10 * * * * *")
	public void send(){
		
		Jedis resource = null;
		
		try{
			
			JedisPool jedisPool = redisUtil.getJedisPool();
			
			resource = jedisPool.getResource();
			
			//取出所以待发送消息的数据
			Set<String> keys = resource.keys("userid_*");
			
			if(keys != null){
				
				//pipline redis 读写操作，实现IO性能提升
				Pipeline pipelined = resource.pipelined();
				
				Map<String, Response<String>> m = new HashMap<String, Response<String>>();
				
				for(String s : keys ){
				
					Response<String> response = pipelined.get(s);
					pipelined.sync();
					m.put(s, response);
					pipelined.del(s);
					pipelined.sync();
				}
				
				Map<String,	 String> maps = new HashMap<String,	 String>();
				
				StringBuffer sb = new StringBuffer();
				
				for(String s : keys ){
					
					String triggerId = s.substring(s.indexOf("_") + 1,s.lastIndexOf("_"));
					
					PointTemplate queryOne = pointTemplateDao.queryOne(Long.valueOf(triggerId));
					
					Trigger trigger = triggerDao.queryOne(Long.valueOf(triggerId), null);
					
					if(trigger == null){
						continue;
					}
					
					//直接操作stringbuffer，避免循环下多次new新对象
					sb.substring(sb.length());
					
					Response<String> next = m.get(s);
					
					String set = next.get();
					
					//文本发送，无所谓,全部写入文本
					if(trigger.getIs_file() == 1){
						String writeList = TxtWriterUtil.writeTxt(filepath, set);
						Object invokeJsFun2 = RunJsOnJava.invokeJsFun(queryOne.getMessage(), "oprate_messaget");
						maps.put("msg",invokeJsFun2.toString() + "\n" + writeList);
						maps.put("userid", s.substring(s.lastIndexOf("_")+1));
						String httpPostRequest = HttpUtil.HttpPostRequest("http://auth.easypnp.com/dd_auth/message/sendMessage", maps);
						logger.info("发送给用户：" + s + "消息结果：" + httpPostRequest);
					
					} else {
						
						
						List<String> circleJson = CircleJsonUtil.circleJson(set);
						
						if(circleJson.size() == 0 ){
							return ;
						}
						
						//钉钉循环通知
						for(String  o : circleJson){
							Object invokeJsFun2 = RunJsOnJava.invokeJsFun(queryOne.getMessage(), "oprate_messaget",o);
							String httpPostRequest = HttpUtil.HttpPostRequest("http://auth.easypnp.com/dd_auth/message/send", maps);
							maps.put("msg",  invokeJsFun2.toString());
							logger.info(trigger.getId() + "发送消息结果：" +  httpPostRequest);
						}
					}
						
				}
				
			}
			
		}catch(Exception e){
			logger.error("发送消息失败",e);
		}finally{
			resource.close();
		}
		
		
		
		
	
	}
	
	
}
