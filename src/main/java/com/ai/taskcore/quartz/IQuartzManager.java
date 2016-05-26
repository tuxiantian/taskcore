package com.ai.taskcore.quartz;

import java.util.HashMap;
import java.util.List;

import org.quartz.CronTrigger;
import org.quartz.JobDetail;
import org.quartz.Trigger;


public interface IQuartzManager {

	/**
	 * 修改trigger,修改qrtz_triggers表数据
	 * 
	 * @param triggerName
	 * @param triggerGroup
	 * @param trigger
	 * @return
	 */
	public boolean updateTrigger(String triggerName, String triggerGroup, Trigger trigger);

	/**
	 * 修改Cron Trigger，涉及表qrtz_cron_triggers，更改执行计划等
	 * 
	 * @param triggerName
	 * @param triggerGroup
	 * @param cronTrigger
	 * @return
	 */
	public boolean updateCronTrigger(String triggerName, String triggerGroup, CronTrigger cronTrigger);

	/**
	 * 修改jobDetail
	 * 
	 * @param jobDetail
	 * @return
	 */
	public boolean updateJobDetail(JobDetail jobDetail);

	/**
	 * 删除Job,直接从数据库中删除，关联表数据也删除
	 * 
	 * @param triggerName
	 * @param triggerGroup
	 * @param jobName
	 * @param jobGroup
	 */
	public boolean deleteJob(String triggerName, String triggerGroup, String jobName, String jobGroup);

	/**
	 * 暂停某个触发器
	 * 
	 * @param triggerName
	 * @param triggerGroup
	 * @return
	 */
	public boolean pauseTrigger(String triggerName, String triggerGroup);

	/**
	 * 恢复已暂停的触发器
	 * 
	 * @param triggerName
	 * @param triggerGroup
	 * @return
	 */
	public boolean resumeTrigger(String triggerName, String triggerGroup);
	/**
	 * 立即执行JOB
	 * 
	 * @param jobName
	 * @param jobGroup
	 * @return
	 */
	public boolean runAJobNow(String jobName, String jobGroup);

	/**
	 * 暂停所有触发器
	 * 
	 * @return
	 */
	public boolean pauseAllTrigger();

	/**
	 * 启动所有触发器
	 * 
	 * @return
	 */
	public boolean startAllTrigger();

	/**
	 * 停止所有触发器,停止后无法启动，无法调用start方法
	 * 
	 * @return
	 */
	public boolean shutdownTrigger();
	/**
	 * 返回所有jobDetail
	 * @return
	 */
	public List<HashMap<String, String>> getAllJobDetail();
}
