package com.yans.source.schedule;

import java.util.List;

import org.quartz.SchedulerException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.yans.source.base.BaseSchedule;
import com.yans.source.dao.CheckPointDao;
import com.yans.source.domain.CheckPoint;
import com.yans.source.dto.CheckPointDto;
import com.yans.source.quartz.QuartzExecuteForPoint;
import com.yans.source.quartz.QuartzOperate;

@Component
public class CheckPointSchedule extends BaseSchedule {
	
	@Autowired
	private CheckPointDao checkPointDao;
	
//	@Autowired
//	private CheckPointInit checkPointInit;
	
	@Autowired
	private QuartzExecuteForPoint quartzExecuteForPoint;
	
	@Autowired QuartzOperate quartzOperate;
	
	
	@Scheduled(cron="0/5 * * * * * ")
	public void checkPoint(){
		
		try{
			
			List<CheckPoint> arr = checkPointDao.queryAll();
			
			//查询到的数据为空的话，取消线程池中的所有task
			if(arr == null || arr.size() == 0 ){
				deleteAll();
				QuartzExecuteForPoint.arr = arr;
				return ;
			}
			
			//数据不同的话，重启启动不同task
			if(arr != null && QuartzExecuteForPoint.arr == null || arr.size() != QuartzExecuteForPoint.arr.size()){
				
				deleteAll();
				
				quartzExecuteForPoint.execute();
				QuartzExecuteForPoint.arr = arr;
			}
				
		}catch(Exception e){
			logger.error("定时检查检查点错误：",e);
		}
			
	}
	
	
	/**
	 * 删除所有的定时任务
	 * @throws SchedulerException
	 */
	public void deleteAll() throws SchedulerException{
		
		if(QuartzExecuteForPoint.arr == null || QuartzExecuteForPoint.arr.size() == 0 ){
			return ;
		}
		
		for(CheckPoint  cp :QuartzExecuteForPoint.arr){
			quartzOperate.removeJob(QuartzExecuteForPoint.checkPoint_prefix + cp.getId().toString(), QuartzExecuteForPoint.checkPoint_prefix + cp.getId().toString());
		}
		
		
	}
	
	
}
