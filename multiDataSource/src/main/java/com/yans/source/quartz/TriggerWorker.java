package com.yans.source.quartz;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.quartz.Job;
import org.quartz.JobDataMap;
import org.quartz.JobDetail;
import org.quartz.JobExecutionContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.alibaba.druid.util.StringUtils;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.yans.source.dao.SubscrUserDao;
import com.yans.source.dao.TriggerDao;
import com.yans.source.domain.PointTemplate;
import com.yans.source.domain.PushData;
import com.yans.source.domain.SubscrUser;
import com.yans.source.domain.Trigger;
import com.yans.source.redis.redisUtil;
import com.yans.source.util.CircleJsonUtil;
import com.yans.source.util.Function;
import com.yans.source.util.HttpUtil;
import com.yans.source.util.RunJsOnJava;
import com.yans.source.util.TxtWriterUtil;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

public class TriggerWorker implements Job  {
	
	public static final Logger logger = LoggerFactory.getLogger("business");
	
	private Trigger trigger;
	
	private PointTemplate pointTemplate;
	
	private TriggerDao triggerDao;
	
	private SubscrUserDao subscrUserDao;
	
	private String filepath;
	
	static Gson json = new Gson();
	
	public String getFilepath() {
		return filepath;
	}

	public void setFilepath(String filepath) {
		this.filepath = filepath;
	}

	public SubscrUserDao getSubscrUserDao() {
		return subscrUserDao;
	}

	public void setSubscrUserDao(SubscrUserDao subscrUserDao) {
		this.subscrUserDao = subscrUserDao;
	}

	public Trigger getTrigger() {
		return trigger;
	}

	public void setTrigger(Trigger trigger) {
		this.trigger = trigger;
	}

	public PointTemplate getPointTemplate() {
		return pointTemplate;
	}

	public void setPointTemplate(PointTemplate pointTemplate) {
		this.pointTemplate = pointTemplate;
	}

	public TriggerDao getTriggerDao() {
		return triggerDao;
	}

	public void setTriggerDao(TriggerDao triggerDao) {
		this.triggerDao = triggerDao;
	}

