package com.googlecode.asyn4j.core.handler;

import com.googlecode.asyn4j.core.work.AsynWork;
import com.googlecode.asyn4j.service.AsynService;

/**
 * 
 * @author pan_java
 *
 */
public abstract class WorkQueueFullHandler implements AsynHandler{
	
	protected AsynService  asynService;
	
	/**
	 * add the asynwork to handler
	 * @param synWork
	 */
	public abstract boolean addAsynWork(AsynWork  asynWork);
	
	/**
	 * process asynwork 
	 */
	public abstract void process();

	public void setAsynService(AsynService asynService) {
		this.asynService = asynService;
	}
	
	

}
