package com.googlecode.asyn4j.core;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Semaphore;
import java.util.concurrent.atomic.AtomicLong;

import com.googlecode.asyn4j.core.handler.AsynServiceHandler;
import com.googlecode.asyn4j.core.handler.ErrorAsynWorkHandler;
import com.googlecode.asyn4j.core.handler.WorkQueueFullHandler;
import com.googlecode.asyn4j.service.AsynServiceImpl;


/**
 * Asyn4j ApplicationContext
 * @author panxiuyan
 *
 */
public class ApplicationContext implements Serializable{
	
	// asyn work default work weight
	protected static final WorkWeight                       DEFAULT_WORK_WEIGHT  = WorkWeight.MIDDLE;

	protected final static int                              CPU_NUMBER           = Runtime.getRuntime()
                                                                                       .availableProcessors();

	protected static ExecutorService                        workExecutor         = null;

	protected static ExecutorService                        callBackExecutor     = null;
    
    
 // call back block queue
	protected static BlockingQueue<Runnable>                callBackQueue        = null;

    // work queue
    protected static BlockingQueue<Runnable>              workQueue            = null;

    // status map
    protected static Map<String, Long>                      statMap              = new HashMap<String, Long>(3);
    
    
    protected WorkQueueFullHandler                          workQueueFullHandler = null;

    protected AsynServiceHandler serviceHandler = null;
    protected ErrorAsynWorkHandler errorAsynWorkHandler = null;


    // default work queue cache size
    protected static int                                    maxCacheWork         = 300;

    // default add work wait time
    protected static long                                   addWorkWaitTime      = 0L;

    // work thread pool size
    protected static int                                    work_thread_num      = (CPU_NUMBER / 2) + 1;

    // callback thread pool size
    protected static int                                    callback_thread_num  = CPU_NUMBER / 2;

    // close service wait time
    protected static long                                   closeServiceWaitTime = 2 * 60 * 1000;
    
    protected Semaphore                                     semaphore            = null;


    protected static AsynServiceImpl                        instance             = null;

    protected final static AtomicLong                       totalWork            = new AtomicLong(0);

    protected final static AtomicLong                       executeWorkNum       = new AtomicLong(0);

    protected final static AtomicLong                       callBackNum          = new AtomicLong(0);
    
    
    public ApplicationContext(int maxCacheWork, long addWorkWaitTime, int workThreadNum, int callBackThreadNum,
            long closeServiceWaitTime){
        this.maxCacheWork = maxCacheWork;
        this.addWorkWaitTime = addWorkWaitTime;
        this.work_thread_num = workThreadNum;
        this.callback_thread_num = callBackThreadNum;
        this.closeServiceWaitTime = closeServiceWaitTime;
        this.semaphore = new Semaphore(maxCacheWork);

    }


	public static ExecutorService getWorkExecutor() {
		return workExecutor;
	}
    
	public static ExecutorService getCallBackExecutor() {
		return callBackExecutor;
	}


	public WorkQueueFullHandler getWorkQueueFullHandler() {
		return workQueueFullHandler;
	}


	public AsynServiceHandler getServiceHandler() {
		return serviceHandler;
	}


	public ErrorAsynWorkHandler getErrorAsynWorkHandler() {
		return errorAsynWorkHandler;
	}


	public Semaphore getSemaphore() {
		return semaphore;
	}
	
	
	
	
	


	
    
    
    
    
    


    
    
    



}
