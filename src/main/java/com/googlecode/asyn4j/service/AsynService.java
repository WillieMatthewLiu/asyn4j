package com.googlecode.asyn4j.service;

import java.util.Map;

import com.googlecode.asyn4j.core.WorkWeight;
import com.googlecode.asyn4j.core.callback.AsynCallBack;
import com.googlecode.asyn4j.core.handler.AsynServiceHandler;
import com.googlecode.asyn4j.core.handler.ErrorAsynWorkHandler;
import com.googlecode.asyn4j.core.handler.WorkQueueFullHandler;
import com.googlecode.asyn4j.core.work.AsynWork;



/**
 * @author pan_java
 */
public interface AsynService {
	
	
	/**
	 * service init status
	 */
	public final static int SERVICE_INIT = 0;
	
	/**
	 * service close status
	 */
	public final static int SERVICE_CLOSE = 1;
    
    /**
     * 
     * @param params
     * @param tagerObject
     * @param method
     */
    public void addWork(Object tagerObject, String method);

    /**
     * add asyn work
     * 
     * @param params －－ params
     * @param clzss －－ traget ClASS
     * @param method －－target method name
     * @param cache -- target object cache falg
     */
    public void addWork(Object tagerObject, String method,Object[] params);

    /**
     * add asyn work
     * 
     * @param params －－ params
     * @param clzss －－ traget ClASS
     * @param method －－ target method name
     * @param asynCallBack --callback method
     */
    public void addWork(Object tagerObject, String method,Object[] params, AsynCallBack asynCallBack);

    
    /**
     * add asyn work
     * 
     * @param params －－ params
     * @param clzss －－ traget Object
     * @param method －－ target method name
     * @param asynCallBack －－ callback method
     * @param weight －－ work weight
     */
    public void addWork(Object tagerObject, String method, Object[] params,AsynCallBack asynCallBack, WorkWeight weight);
    
    
    /**
     * 
     * @param tagerObject －－ traget Object 
     * @param method－－ target method name
     * @param params －－ params
     * @param asynCallBack －－ callback
     * @param weight－－ work weight
     * @param cache －－  target is cache
     */
    public void addWork(Object tagerObject, String method, Object[] params, AsynCallBack asynCallBack, WorkWeight weight,
            boolean cache);


    /**
     *  add asyn work with Spring Bean
     * 
     * @param params －－ params
     * @param target －－ target bean name
     * @param method －－ target method name
     * @param asynCallBack －－target method name
     * @param weight －－work weight
     */
    public void addWorkWithSpring( String target, String method,Object[] params, AsynCallBack asynCallBack, WorkWeight weight);

    /**
     * add asyn work
     * 
     * @param asynWork －－ asyn work entity
     */
    public void addAsynWork(AsynWork asynWork);

    /**
     * get run stat map
     * 
     * @return
     */
    public Map<String, Long> getRunStatMap();

    /**
     * get run stat string
     * 
     * @return
     */
    public String getRunStatInfo();

    /**
     * add work cache work queue
     * 
     * @param workQueueFullHandler
     */
    public void setWorkQueueFullHandler(WorkQueueFullHandler workQueueFullHandler);

    /**
     * start service
     */
    public void init();

    /**
     * close service
     * @ wait time
     */
    public void close(long waitTime);
    
    /**
     * close service
     */
    public void close();

    /**
     * set close service handler
     * 
     * @param closeHander
     */
    public void setServiceHandler(AsynServiceHandler serviceHandler);

    /**
     * set error asyn work handler
     * 
     * @param errorAsynWorkHandler
     */
    public void setErrorAsynWorkHandler(ErrorAsynWorkHandler errorAsynWorkHandler);

}
