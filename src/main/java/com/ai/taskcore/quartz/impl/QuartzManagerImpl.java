package com.ai.taskcore.quartz.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.quartz.CronTrigger;
import org.quartz.JobDetail;
import org.quartz.Trigger;
import org.quartz.impl.StdScheduler;

import com.ai.taskcore.quartz.IQuartzManager;
import com.ai.taskcore.util.DateUtil;

public class QuartzManagerImpl implements IQuartzManager {
	// Quartz调度器对象
	private StdScheduler scheduler;

	public void setScheduler(StdScheduler scheduler) {
		this.scheduler = scheduler;
	}

	/**
	 * 修改trigger,修改qrtz_triggers表数据
	 * 
	 * @param triggerName
	 * @param triggerGroup
	 * @param trigger
	 * @return
	 */
	public boolean updateTrigger(String triggerName, String triggerGroup, Trigger trigger) {
		boolean flag = false;
		try {
			scheduler.rescheduleJob(triggerName, triggerGroup, trigger);
			flag = true;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return flag;
	}

	/**
	 * 修改Cron Trigger，涉及表qrtz_cron_triggers，更改执行计划等
	 * 
	 * @param triggerName
	 * @param triggerGroup
	 * @param cronTrigger
	 * @return
	 */
	public boolean updateCronTrigger(String triggerName, String triggerGroup, CronTrigger cronTrigger) {
		boolean flag = false;
		try {
			scheduler.rescheduleJob(triggerName, triggerGroup, cronTrigger);
			flag = true;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return flag;
	}

	/**
	 * 修改jobDetail
	 * 
	 * @param jobDetail
	 * @return
	 */
	public boolean updateJobDetail(JobDetail jobDetail) {
		boolean flag = false;
		try {
			scheduler.addJob(jobDetail, true);
			flag = true;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return flag;
	}

	/**
	 * 删除Job,直接从数据库中删除，关联表数据也删除
	 * 
	 * @param triggerName
	 * @param triggerGroup
	 * @param jobName
	 * @param jobGroup
	 */
	public boolean deleteJob(String triggerName, String triggerGroup, String jobName, String jobGroup) {
		boolean flag = false;
		try {
			scheduler.pauseTrigger(triggerName, triggerGroup);// 停止触发器
			scheduler.unscheduleJob(triggerName, triggerGroup);// 移除触发器
			scheduler.deleteJob(jobName, jobGroup);// 删除任务
			flag = true;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return flag;
	}

	/**
	 * 暂停某个触发器
	 * 
	 * @param triggerName
	 * @param triggerGroup
	 * @return
	 */
	public boolean pauseTrigger(String triggerName, String triggerGroup) {
		boolean flag = false;
		try {
			// 暂停触发器
			scheduler.pauseTrigger(triggerName, triggerGroup);
			flag = true;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return flag;
	}

	/**
	 * 恢复已暂停的触发器
	 * 
	 * @param triggerName
	 * @param triggerGroup
	 * @return
	 */
	public boolean resumeTrigger(String triggerName, String triggerGroup) {
		boolean flag = false;
		try {
			// 暂停触发器
			scheduler.resumeTrigger(triggerName, triggerGroup);
			flag = true;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return flag;
	}
	/**
	 * 立即执行JOB
	 * 
	 * @param jobName
	 * @param jobGroup
	 * @return
	 */
	public boolean runAJobNow(String jobName, String jobGroup) {
		boolean flag = false;
		try {
			// 立即执行JOB
			scheduler.triggerJob(jobName, jobGroup);
			flag = true;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return flag;
	}

	/**
	 * 暂停所有触发器
	 * 
	 * @return
	 */
	public boolean pauseAllTrigger() {
		boolean flag = false;
		try {
			scheduler.standby();
			flag = true;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return flag;
	}

	/**
	 * 启动所有触发器
	 * 
	 * @return
	 */
	public boolean startAllTrigger() {
		boolean flag = false;
		try {
			if (scheduler.isInStandbyMode()) {
				scheduler.start();
			}
			flag = true;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return flag;
	}

	/**
	 * 停止所有触发器,停止后无法启动，无法调用start方法
	 * 
	 * @return
	 */
	public boolean shutdownTrigger() {
		boolean flag = false;
		try {
			scheduler.shutdown();
			flag = true;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return flag;
	}

	/**
	 * 返回所有jobDetail
	 * 
	 * @return
	 */
	public List<HashMap<String, String>> getAllJobDetail() {
		List<HashMap<String, String>> list = new ArrayList<HashMap<String, String>>();
		try {
			String[] jobGroups = scheduler.getJobGroupNames();
			for (String jobGroup : jobGroups) {
				String[] jobNames = scheduler.getJobNames(jobGroup);
				for (String jobName : jobNames) {
					HashMap<String, String> map = new HashMap<String, String>();
					JobDetail jobDetail = scheduler.getJobDetail(jobName, jobGroup);
					// job组
					map.put("jobGroup", jobGroup);
					// job名称
					map.put("jobName", jobDetail.getName());
					// job业务描述
					map.put("description", jobDetail.getDescription());
					// 以下返回该任务的trigger信息
					Trigger[] trigger = scheduler.getTriggersOfJob(jobName, jobGroup);
					// 执行计划表达式
					String cronExpression = null;
					// 任务启动时间
					String startTime = null;
					// 停止时间
					String previousFireTime = null;
					// 触发器状态编码
					int triggerState = 0;
					// 触发器描述
					String triggerDesc = null;
					// 触发器名称
					String triggerName=null;
					// 触发器组名
					String triggerGroup=null;
					if (trigger != null && trigger.length > 0) {
						cronExpression = ((CronTrigger) trigger[0]).getCronExpression();
						startTime = DateUtil.date2String(trigger[0].getStartTime());
						previousFireTime = DateUtil.date2String(trigger[0].getPreviousFireTime());
						triggerName=trigger[0].getName();
						triggerGroup=trigger[0].getGroup();
					}
					map.put("cronExpression", cronExpression);
					map.put("startTime", startTime);
					map.put("previousFireTime", previousFireTime);
					map.put("triggerName", triggerName);
					map.put("triggerGroup",triggerGroup);
					triggerState = scheduler.getTriggerState(trigger[0].getName(), trigger[0].getGroup());
					switch (triggerState) {
					case 0:
						triggerDesc = "正常";
						break;
					case 1:
						triggerDesc = "暂停";
						break;
					case 2:
						triggerDesc = "已完成";
						break;
					case 3:
						triggerDesc = "异常";
						break;
					case 4:
						triggerDesc = "阻塞";
						break;
					case -1:
						triggerDesc = "已删除";
						break;
					default:
						break;
					}
					map.put("triggerDesc", triggerDesc);
					// DELETED -1 COMPLETE 2 PAUSED 1 PAUSED_BLOCKED 1 ERROR 3 BLOCKED 4 否则 返回0
					/*
					 * None：Trigger已经完成，且不会在执行，或者找不到该触发器，或者Trigger已经被删除
					 * COMPLETE：触发器完成，但是任务可能还正在执行中 PAUSED：暂停状态 BLOCKED：线程阻塞状态
					 * NORMAL:正常状态 BLOCKED：线程阻塞状态
					 */
					HashMap<String, String> targerMap = new HashMap<String, String>();
					for (Object e : jobDetail.getJobDataMap().entrySet()) {
						Map.Entry<?, ?> entry = (Entry<?, ?>) e;
						targerMap.put(entry.getKey().toString(), entry.getValue().toString());
					}
					// 任务实现类
					map.put("targetObject", targerMap.get("targetObject").split("@")[0]);
					// 任务执行方法
					map.put("targetMethod", targerMap.get("targetMethod"));
					list.add(map);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return list;
	}
}
