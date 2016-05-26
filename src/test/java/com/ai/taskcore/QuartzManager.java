package com.ai.taskcore;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.quartz.CronTrigger;
import org.quartz.JobDetail;
import org.quartz.Scheduler;
import org.quartz.SchedulerFactory;
import org.quartz.Trigger;
import org.quartz.impl.StdScheduler;
import org.quartz.impl.StdSchedulerFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

public class QuartzManager {
	// 更改执行计划
	public boolean updateTrigger() {
		boolean flag = false;
		ApplicationContext springContext = new ClassPathXmlApplicationContext(new String[] { "classpath:spring/spring-all.xml"});
		try{
			StdScheduler  scheduler=(StdScheduler)springContext.getBean("schedulerFactoryBean");
			//更新qrtz_trigger表
//			Trigger trigger=scheduler.getTrigger("testTrigger", "ds_trigger");
//			trigger.setDescription("xxx");
//			scheduler.rescheduleJob("testTrigger", "ds_trigger", trigger);
//			scheduler.triggerJob("myJobDetail1", "ds_jobs");
			//更改执行计划,qrtz_cron_triggers
//			CronTrigger cronTrigger=(CronTrigger)scheduler.getTrigger("testTrigger", "ds_trigger");
//			cronTrigger.setCronExpression("0/2 * * * * ?");
//			scheduler.rescheduleJob("testTrigger", "ds_trigger", cronTrigger);
			
			//更改jobDetail,qrtz_job_details
//			JobDetail jd=scheduler.getJobDetail("mayJobDetail", "ds_jobs");
//			jd.setDescription("测试任务！");
//			scheduler.addJob(jd, true);
			
			//删除job,直接从数据库中删除，关联表也删除
//			scheduler.pauseTrigger("testTrigger", "ds_trigger");// 停止触发器  
//			scheduler.unscheduleJob("testTrigger", "ds_trigger");// 移除触发器  
//			scheduler.deleteJob("mayJobDetail", "ds_jobs");// 删除任务 
			
			//停止job，通过停止触发器来操作
			 /*
	         * 更改任务调度的周期时间：
	         * 1，不要调用StdScheduler的shutdown()方法，shutdown()以后无法再start()
	         * 2，可使用standby()暂停调度任务，再start()
	         * 3，设置cron后，要调用rescheduleJob(TriggerKey triggerKey, Trigger newTrigger)
	         */
			//判断调度器当前是否在暂停状态
//            if (scheduler.isInStandbyMode()) {
//                scheduler.start();
//            }
			//停止所有触发器
			//scheduler.standby();
			
			//重启job
//			scheduler.resumeTrigger("testTrigger", "ds_trigger"); 
//			System.out.println("触发器重启成功！");
			
			//返回所有jobDeail
//			String[] jobGroups=scheduler.getJobGroupNames();
//			for(String jobGroup:jobGroups){
//				System.out.println("jobGroup="+jobGroup);
//				String[] jobNames=scheduler.getJobNames(jobGroup);
//				for(String jobName:jobNames){
//					JobDetail jobD=scheduler.getJobDetail(jobName, jobGroup);
//					System.out.println("jobName="+jobD.getName());
//					System.out.println("description="+jobD.getDescription());
//					Trigger[] trigger=scheduler.getTriggersOfJob(jobName, jobGroup);
//					System.out.println("cronExpression="+((CronTrigger)trigger[0]).getCronExpression());
					//0：活动 1：停止
//					DELETED -1
//					COMPLETE 2
//					PAUSED 1
//					PAUSED_BLOCKED 1
//					ERROR 3
//					BLOCKED 4
//					否则 返回0
//					None：Trigger已经完成，且不会在执行，或者找不到该触发器，或者Trigger已经被删除
//					NORMAL:正常状态
//					PAUSED：暂停状态
//					COMPLETE：触发器完成，但是任务可能还正在执行中
//					BLOCKED：线程阻塞状态
//					ERROR：出现错误
//					System.out.println("triggerState="+scheduler.getTriggerState(trigger[0].getName(), trigger[0].getGroup()));
//					//开始时间
//					System.out.println(trigger[0].getStartTime());
//					//停止时间
//					System.out.println(trigger[0].getPreviousFireTime());
//					ArrayList<List<String>> listJobData = new ArrayList<List<String>>();
//					for (Object e : jobD.getJobDataMap().entrySet()) {
//						Map.Entry<?, ?> entry = (Entry<?, ?>) e;
//						ArrayList<String> list = new ArrayList<String>();
//						list.add("'" + entry.getKey() + "'");
//						list.add("'" + entry.getValue() + "'");
//						listJobData.add(list);
//					}
//					//[['targetObject', 'com.ai.taskcore.jobs.MyJobTest@536cfd7c'], ['targetMethod', 'executeInternal']]
//					System.out.println(listJobData.toString());
//				}
//			}
			
//			Thread.sleep(20000);
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return flag;
	}
	
	public static void main(String[] args) {
		QuartzManager qm=new QuartzManager();
		qm.updateTrigger();
	}
}
