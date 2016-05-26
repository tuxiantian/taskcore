package com.ai.taskcore.jobs;

import com.ai.taskcore.quartz.IJob;
import com.ai.taskcore.service.IMyService1;

public class MyJobTest1 implements IJob {

	private static final long serialVersionUID = 1L;
	private IMyService1 myService1;

	public void setMyService1(IMyService1 myService1) {
		this.myService1 = myService1;
	}

	public void executeInternal() {
		myService1.doService();
	}

}
