package com.ai.taskcore.quartz;

import java.util.HashMap;
import java.util.List;

import com.ai.frame.bean.InputObject;
import com.ai.frame.bean.OutputObject;


public interface IQuartzService {

	/**
	 * 修改job描述信息
	 * 
	 * @param triggerName
	 * @param triggerGroup
	 * @param trigger
	 * @return
	 */
	public void updateJobDesc(InputObject inputObject, OutputObject outputObject) throws Exception;

	/**
	 * 更改执行计划
	 * 
	 * @param triggerName
	 * @param triggerGroup
	 * @param cronTrigger
	 * @return
	 */
	public void updateCronExpression(InputObject inputObject, OutputObject outputObject) throws Exception;

	/**
	 * 修改job名称
	 * 
	 * @param jobDetail
	 * @return
	 */
	public void updateJobName(InputObject inputObject, OutputObject outputObject) throws Exception;

	/**
	 * 删除Job,直接从数据库中删除，关联表数据也删除
	 * 
	 * @param triggerName
	 * @param triggerGroup
	 * @param jobName
	 * @param jobGroup
	 */
	public void deleteJob(InputObject inputObject, OutputObject outputObject) throws Exception;

	/**
	 * 暂停某个触发器
	 * 
	 * @param triggerName
	 * @param triggerGroup
	 * @return
	 */
	public void pauseTrigger(InputObject inputObject, OutputObject outputObject) throws Exception;

	/**
	 * 恢复已暂停的触发器
	 * 
	 * @param triggerName
	 * @param triggerGroup
	 * @return
	 */
	public void resumeTrigger(InputObject inputObject, OutputObject outputObject) throws Exception;
	
	/**
	 * 立即执行JOB
	 * 
	 * @param jobName
	 * @param jobGroup
	 * @return
	 */
	public void runAJobNow(InputObject inputObject, OutputObject outputObject) throws Exception;

	/**
	 * 暂停所有触发器
	 * 
	 * @return
	 */
	public void pauseAllTrigger(InputObject inputObject, OutputObject outputObject) throws Exception;

	/**
	 * 启动所有触发器
	 * 
	 * @return
	 */
	public void startAllTrigger(InputObject inputObject, OutputObject outputObject) throws Exception;

	/**
	 * 停止所有触发器,停止后无法启动，无法调用start方法
	 * 
	 * @return
	 */
	public void shutdownTrigger(InputObject inputObject, OutputObject outputObject) throws Exception;

	/**
	 * 返回所有jobDetail
	 * 
	 * @return
	 */
	public void getAllJobDetail(InputObject inputObject, OutputObject outputObject) throws Exception;
}
