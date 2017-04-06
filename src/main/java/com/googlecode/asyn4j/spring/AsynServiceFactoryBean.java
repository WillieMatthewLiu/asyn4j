package com.googlecode.asyn4j.spring;

import java.lang.reflect.Constructor;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.FactoryBean;

import com.googlecode.asyn4j.core.handler.AsynServiceHandler;
import com.googlecode.asyn4j.core.handler.ErrorAsynWorkHandler;
import com.googlecode.asyn4j.core.handler.WorkQueueFullHandler;
import com.googlecode.asyn4j.service.AsynService;
import com.googlecode.asyn4j.service.AsynServiceImpl;

public class AsynServiceFactoryBean implements FactoryBean {

    private final static Log        log                  = LogFactory.getLog(AsynServiceFactoryBean.class);

    private final static int        CPU_NUMBER           = Runtime.getRuntime().availableProcessors();

    // default work queue cache size
    private int                     maxCacheWork         = 300;

    // default add work wait time
    private long                    addWorkWaitTime      = Long.MAX_VALUE;

    // work thread pool size
    private int                     workThreadNum        = (CPU_NUMBER / 2) + 1;

    // callback thread pool size
    private int                     callbackThreadNum    = CPU_NUMBER / 2;
    
    //close service wait time
    private long                    closeServiceWaitTime = 60 * 1000;

    private WorkQueueFullHandler    workQueueFullHandler;

    private ErrorAsynWorkHandler    errorAsynWorkHandler;

    private AsynServiceHandler asynServiceCloseHandler;

    public void setMaxCacheWork(int maxCacheWork) {
        this.maxCacheWork = maxCacheWork;
    }

    public void setAddWorkWaitTime(long addWorkWaitTime) {
        this.addWorkWaitTime = addWorkWaitTime;
    }

    public void setWorkThreadNum(int workThreadNum) {
        this.workThreadNum = workThreadNum;
    }

    public void setCallbackThreadNum(int callbackThreadNum) {
        this.callbackThreadNum = callbackThreadNum;
    }

    public void setCloseServiceWaitTime(long closeServiceWaitTime) {
        this.closeServiceWaitTime = closeServiceWaitTime;
    }

    public WorkQueueFullHandler getWorkQueueFullHandler() {
        return workQueueFullHandler;
    }

    public void setWorkQueueFullHandler(WorkQueueFullHandler workQueueFullHandler) {
        this.workQueueFullHandler = workQueueFullHandler;
    }

    public ErrorAsynWorkHandler getErrorAsynWorkHandler() {
        return errorAsynWorkHandler;
    }

    public void setErrorAsynWorkHandler(ErrorAsynWorkHandler errorAsynWorkHandler) {
        this.errorAsynWorkHandler = errorAsynWorkHandler;
    }

    public AsynServiceHandler getAsynServiceCloseHandler() {
        return asynServiceCloseHandler;
    }

    public void setAsynServiceCloseHandler(AsynServiceHandler asynServiceCloseHandler) {
        this.asynServiceCloseHandler = asynServiceCloseHandler;
    }

    @Override
    public Object getObject() throws Exception {
        AsynService asynService = AsynServiceImpl.getService(maxCacheWork, addWorkWaitTime, workThreadNum,
                callbackThreadNum,closeServiceWaitTime);
        //set some handler
        if (workQueueFullHandler != null) {
            asynService.setWorkQueueFullHandler(workQueueFullHandler);
        }
        if (errorAsynWorkHandler != null) {
            asynService.setErrorAsynWorkHandler(errorAsynWorkHandler);
        }
        if (asynServiceCloseHandler != null) {
            asynService.setServiceHandler(asynServiceCloseHandler);
        }
        asynService.init();
        return asynService;
    }

    @Override
    public Class getObjectType() {
        return AsynServiceImpl.class;
    }

    @Override
    public boolean isSingleton() {
        return true;
    }

    /**
     * init object
     * 
     * @param className
     * @return
     */
    private Object newObject(String className) {
        Class clzss = null;
        try {
            clzss = Class.forName(className);
            Constructor constructor = clzss.getConstructor();
            if (constructor != null) {
                return constructor.newInstance();
            }
        } catch (Exception e) {
            log.error(e);
        }
        return null;
    }

}
