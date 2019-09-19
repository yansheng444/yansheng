package com.yans.source.schedule;

import java.util.List;

import org.quartz.SchedulerException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.yans.source.base.BaseSchedule;
import com.yans.source.dao.TriggerDao;
import com.yans.source.domain.Trigger;
import com.yans.source.quartz.QuartzExecute;
import com.yans.source.quartz.QuartzOperate;

@Component
public class TriggerSchedule extends BaseSchedule {

	@Autowired
	private TriggerDao triggerDao;

//	@Autowired
//	private TriggerInit triggerInit;
//
//	@Autowired
//	private TriggerScheduleExcute triggerScheduleExcute;
	
	@Autowired
	private QuartzExecute quartzExecute;
	
	@Autowired
	private QuartzOperate quartzOperate;


	@Scheduled(cron = "*/5 * * * * *")
	public void trigger() {

		try {

			List<Trigger> queryAll = triggerDao.queryAll();
			// 无数据，删除所有的定时任务
			if (queryAll == null || queryAll.size() == 0) {
				deleteAll();
				QuartzExecute.arr = null;
				return;
			}

			if (queryAll != null && QuartzExecute.arr == null || queryAll.size() != QuartzExecute.arr.size()) {
				
				deleteAll();
				
				//重启
				QuartzExecute.arr = queryAll;
				quartzExecute.execute();
			}

		} catch (Exception e) {
			logger.error("定时检查检查点错误：", e);
			e.printStackTrace();
		}

	}
	
	
	/**
	 * 删除所有的定时任务
	 * @throws SchedulerException
	 */
	public void deleteAll() throws SchedulerException{
		
		if(QuartzExecute.arr == null || QuartzExecute.arr.size() == 0 ){
			return ;
		}
		
		for(Trigger t : QuartzExecute.arr){
			quartzOperate.removeJob(QuartzExecute.trigger_prefix + t.getId().toString(), QuartzExecute.trigger_prefix + t.getId().toString());;
		}
		
		
	}
	
	

}
