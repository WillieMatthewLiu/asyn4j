package com.googlecode.asyn4j.core.handler;

import java.util.concurrent.BlockingQueue;

import com.googlecode.asyn4j.core.callback.AsynCallBack;
import com.googlecode.asyn4j.core.work.AsynWork;
import com.googlecode.asyn4j.service.AsynService;

/**
 * TODO Comment of AsynServiceCloseHandler
 * 
 * @author pan_java
 * @version AsynServiceCloseHandler.java 2010-8-27 下午07:29:17
 */
public abstract class AsynServiceHandler implements AsynHandler{
	
	
	private int serviceStat = AsynService.SERVICE_INIT;

    protected BlockingQueue<Runnable>     asynWorkQueue;

    protected BlockingQueue<Runnable> callBackQueue;
    
    protected AsynService asynService;

    public void setAsynWorkQueue(BlockingQueue<Runnable> asynWorkQueue) {
        this.asynWorkQueue = asynWorkQueue;
    }

    public void setCallBackQueue(BlockingQueue<Runnable> callBackQueue) {
        this.callBackQueue = callBackQueue;
    }
    
    public void setServiceStat(int serviceStat) {
		this.serviceStat = serviceStat;
	}
    
    public void setAsynService(AsynService asynService) {
		this.asynService = asynService;
	}

	/***
     * do close asyn service if you have set the workQueueFullHandler then
     * workQueueFullHandler a queue ,must save the workQueueFullHandler queue
     */
    public  void process(){
    	if(serviceStat==AsynService.SERVICE_INIT){
    		this.init();
    	}else{
    		this.destroy();
    	}
    }
    
    /**
     * init
     */
    public abstract void init();
    
    /**
     * destroy
     */
    public abstract void destroy();

}
