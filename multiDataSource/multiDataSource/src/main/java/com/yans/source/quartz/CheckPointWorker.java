package com.yans.source.quartz;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.ReentrantLock;

import javax.script.ScriptException;

import org.quartz.Job;
import org.quartz.JobDataMap;
import org.quartz.JobDetail;
import org.quartz.JobExecutionContext;
import org.quartz.SchedulerException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.yans.source.dao.CheckPointDao;
import com.yans.source.dao.PointTemplateDao;
import com.yans.source.dao.SubscrUserDao;
import com.yans.source.dao.TriggerDao;
import com.yans.source.domain.CheckPoint;
import com.yans.source.domain.DataSourceConfig;
import com.yans.source.domain.PointTemplate;
import com.yans.source.domain.Trigger;
import com.yans.source.redis.redisUtil;
import com.yans.source.util.Function;
import com.yans.source.util.RunJsOnJava;
import com.yans.source.worker.FileCall;
import com.yans.source.worker.InterfaceCall;
import com.yans.source.worker.JdbcCall;
import com.yans.source.worker.RedisCall;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

public class CheckPointWorker implements Job {

	private CheckPoint checkPoint;
	
	public static final Logger logger = LoggerFactory.getLogger("business");
	
	private CheckPointDao checkPointDao;
	
	private TriggerDao triggerDao;
	
	private QuartzOperate<CheckPointWorker> quartzOperate;
	
	private QuartzOperate<TriggerWorker> quartzTrigger;
	
	private PointTemplateDao pointTemplateDao;
	
	private SubscrUserDao subscrUserDao;
	
	private String filepath ;
	
	public QuartzOperate<TriggerWorker> getQuartzTrigger() {
		return quartzTrigger;
	}

	public void setQuartzTrigger(QuartzOperate<TriggerWorker> quartzTrigger) {
		this.quartzTrigger = quartzTrigger;
	}

	public PointTemplateDao getPointTemplateDao() {
		return pointTemplateDao;
	}

	public void setPointTemplateDao(PointTemplateDao pointTemplateDao) {
		this.pointTemplateDao = pointTemplateDao;
	}

	public SubscrUserDao getSubscrUserDao() {
		return subscrUserDao;
	}

	public void setSubscrUserDao(SubscrUserDao subscrUserDao) {
		this.subscrUserDao = subscrUserDao;
	}

	public String getFilepath() {
		return filepath;
	}

	public void setFilepath(String filepath) {
		this.filepath = filepath;
	}

	public QuartzOperate<CheckPointWorker> getQuartzOperate() {
		return quartzOperate;
	}

	public void setQuartzOperate(QuartzOperate<CheckPointWorker> quartzOperate) {
		this.quartzOperate = quartzOperate;
	}

	public TriggerDao getTriggerDao() {
		return triggerDao;
	}

	public void setTriggerDao(TriggerDao triggerDao) {
		this.triggerDao = triggerDao;
	}

	public CheckPoint getCheckPoint() {
		return checkPoint;
	}

	public void setCheckPoint(CheckPoint checkPoint) {
		this.checkPoint = checkPoint;
	}

	public CheckPointDao getCheckPointDao() {
		return checkPointDao;
	}

	public void setCheckPointDao(CheckPointDao checkPointDao) {
		this.checkPointDao = checkPointDao;
	}
	
	static Gson json = new Gson();
	
	@SuppressWarnings("all")
	static ThreadPoolExecutor tpe = new ThreadPoolExecutor(10, 20, 10, TimeUnit.MINUTES, new ArrayBlockingQueue(10));
	

	private ReentrantLock lock = new ReentrantLock();
	
