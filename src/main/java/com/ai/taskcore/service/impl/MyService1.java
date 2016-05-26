package com.ai.taskcore.service.impl;

import java.util.Date;

import com.ai.taskcore.service.IMyService1;
import com.ai.taskcore.util.DateUtil;

public class MyService1 implements IMyService1{

	private static final long serialVersionUID = 1L;
	@Override
	public void doService() {
		try {
			//下面的代码可以模拟任务阻塞
			/*Thread.sleep(10000);*/
		} catch (Exception e) {
			e.printStackTrace();
		}
		System.out.println("MyService1---"+DateUtil.date2String(new Date())+"---"+Thread.currentThread().getName());
	}

}
