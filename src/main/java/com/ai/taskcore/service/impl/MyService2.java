package com.ai.taskcore.service.impl;

import java.util.Date;

import com.ai.taskcore.service.IMyService2;
import com.ai.taskcore.util.DateUtil;

public class MyService2 implements IMyService2{

	private static final long serialVersionUID = 1L;
	@Override
	public void doService() {
		System.out.println("MyService2---"+DateUtil.date2String(new Date())+"---"+Thread.currentThread().getName());
	}

}
