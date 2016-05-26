package com.ai.taskcore.quartz.impl;

import java.io.Serializable;

import org.springframework.context.ApplicationContext;

public class BootstrapJob implements Serializable {

	private static final long serialVersionUID = 4195235887559214105L;
	private String targetJob;

	public void execute(ApplicationContext cxt) {
		Job job = (Job) cxt.getBean(this.targetJob);
		job.execute();
	}

	public String getTargetJob() {
		return targetJob;
	}

	public void setTargetJob(String targetJob) {
		this.targetJob = targetJob;
	}
}
