package com.yans.source.quartz;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.quartz.CronScheduleBuilder;
import org.quartz.CronTrigger;
import org.quartz.Job;
import org.quartz.JobBuilder;
import org.quartz.JobDetail;
import org.quartz.JobKey;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.quartz.Trigger;
import org.quartz.TriggerBuilder;
import org.quartz.TriggerKey;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class QuartzOperate<T extends Job> {

	
	@Autowired
	private Scheduler scheduler;
	
	/**
     * 添加一个定时任务
     *
     * @param jobName           任务名
     * @param jobGroupName      任务组名
     * @param triggerName       触发器名
     * @param triggerGroupName  触发器组名
     * @param jobClass          任务
     * @param cron              时间设置，参考quartz说明文档
	 * @throws SchedulerException 
     */
    @SuppressWarnings(value="all")
    public void addJob(T t,String jobName,String triggerName,String cron) throws SchedulerException {
        
            // 任务名，任务组，任务执行类
            JobDetail job = JobBuilder.newJob(t.getClass()).withIdentity(jobName, null).build();
            Map<String, Object> param = new HashMap<>();
            param.put("t", t);
            // 任务参数
            job.getJobDataMap().putAll(param);
            
          
            // 触发器
            TriggerBuilder<Trigger> triggerBuilder = TriggerBuilder.newTrigger();
            // 触发器名,触发器组
            triggerBuilder.withIdentity(triggerName, null);
            triggerBuilder.startNow();
            // 触发器时间设定
            triggerBuilder.withSchedule(CronScheduleBuilder.cronSchedule(cron));
            // 创建Trigger对象
            CronTrigger trigger = (CronTrigger) triggerBuilder.build();
            
            // 调度容器设置JobDetail和Trigger
            scheduler.scheduleJob(job, trigger);

            // 启动
            if (!scheduler.isShutdown()) {
                scheduler.start();
            }
        
    }

    /**
     * 修改一个任务的触发时间
     *
     * @param triggerName       触发器名
     * @param triggerGroupName  触发器组名
     * @param cron              时间设置，参考quartz说明文档
     * @throws SchedulerException 
     */
    public void modifyJobTime(String triggerName,String cron) throws SchedulerException {
        TriggerKey triggerKey = TriggerKey.triggerKey(triggerName, null);
        CronTrigger trigger = (CronTrigger) scheduler.getTrigger(triggerKey);
        if (trigger == null) {
            return;
        }

        String oldTime = trigger.getCronExpression();
        if (!oldTime.equalsIgnoreCase(cron)) {
            // 触发器
            TriggerBuilder<Trigger> triggerBuilder = TriggerBuilder.newTrigger();
            // 触发器名,触发器组
            triggerBuilder.withIdentity(triggerName, null);
            triggerBuilder.startNow();
            // 触发器时间设定
            triggerBuilder.withSchedule(CronScheduleBuilder.cronSchedule(cron));
            // 创建Trigger对象
            trigger = (CronTrigger) triggerBuilder.build();
            // 方式一 ：修改一个任务的触发时间
            scheduler.rescheduleJob(triggerKey, trigger);
            
        }
    }

    /**
     * 移除一个任务
     *
     * @param jobName           任务名
     * @param jobGroupName      任务组名
     * @param triggerName       触发器名
     * @param triggerGroupName  触发器组名
     * @throws SchedulerException 
     */
    public void removeJob(String triggerName,String jobName) throws SchedulerException {

        TriggerKey triggerKey = TriggerKey.triggerKey(triggerName, null);

        // 移除触发器
        scheduler.unscheduleJob(triggerKey);
        
        // 停止触发器
        scheduler.pauseTrigger(triggerKey);
        
        // 删除任务
        scheduler.deleteJob(JobKey.jobKey(jobName, null));
       
    }

    /**
     * 获取任务是否存在
     *
     * STATE_BLOCKED 4 阻塞
     * STATE_COMPLETE 2 完成
     * STATE_ERROR 3 错误
     * STATE_NONE -1 不存在
     * STATE_NORMAL 0 正常
     * STATE_PAUSED 1 暂停
     * @throws SchedulerException 
     *
     */
    public  Boolean notExists(String triggerName) throws SchedulerException {
        
       return scheduler.getTriggerState(TriggerKey.triggerKey(triggerName, null)) == Trigger.TriggerState.NONE;
        
    }

	
	/**
	 * 立即创建一个trigger并且马上执行
	 * @param t
	 * @param jobName
	 * @param triggerName
	 * @param cron
	 * @throws SchedulerException
	 */
    public void executeTrigger(T t,String jobName,String triggerName) throws SchedulerException {
        
    	removeJob(triggerName, jobName);
    	
        // 任务名，任务组，任务执行类
        JobDetail job = JobBuilder.newJob(t.getClass()).withIdentity(jobName, null).build();
        Map<String, Object> param = new HashMap<>();
        param.put("t", t);
        // 任务参数
        job.getJobDataMap().putAll(param);
        
        // 触发器
        TriggerBuilder<Trigger> triggerBuilder = TriggerBuilder.newTrigger();
        // 触发器名,触发器组
        triggerBuilder.withIdentity(triggerName, null);
        triggerBuilder.startNow();
       
        // 触发器时间设定
        triggerBuilder.startAt(new Date());
        
        // 创建Trigger对象//跟上面addjob的不同在于上面是立即crontrigger 表达式trigger
        Trigger build = triggerBuilder.build();
        
        //创建一个临时的trigger并且执行
        scheduler.scheduleJob(job, build);

        // 启动
        if (!scheduler.isShutdown()) {
            scheduler.start();
        }
    
}
	
	
	
	
	
	
}
