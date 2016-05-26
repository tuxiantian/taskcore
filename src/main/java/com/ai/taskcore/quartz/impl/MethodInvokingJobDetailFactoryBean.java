package com.ai.taskcore.quartz.impl;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.quartz.Job;
import org.quartz.JobDetail;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.quartz.Scheduler;
import org.quartz.StatefulJob;
import org.springframework.beans.factory.BeanNameAware;
import org.springframework.beans.factory.FactoryBean;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.context.ApplicationContext;
import org.springframework.util.MethodInvoker;

public class MethodInvokingJobDetailFactoryBean implements FactoryBean<Object>,
		BeanNameAware, InitializingBean {
	private Log logger = LogFactory.getLog(getClass());
	private String group = Scheduler.DEFAULT_GROUP;
	private boolean concurrent = true;
	private boolean durable = false;
	private boolean volatility = false;
	private boolean shouldRecover = false;
	private String[] jobListenerNames;
	private String beanName;
	private JobDetail jobDetail;
	private String targetClass;
	private Object targetObject;
	private String targetMethod;
	private String staticMethod;
	private Object[] arguments;
	private String description;

	public String getTargetClass() {
		return targetClass;
	}

	public void setTargetClass(String targetClass) {
		this.targetClass = targetClass;
	}

	public String getTargetMethod() {
		return targetMethod;
	}

	public void setTargetMethod(String targetMethod) {
		this.targetMethod = targetMethod;
	}

	public Object getObject() throws Exception {
		return jobDetail;
	}

	public Class getObjectType() {
		return JobDetail.class;
	}

	public boolean isSingleton() {
		return true;
	}

	public void setBeanName(String beanName) {
		this.beanName = beanName;
	}

	public void afterPropertiesSet() throws Exception {
		try {
			logger.debug("start");

			logger.debug("Creating JobDetail " + beanName);
			jobDetail = new JobDetail();
			jobDetail.setName(beanName);
			jobDetail.setGroup(group);
			jobDetail.setJobClass(concurrent ? MethodInvokingJob.class
					: StatefulMethodInvokingJob.class);
			jobDetail.setDurability(durable);
			jobDetail.setVolatility(volatility);
			jobDetail.setRequestsRecovery(shouldRecover);
			jobDetail.setDescription(description);
			if (targetClass != null)
				jobDetail.getJobDataMap().put("targetClass", targetClass);
			if (targetObject != null)
				jobDetail.getJobDataMap().put("targetObject", targetObject);
			if (targetMethod != null)
				jobDetail.getJobDataMap().put("targetMethod", targetMethod);
			if (staticMethod != null)
				jobDetail.getJobDataMap().put("staticMethod", staticMethod);
			if (arguments != null)
				jobDetail.getJobDataMap().put("arguments", arguments);

			logger.debug("Registering JobListener names with JobDetail object "
					+ beanName);
			if (this.jobListenerNames != null) {
				for (int i = 0; i < this.jobListenerNames.length; i++) {
					this.jobDetail.addJobListener(this.jobListenerNames[i]);
				}
			}
			logger.info("Created JobDetail: " + jobDetail + "; targetClass: "
					+ targetClass + "; targetObject: " + targetObject
					+ "; targetMethod: " + targetMethod + "; staticMethod: "
					+ staticMethod + "; arguments: " + arguments + ";");
		} finally {
			logger.debug("end");
		}
	}

	public void setConcurrent(boolean concurrent) {
		this.concurrent = concurrent;
	}

	public void setDurable(boolean durable) {
		this.durable = durable;
	}

	public void setGroup(String group) {
		this.group = group;
	}

	public void setJobListenerNames(String[] jobListenerNames) {
		this.jobListenerNames = jobListenerNames;
	}

	public void setShouldRecover(boolean shouldRecover) {
		this.shouldRecover = shouldRecover;
	}

	public void setVolatility(boolean volatility) {
		this.volatility = volatility;
	}
	
	public void setDescription(String description) {
		this.description = description;
	}
	public static class MethodInvokingJob implements Job {
		protected Log logger = LogFactory.getLog(getClass());

		public void execute(JobExecutionContext context)
				throws JobExecutionException {
			try {
				logger.debug("start");
				String targetClass = context.getMergedJobDataMap().getString(
						"targetClass");
				Class targetClassClass = null;
				if (targetClass != null) {
					targetClassClass = Class.forName(targetClass); 
				}
				Object targetObject = context.getMergedJobDataMap().get("targetObject");
				if (targetObject instanceof BootstrapJob) {
					ApplicationContext ac = (ApplicationContext) context.getScheduler().getContext().get("applicationContext");
					BootstrapJob target = (BootstrapJob) targetObject;
					target.execute(ac);
				} else {
					String targetMethod = context.getMergedJobDataMap().getString("targetMethod");
					String staticMethod = context.getMergedJobDataMap().getString("staticMethod");
					Object[] arguments = (Object[]) context.getMergedJobDataMap().get("arguments");
					MethodInvoker methodInvoker = new MethodInvoker();
					methodInvoker.setTargetClass(targetClassClass);
					methodInvoker.setTargetObject(targetObject);
					methodInvoker.setTargetMethod(targetMethod);
					methodInvoker.setStaticMethod(staticMethod);
					methodInvoker.setArguments(arguments);
					methodInvoker.prepare();
					methodInvoker.invoke();
				}
			} catch (Exception e) {
				throw new JobExecutionException(e);
			} finally {
				logger.debug("end");
			}
		}
	}

	public static class StatefulMethodInvokingJob extends MethodInvokingJob
			implements StatefulJob {
		// No additional functionality; just needs to implement StatefulJob.
	}

	public Object[] getArguments() {
		return arguments;
	}

	public void setArguments(Object[] arguments) {
		this.arguments = arguments;
	}

	public String getStaticMethod() {
		return staticMethod;
	}

	public void setStaticMethod(String staticMethod) {
		this.staticMethod = staticMethod;
	}

	public void setTargetObject(Object targetObject) {
		this.targetObject = targetObject;
	}
}