	@Override
	public void execute(JobExecutionContext context) {
		
		JedisPool jedisPool = null;
		Jedis resource = null;
		
		try{
			
			jedisPool = redisUtil.getJedisPool();
			resource = jedisPool.getResource();
			
			JobDetail jobDetail = context.getJobDetail();
			
			JobDataMap map = jobDetail.getJobDataMap();
			CheckPointWorker cw = (CheckPointWorker) map.get("t");
			checkPoint = cw.getCheckPoint();
			checkPointDao = cw.getCheckPointDao();
			triggerDao = cw.getTriggerDao();
			filepath = cw.getFilepath();
			subscrUserDao = cw.getSubscrUserDao();
			pointTemplateDao = cw.getPointTemplateDao();
			quartzTrigger = cw.getQuartzTrigger();
			
			String cp_collector = checkPoint.getCp_collector();
			
			List<DataSourceConfig> datas = json.fromJson(cp_collector, new TypeToken<List<DataSourceConfig>>(){}.getType());
			
			List<Callable<String>> calls = operateCall(datas);
			List<Future<String>> futures = null;
			
			lock.lock();
			
			futures = tpe.invokeAll(calls);
			
			lock.unlock();
		
			int batchOprate = batchOprate(resource, futures);
			
			if(batchOprate < 0){
				return;
			}
			
			notifyTrigger(checkPoint.getId().toString());
			
		}catch(Exception e){
			logger.error("检查点错误：" ,e);
		}finally{
			
			if(lock.isLocked()){
				lock.unlock();
			}
			
			if(resource != null){
				resource.close();
			}
		}
	}

	
	/**
	 * 批处理 改成多线程执行的工作类对象集合，多线程处理，避免执行速度过慢
	 * @param datas
	 * @return
	 */
	public List<Callable<String>> operateCall(List<DataSourceConfig> datas){
		
		List<Callable<String>> data = new ArrayList<Callable<String>>();
		
		for(DataSourceConfig d : datas){
			
			switch (d.getType()) {
			case 1:
				JdbcCall jc = new JdbcCall();
				jc.setDsc(d);
				data.add(jc);
				break;
			case 2:
				RedisCall rc = new RedisCall();
				rc.setDsc(d);
				data.add(rc);
				break;
			case 3:
				InterfaceCall ic = new InterfaceCall();
				ic.setDsc(d);
				data.add(ic);
				break;
			case 4:
				FileCall fc = new FileCall();
				fc.setDsc(d);
				data.add(fc);
				break;

			default:
				break;
			}
			
		}
		
		return data;
		
	}
	
	/**
	 * 将多个批处理文件整理成json放入redis
	 * @param jedis
	 * @param futures
	 * @param cmd
	 * @return
	 * @throws InterruptedException
	 * @throws ExecutionException
	 * @throws NoSuchMethodException
	 * @throws ScriptException
	 */
	public int batchOprate(Jedis jedis,List<Future<String>> futures) throws NoSuchMethodException, ScriptException  {
		
		
		List<String> strs = new ArrayList<String>();
		
		for(Future<String> f : futures){
			try{
				strs.add(f.get());
			}catch(Exception e){
				logger.error("多线程获取结果失败：",e);
				return -1;
			}
		}
		
		Object invokeJsFun = RunJsOnJava.invokeJsFun(checkPoint.getCp_etl_batch(), "collector", json.toJson(strs));
		
		//放入最新数据
		String val = checkPoint.getId().toString() + "_" + Function.todayStringFromNow();
		jedis.set(checkPoint.getId().toString(), val );
		jedis.setex(val,checkPoint.getExpire_time(),invokeJsFun.toString());
		return 1;
		
	}
	
	public void notifyTrigger(String checkId){
		
		List<Trigger> queryData = triggerDao.queryData(checkId, 1, null, null);
		
		if(queryData != null && queryData.size() != 0){
			for(Trigger t : queryData){
				try {
					TriggerWorker tw = new TriggerWorker();
					tw.setTrigger(t);
					PointTemplate queryOne = pointTemplateDao.queryOne(t.getId());
					tw.setPointTemplate(queryOne);
					tw.setSubscrUserDao(subscrUserDao);
					tw.setTriggerDao(triggerDao);
					tw.setFilepath(filepath);
					
					quartzTrigger.executeTrigger(tw,QuartzExecute.trigger_prefix + t.getId() , QuartzExecute.trigger_prefix + t.getId());
					
					
				} catch (SchedulerException e) {
					logger.error("立即执行trigger报错：",e);
				}
				
				
			}
			
		}
		
		
	}
	
	
	
	
	
	
	
	
	
	
	
	
	
}
