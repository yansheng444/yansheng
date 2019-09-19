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

import com.yans.source.dao.CheckPointDao;
import com.yans.source.dao.PointTemplateDao;
import com.yans.source.dao.SubscrUserDao;
import com.yans.source.dao.TriggerDao;
import com.yans.source.domain.CheckPoint;

@Component
public class QuartzExecuteForPoint {
	
	public static final String checkPoint_prefix = "checkPoint";
	
	public static final Logger logger = LoggerFactory.getLogger("business");
	
	@Autowired
	private PointTemplateDao pointTemplateDao;
	
	@Autowired
	private SubscrUserDao subscrUserDao;
	
	@Value("${filepath}")
	private String filepath ;
	
	@Autowired
	private CheckPointDao checkPointDao;

	@Autowired
	private QuartzOperate<CheckPointWorker> quartzOperate;
	
	@Autowired
	private QuartzOperate<TriggerWorker> quartzTrigger;
	
	@Autowired
	private TriggerDao triggerDao;
	
	//定义一个全局变量主要是为了比较新查询出来的数据是否完全一致
	public static List<CheckPoint> arr = new ArrayList<CheckPoint>();
	
    @PostConstruct
    public void execute() {
    	
    	List<CheckPoint> data = checkPointDao.queryAll();
    	arr = data;
    	if(data == null || data.size() == 0){
    		return ;
    	}
		
		for(CheckPoint cd : data){
			
			CheckPointWorker cp = new CheckPointWorker();
			cp.setCheckPoint(cd);
			cp.setCheckPointDao(checkPointDao);
			cp.setTriggerDao(triggerDao);
			cp.setQuartzOperate(quartzOperate);
			cp.setPointTemplateDao(pointTemplateDao);
			cp.setFilepath(filepath);
			cp.setSubscrUserDao(subscrUserDao);
			cp.setQuartzTrigger(quartzTrigger);
			
			try {
				
				quartzOperate.addJob(cp, checkPoint_prefix + cd.getId().toString(), checkPoint_prefix + cd.getId().toString(), cd.getCondition());
				
			} catch (SchedulerException e) {
				logger.error("增加job错误：",e);
			}
			
		}
    	
    	
    	
    }

    
    
    
    
    
}
	