	@Override
	public void execute(JobExecutionContext job) {
		
		JobDetail jobDetail = job.getJobDetail();
		JobDataMap jobDataMap = jobDetail.getJobDataMap();
		Jedis resource = null;
		TriggerWorker tw = (TriggerWorker) jobDataMap.get("t");
		
		trigger = tw.getTrigger();
		pointTemplate = tw.getPointTemplate();
		subscrUserDao = tw.getSubscrUserDao();
		triggerDao = tw.getTriggerDao();
		filepath = tw.getFilepath();
		
		String pushdata = null;
		
		try{
			
			//取出检查点的数据
			JedisPool jedisPool = redisUtil.getJedisPool();
			resource = jedisPool.getResource();
			
			String key = resource.get(trigger.getCheck_id().toString());
			
			if(key == null ){
				return ;
			}
			
			String data = resource.get(key);
			
			if(data == null){
				return ;
			}
			
			List<SubscrUser> queryAll = subscrUserDao.queryAll(trigger.getId());
			
			Map<String,	 String> maps = new HashMap<String,	 String>();
			
			//循环订阅关系，对应的数据发给对应的人
			if(queryAll != null && queryAll.size() != 0){
			
				for(SubscrUser s : queryAll){
					
					maps.clear();
					
					if(s.getB_user_id() == null || StringUtils.isEmpty(s.getB_user_id())){
						continue;
					}
					
					//判断是否要重复推送，如果是需要重复推送，判断时间是否达到推送时间，若达到，进行处理
					if(trigger.getTg_max_redeliv() != 0 ){
						String[] newData = getNewData(resource, s, data,key);
						data = newData[0];
						pushdata = newData[1];
					}
					
					if(data == null ){
						return;
					}
					
					//取出所有的数据
					Object invokeJsFun = RunJsOnJava.invokeJsFun(trigger.getJs_script(), "oprateJson",s.getB_user_id(),data);//updete
					
					//无此人数据
					if(invokeJsFun == null || invokeJsFun.toString().length() < 3){
						continue;
					}
					
					//去掉今天已经发送过的数据
					String removeTodayData = removeTodayData(resource, s, invokeJsFun.toString());
					
					if(org.springframework.util.StringUtils.isEmpty(removeTodayData)){
						return;
					}
					
					//消息判断是否立即发送还是延迟发送
					if(trigger.getIs_right() == 0 ){
						
						maps.put("userid", s.getUser_id());
						
						if(trigger.getIs_file() == 0){
							
							List<String> circleJson = CircleJsonUtil.circleJson(invokeJsFun.toString());
							
							if(circleJson.size() == 0 ){
								return ;
							}
						
							//钉钉循环通知
							for(String  o : circleJson){
								Object invokeJsFun2 = RunJsOnJava.invokeJsFun(pointTemplate.getMessage(), "oprate_messaget",o);
								maps.put("msg",  invokeJsFun2.toString());
								String httpPostRequest = HttpUtil.HttpPostRequest("http://auth.easypnp.com/dd_auth/message/sendMessage", maps);
								logger.info(trigger.getId() + "发送消息结果：" +  httpPostRequest);
							}
							
						}else{
							
							Object invokeJsFun2 = RunJsOnJava.invokeJsFun(pointTemplate.getMessage(), "oprate_messaget",s);
							//文件发送,写入文件
							String writeTxt = TxtWriterUtil.writeTxt(filepath,removeTodayData);
							
							maps.put("msg",  invokeJsFun2.toString() +"\n" + writeTxt);
							String httpPostRequest = HttpUtil.HttpPostRequest("http://auth.easypnp.com/dd_auth/message/sendMessage", maps);
							logger.info(trigger.getId() + "发送消息结果：" +  httpPostRequest);
						}
						
					}else {
						
						//如果不是立即发送，需要进行对数据的去重
						//不是立即发送，则放入reids中'sys
						
						String smembers = resource.get("userid_" + trigger.getId() + "_" + s.getUser_id());
						
						if(org.springframework.util.StringUtils.isEmpty(smembers) ){
							resource.set("userid_" + trigger.getId() + "_" + s.getUser_id(),  data.toString());
							logger.info(trigger.getId() + "存放消息success");
							
						}else {
							
							List<String> olddata = CircleJsonUtil.circleJson(smembers);
							List<String> newdata = CircleJsonUtil.circleJson(data);
							
							newdata.removeAll(olddata);
							newdata.addAll(olddata);
							
							resource.set("userid_" + trigger.getId() + "_" + s.getUser_id(), json.toJson(newdata));
							logger.info(trigger.getId() + "存放消息success");
						}
					}
					
					//信息处理完事之后，删除所有的达标的重复推送数据，更新订阅关系表last_push_data
					resetData(resource,pushdata,s,data);
					
				}
			}
			
		}catch(Exception e){
			logger.error("触发器执行失败：",e);
		}finally{
			if(resource != null){
				resource.quit();
				resource.disconnect();
			}
		}
		
	}
	
	private void resetData(Jedis resource, String pushdata,SubscrUser su,String todayData) {
		
		List<PushData> arr =  null;
		
		try{
			arr = json.fromJson(pushdata, new TypeToken<List<PushData>>(){}.getType());
		}catch(Exception e){
			logger.error("重置数据失败：", e);
		}
		
		if(arr != null &&  arr.size() > 0){
			subscrUserDao.updatePushData(su.getId(), pushdata);
		}
		
	}

