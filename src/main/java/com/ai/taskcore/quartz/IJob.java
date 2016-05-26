package com.ai.taskcore.quartz;

import java.io.Serializable;

public interface IJob extends Serializable{
	/** 
	 * 处理任务的核心函数 
	 * @param cxt Spring 上下文 
	 */
	void executeInternal();
}
