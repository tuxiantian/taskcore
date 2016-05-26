package com.ai.taskcore.jobs;

import com.ai.taskcore.quartz.IJob;
import com.ai.taskcore.service.IMyService2;

public class MyJobTest2 implements IJob{

	private static final long serialVersionUID = 1L;
	private IMyService2 myService2;
	public void setMyService2(IMyService2 myService2) {
		this.myService2 = myService2;
	}
	
	public void executeInternal() {
		myService2.doService();
	}
}