	/**
	 * 获取需要发送的数据，判断是否需要重复推送，如果是重复推送的话，修改推送次数
	 * 需要去掉本日推送数据，避免钉钉发送消息给限流限频率
	 * @param data 最新的redis数据
	 * @param jedis
	 * @param key checkid，也是最新数据的key
	 * @return
	 */
	public String[] getNewData(Jedis jedis,SubscrUser su,String data,String key) throws Exception{
		
		String last_push_data = su.getLast_push_data();
		List<PushData> push = new ArrayList<PushData>();
		
		PushData p1 = new PushData();
		p1.setLasttime(getNow());
		p1.setCount(1);
		p1.setKey(key);
		push.add(p1);
		
		//并无推送数据，返回最新取到的
		if(org.springframework.util.StringUtils.isEmpty(last_push_data)){
			return new String[]{data,json.toJson(push)};
		}
		
		List<PushData> datas = json.fromJson(last_push_data, new TypeToken<List<PushData>>(){}.getType());
		
		//并无要推送的数据
		if(datas == null || datas.size() == 0 ){
			return new String[]{data,json.toJson(push)};
		}
		
		String olddata = data;
		
		//对历次需要重复推送的数据去重
		for(PushData p : datas){
			
			String string = jedis.get(p.getKey());
			
			if(!org.springframework.util.StringUtils.isEmpty(string)){
				
				if(addtime(p.getLasttime(), trigger.getTg_redeliv_interval().toString()) >= 0){
					olddata = union(string, olddata);
					if(p.getCount() + 1 < trigger.getTg_max_redeliv()){
						p.setLasttime(getNow());
						p.setCount(p.getCount() + 1);
						push.add(p);
					}
				}else {
					push.add(p);
				}
			}
			
		}	
		
		return new String[]{olddata,json.toJson(push)};	
		
	}
	
	
	/**
	 *比较两个json数组差集
	 * @return
	 */
	public static String subtract(String newData,String oldData){
		
		List newarr = json.fromJson(newData, new TypeToken<List>(){}.getType());
		List oldarr = json.fromJson(oldData, new TypeToken<List>(){}.getType());
		
		if(newarr == null && oldarr == null){
			return null;
		}else if (newarr != null && oldarr == null){
			return json.toJson(newarr);
		}else if(newarr == null && oldarr != null){
			return json.toJson(oldarr);
		}
		
		newarr.removeAll(oldarr);
		
		if(newarr.size() == 0 ){
			return null;
		}
		
		return json.toJson(newarr);
		
	}
	
	/**
	 *比较两个json数组并集
	 * @return
	 */
	public static String union(String newData,String oldData){
		
		List newarr = json.fromJson(newData, new TypeToken<List>(){}.getType());
		List oldarr = json.fromJson(oldData, new TypeToken<List>(){}.getType());
		
		if(newarr == null && oldarr == null){
			return null;
		}else if (newarr != null && oldarr == null){
			return json.toJson(newarr);
		}else if(newarr == null && oldarr != null){
			return json.toJson(oldarr);
		}
		
		oldarr.removeAll(newarr);//差集
		
		oldarr.addAll(newarr);
		
		return json.toJson(oldarr);
		
	}
	
	/**
	 * 获取当前时间，格式精确到分钟 YYYY-MM-dd HH:mm
	 * @return
	 */
	public static String getNow(){
		SimpleDateFormat sdf = new SimpleDateFormat("YYYY-MM-dd HH:mm");
		return sdf.format(new Date());
	}
	
	public static void main(String[] args) throws ParseException {
		DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm");
		Date date = df.parse("2018-12-12 10:48");
		System.out.println(date.getTime());
	}
	
	/**
	 * 当前时间加上发送时间间隔后和上次发送时间比较
	 * @param timeStr 上次发送时间
	 * @param addnumber 时间间隔
	 * @return
	 * @throws ParseException
	 */
	public int addtime(String timeStr,String addnumber) throws ParseException {
		
		DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm");
		Date date = df.parse(timeStr);
		String now = df.format(new Date().getTime());
		//时间累计
		Calendar gc =new GregorianCalendar();
		gc.setTime(date);
		gc.add(GregorianCalendar.MINUTE,Integer.parseInt(addnumber));
		String str= df.format(gc.getTime());
		int compare_date = compare_date(now, str);
		return compare_date;
		
	}
	
	
	/**
	 * 比较两个时间大小，前面大，返回1，一样大 返回0，小 返回-1
	 * @param date1
	 * @param date2
	 * @return
	 * @throws ParseException
	 */
	public static int compare_date(String date1, String date2) throws ParseException {
	        
	    DateFormat df = new SimpleDateFormat("yyyy-MM-dd hh:mm");
	    
	    Date dt1 = df.parse(date1);
	    Date dt2 = df.parse(date2);
	    if (dt1.getTime() > dt2.getTime()) {
	        return 1;
	    } else if (dt1.getTime() < dt2.getTime()) {
	        return -1;
	    } else {
	        return 0;
	    }
	   
	}
	
	public String removeTodayData(Jedis jedis,SubscrUser su,String data){
		
		if(org.springframework.util.StringUtils.isEmpty(data)){
			return null;
		}
		
		String todayData = jedis.get(Function.todayString() + "_" + trigger.getId() + "_" + su.getUser_id());
		
		String subtract = subtract(data, todayData);
		
		jedis.setex(Function.todayString() + "_" +  trigger.getId() + "_" + su.getUser_id() ,86400,data);
		
		return subtract;
		
		
	}
	
	
	
	
}
