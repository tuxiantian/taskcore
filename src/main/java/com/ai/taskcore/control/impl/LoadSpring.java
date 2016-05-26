package com.ai.taskcore.control.impl;

import org.springframework.context.support.ClassPathXmlApplicationContext;

public class LoadSpring {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		try{
			ClassPathXmlApplicationContext context = new ClassPathXmlApplicationContext(new String[] {"spring/spring-all.xml"});
	        context.start();
	        System.out.println("taskCore已启动!");
	        synchronized (LoadSpring.class) {
	            try {
	            	LoadSpring.class.wait();
	            } catch (Exception e) {
	            	e.printStackTrace();
	            }
	        }
		}catch(Exception e){
			e.printStackTrace();
		}
	}

}
