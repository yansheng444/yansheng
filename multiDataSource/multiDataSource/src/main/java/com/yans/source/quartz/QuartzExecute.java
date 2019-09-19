package com.yans.source.quartz;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;

import org.quartz.SchedulerException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.yans.source.dao.PointTemplateDao;
import com.yans.source.dao.SubscrUserDao;
import com.yans.source.dao.TriggerDao;
import com.yans.source.domain.PointTemplate;

@Component
public class QuartzExecute {
	
	public static final Logger logger = LoggerFactory.getLogger("business");
	
	public static final String group_job_prefix = "group_job";
	
	public static final String group_trigger_prefix = "group_trigger";
	
	public static final String trigger_prefix = "trigger";
	
	@Autowired
	private TriggerDao triggerDao;
	
	@Autowired
	private PointTemplateDao pointTemplateDao;
	
	@Autowired
	private SubscrUserDao subscrUserDao;
	
	@Value("${filepath}")
	private String filepath ;
	
	@Autowired
	private QuartzOperate<TriggerWorker> quartzOperate;
	
	//定义一个全局变量主要是为了比较新查询出来的数据是否完全一致
	public static List<com.yans.source.domain.Trigger> arr = new ArrayList<com.yans.source.domain.Trigger>();
	
    @PostConstruct
    public void execute() {
    	List<com.yans.source.domain.Trigger> queryAll = triggerDao.queryAll();
    	System.out.println(queryAll.size());
    	arr = queryAll;
    	
    	if(queryAll == null || queryAll.size() == 0){
    		return ;
    	}
    	
    	for(com.yans.source.domain.Trigger t : queryAll){
			TriggerWorker tw = new TriggerWorker();
			tw.setTrigger(t);
			PointTemplate queryOne = pointTemplateDao.queryOne(t.getId());
			tw.setPointTemplate(queryOne);
			tw.setSubscrUserDao(subscrUserDao);
			tw.setTriggerDao(triggerDao);
			tw.setFilepath(filepath);
			
			try {
				quartzOperate.addJob(tw, trigger_prefix + t.getId().toString(), trigger_prefix + t.getId().toString(), t.getCondition_detail());
			} catch (SchedulerException e) {
				logger.error(" trigger addjob error:",e);
			}
    				
    	}
    	
    	
    }
    
    
    
    
}
	
