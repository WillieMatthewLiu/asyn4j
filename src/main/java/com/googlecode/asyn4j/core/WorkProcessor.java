package com.googlecode.asyn4j.core;

import java.io.Serializable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Semaphore;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.googlecode.asyn4j.core.callback.AsynCallBack;
import com.googlecode.asyn4j.core.handler.ErrorAsynWorkHandler;
import com.googlecode.asyn4j.core.work.AsynWork;

/**
 * @author pan_java
 */

public final class WorkProcessor implements Serializable,Runnable ,Comparable<WorkProcessor>{
    private static final Log     log = LogFactory.getLog(WorkProcessor.class);
    private AsynWork             asynWork;
    private ApplicationContext applicationContext;

    public WorkProcessor(AsynWork asynWork,ApplicationContext applicationContext) {
        this.asynWork = asynWork;
        this.applicationContext = applicationContext;
    }

    @Override
    public void run() {
        Thread currentThread = Thread.currentThread();
        if (asynWork.getThreadName() != null) {
            setName(currentThread, asynWork.getThreadName());
        }
        AsynCallBack result = null;
        try {
            //asyn work execute
            result = asynWork.call();
            
            if (result != null) {//execute callback
            	applicationContext.getCallBackExecutor().execute(result);
            }
            
        } catch (Throwable throwable ) {
            //if execute asyn work is error,errorAsynWorkHandler disposal
            if (applicationContext.getErrorAsynWorkHandler() != null) {
            	applicationContext.getErrorAsynWorkHandler().addErrorWork(asynWork,throwable);
            }
        }finally{
        	applicationContext.getSemaphore().release();
        }
        

    }

    /**
     * set thread name
     * 
     * @param thread
     * @param name
     */
    private void setName(Thread thread, String name) {
        try {
            thread.setName(name);
        } catch (SecurityException se) {
            log.error(se);
        }
    }

    public AsynWork getAsynWork() {
        return asynWork;
    }
    
    @Override
    public int compareTo(WorkProcessor o) {
        return o.getAsynWork().getWeight() - this.asynWork.getWeight();
    }

}
